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

    /**
     * Authorize a credit card transaction.
     * - Returns 400 if card number format is invalid (must be DDDD-DDDD-DDDD-DDDD)
     * - Returns 200 Authorized for 90% of valid requests
     * - Returns 402 Declined for 10% of valid requests
     */
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

    /**
     * Health check endpoint for ALB and container orchestration.
     * Must always return 200 OK.
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("CCA Service is running!");
    }
}