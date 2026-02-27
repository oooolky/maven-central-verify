package com.cs6650.shoppingcartservice;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class CheckoutController {

  private final RestClient ccaClient;
  private final RabbitTemplate rabbitTemplate;
  private final String queueName;

  // In-memory data structures required for the high-concurrency load test
  private final ConcurrentHashMap<Integer, List<CartItem>> carts = new ConcurrentHashMap<>();
  private final AtomicInteger orderIdGenerator = new AtomicInteger(1);

  public CheckoutController(
      @Value("${cca.base-url}") String ccaBaseUrl,
      RabbitTemplate rabbitTemplate,
      @Value("${ship.queue}") String queueName
  ) {
    this.ccaClient = RestClient.builder().baseUrl(ccaBaseUrl).build();
    this.rabbitTemplate = rabbitTemplate;
    this.queueName = queueName;
  }

  // Align with OpenAPI Record specifications
  public record CheckoutReq(String credit_card_number) {}
  public record CheckoutResp(Integer order_id) {}
  public record CartItem(Integer productId, Integer quantity) {}

  // Message sent to RabbitMQ (must include product details for the Warehouse to process)
  public record ShipMessage(
      String orderId,
      Integer shoppingCartId,
      List<Item> items,
      long createdAtEpochMs
  ) {
    public record Item(Integer productId, Integer quantity) {}
  }

  // Create new /health GET to align with other services
  @GetMapping({"/health", "/shopping-cart/health", "/shopping-carts/health"})
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("Shopping Cart Service is running");
  }


  @PostMapping("/shopping-carts/{shoppingCartId}/checkout")
  public ResponseEntity<?> checkout(
    @PathVariable Integer shoppingCartId,
    @RequestBody CheckoutReq req) {
      
      // Load testing mock data:
      // Since the load tester skips the "add items" step, we inject mock items here
      // so the Warehouse has data to tally when the message is consumed.
      List<CartItem> cartItems = carts.computeIfAbsent(shoppingCartId, id -> List.of(new CartItem(101, 2), new CartItem(205, 1)));
    
      // 1. Synchronous call to Credit Card Authorizer (CCA)
      try {
        ccaClient.post()
          .uri("/credit-card-authorizer/authorize")
          .contentType(MediaType.APPLICATION_JSON)
          .body(Map.of("credit_card_number", req.credit_card_number()))
          .retrieve()
          .toBodilessEntity();
      } catch (HttpClientErrorException e) {
        // Error 400 or 402, return immediately to client
        return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
      } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("CCA unavailable");
      }

    // 2. Payment Success: Send full cart details to RabbitMQ
    String orderId = UUID.randomUUID().toString();
    ShipMessage msg = new ShipMessage(
        orderId,
        shoppingCartId,
        cartItems.stream().map(i -> new ShipMessage.Item(i.productId(), i.quantity())).toList(),
        System.currentTimeMillis()
    );

    CorrelationData cd = new CorrelationData(orderId); // 或 UUID.randomUUID().toString()

    rabbitTemplate.convertAndSend("", queueName, msg, cd);

// 3. Wait for RabbitMQ Publisher Confirm (Assignment Requirement)
    try {
      CorrelationData.Confirm confirm = cd.getFuture().get(5, TimeUnit.SECONDS);
      if (confirm == null || !confirm.isAck()) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("RMQ publish not confirmed");
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("RMQ confirm timeout/failure");
    }

    // 4. All success: Generate order ID and return 200 OK
    return ResponseEntity.ok(new CheckoutResp(orderIdGenerator.getAndIncrement()));
  }
}