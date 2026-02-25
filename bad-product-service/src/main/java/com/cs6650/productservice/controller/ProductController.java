package com.cs6650.productservice.controller;

import com.cs6650.productservice.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class ProductController {

    private final AtomicInteger productIdCounter = new AtomicInteger(1);

    /**
     * Create a new product.
     * BAD VERSION: Returns 503 Service Unavailable 50% of the time
     * to simulate a failing instance for ALB Automatic Target Weights demo.
     *
     * @param product the product data from request body (JSON)
     * @return HTTP 201 Created (50%) or HTTP 503 Service Unavailable (50%)
     */
    @PostMapping("/product")
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Product product) {
        // 50% chance of simulated failure
        if (ThreadLocalRandom.current().nextDouble() < 0.5) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("message", "Service temporarily unavailable"));
        }

        int generatedId = productIdCounter.getAndIncrement();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("product_id", generatedId));
    }

    /**
     * Health check endpoint — ALWAYS returns 200.
     * Must never return 503 so the ALB keeps this instance in service,
     * allowing ATW to demonstrate traffic-shifting behavior.
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Bad Product Service is running!");
    }
}