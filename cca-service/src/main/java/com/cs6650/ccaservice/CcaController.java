package com.cs6650.ccaservice;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credit-card")
public class CcaController {

  private static final Pattern CC_PATTERN = Pattern.compile("^\\d{4}-\\d{4}-\\d{4}-\\d{4}$");

  public record CcaRequest(String creditCard) {}
  public record CcaResponse(String result) {}

  @PostMapping("/authorize")
  public ResponseEntity<CcaResponse> authorize(@RequestBody CcaRequest req) {
    String cc = req == null ? null : req.creditCard();
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
}
