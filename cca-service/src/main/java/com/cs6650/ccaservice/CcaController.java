package com.cs6650.ccaservice;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CcaController {

  private static final Pattern CC_PATTERN = Pattern.compile("^\\d{4}-\\d{4}-\\d{4}-\\d{4}$");

  public record CcaRequest(String creditCard, String credit_card_number) {}
  public record CcaResponse(String result) {}

  /**
   * Support both route styles during integration
   * 1) /credit-card/authorize
   * 2) /credit-card-authorizer/authorize
   */
  @PostMapping({"/credit-card/authorize", "/credit-card-authorizer/authorize"})
  public ResponseEntity<CcaResponse> authorize(@RequestBody CcaRequest req) {
    String cc = extractCardNumber(req);
    if (cc == null || !CC_PATTERN.matcher(cc).matches()) {
      return ResponseEntity.badRequest().body(new CcaResponse("Bad Request"));
    }

    // 90% authorized, 10% declined
    int r = ThreadLocalRandom.current().nextInt(100);
    if (r < 90) {
      return ResponseEntity.ok(new CcaResponse("Authorized"));
    } else {
      return ResponseEntity.status(402).body(new CcaResponse("Declined"));
    }
  }

  @GetMapping({"/health", "/credit-card/health", "/credit-card-authorizer/health"})
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("CCA Service is running");
  }

  private String extractCardNumber(CcaRequest req) {
    if (req == null) {
      return null;
    }
    if (req.credit_card_number() != null && !req.credit_card_number().isBlank()) {
      return req.credit_card_number();
    }
    return req.creditCard();
  }
}
