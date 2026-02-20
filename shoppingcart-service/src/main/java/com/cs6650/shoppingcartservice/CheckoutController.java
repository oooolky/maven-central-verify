package com.cs6650.shoppingcartservice;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;

@RestController
@RequestMapping("/shopping-cart")
public class CheckoutController {

  private final RestClient ccaClient;
  private final RabbitTemplate rabbitTemplate;
  private final String queueName;

  public CheckoutController(
      @Value("${cca.base-url}") String ccaBaseUrl,
      RabbitTemplate rabbitTemplate,
      @Value("${ship.queue}") String queueName
  ) {
    this.ccaClient = RestClient.builder().baseUrl(ccaBaseUrl).build();
    this.rabbitTemplate = rabbitTemplate;
    this.queueName = queueName;
  }

  public record CheckoutReq(String shoppingCartId, String creditCard) {}

  @PostMapping("/checkout")
  public ResponseEntity<String> checkout(@RequestBody CheckoutReq req) {
    // 1) Call CCA
    ResponseEntity<String> ccaResp;
    try {
      ccaResp = ccaClient.post()
          .uri("/credit-card/authorize")
          .contentType(MediaType.APPLICATION_JSON)
          .body(req == null ? "{}" : "{\"creditCard\":\"" + req.creditCard() + "\"}")
          .retrieve()
          .toEntity(String.class);
    } catch (org.springframework.web.client.HttpClientErrorException e) {
      // includes 400/402 etc
      return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
    } catch (Exception e) {
      return ResponseEntity.status(502).body("CCA unavailable");
    }

    if (!ccaResp.getStatusCode().is2xxSuccessful()) {
      // CCA declined or bad request
      return ResponseEntity.status(ccaResp.getStatusCode()).body(ccaResp.getBody());
    }

    // 2) Authorized -> publish to RabbitMQ, wait for publisher confirm
    String msg = "ship cart=" + req.shoppingCartId() + " at=" + Instant.now().toEpochMilli();

    CorrelationData cd = new CorrelationData();
    rabbitTemplate.convertAndSend("", queueName, msg, cd);

    try {
      // wait confirm (publisher confirms)
      CorrelationData.Confirm confirm = cd.getFuture().get(5, TimeUnit.SECONDS);
      if (confirm == null || !confirm.isAck()) {
        return ResponseEntity.status(502).body("RMQ publish not confirmed");
      }
    } catch (Exception e) {
      return ResponseEntity.status(502).body("RMQ confirm timeout/failure");
    }

    return ResponseEntity.ok("{\"result\":\"OK\"}");
  }
}
