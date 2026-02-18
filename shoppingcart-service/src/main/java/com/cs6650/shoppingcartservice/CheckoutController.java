package com.cs6650.shoppingcartservice;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/shopping-cart")
public class CheckoutController {

  public record Item(long productId, int quantity) {}
  public record CheckoutRequest(String shoppingCartId, String creditCard, List<Item> items) {}

  public record ShipMessage(String orderId, String shoppingCartId, List<Item> items, long createdAtEpochMs) {}

  private final RestClient restClient;
  private final RabbitTemplate rabbitTemplate;
  private final String shipQueue;
  private final long confirmTimeoutMs;

  public CheckoutController(
      RabbitTemplate rabbitTemplate,
      @Value("${cca.base-url}") String ccaBaseUrl,
      @Value("${ship.queue}") String shipQueue,
      @Value("${rmq.confirm-timeout-ms}") long confirmTimeoutMs
  ) {
    this.rabbitTemplate = rabbitTemplate;
    this.shipQueue = shipQueue;
    this.confirmTimeoutMs = confirmTimeoutMs;
    this.restClient = RestClient.builder().baseUrl(ccaBaseUrl).build();
  }

  @PostMapping("/checkout")
  public ResponseEntity<String> checkout(@RequestBody CheckoutRequest req) {
    if (req == null || req.creditCard() == null) {
      return ResponseEntity.badRequest().body("Bad Request");
    }

    ResponseEntity<String> ccaResp = restClient.post()
        .uri("/credit-card/authorize")
        .contentType(MediaType.APPLICATION_JSON)
        .body(new CcaReq(req.creditCard()))
        .retrieve()
        .toEntity(String.class);

    int status = ccaResp.getStatusCode().value();
    if (status == 400) return ResponseEntity.badRequest().body("Bad credit card format");
    if (status == 402) return ResponseEntity.status(402).body("Declined");
    if (status != 200) return ResponseEntity.status(502).body("CCA error: " + status);

    String orderId = UUID.randomUUID().toString();
    ShipMessage msg = new ShipMessage(
        orderId,
        req.shoppingCartId() == null ? "loadtest" : req.shoppingCartId(),
        req.items() == null || req.items().isEmpty()
            ? List.of(new Item(1L, 1))  // 默认给个 item，便于 warehouse 统计
            : req.items(),
        Instant.now().toEpochMilli()
    );

    try {
      rabbitTemplate.convertAndSend("", shipQueue, msg);

      boolean ok = rabbitTemplate.waitForConfirms(confirmTimeoutMs);
      if (!ok) {
        return ResponseEntity.status(503).body("RabbitMQ confirm timeout");
      }
      return ResponseEntity.ok("OK orderId=" + orderId);

    } catch (Exception e) {
      return ResponseEntity.status(503).body("RabbitMQ publish failed: " + e.getMessage());
    }
  }

  private record CcaReq(String creditCard) {}
}
