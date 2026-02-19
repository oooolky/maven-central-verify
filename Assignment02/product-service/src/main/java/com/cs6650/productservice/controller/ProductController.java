package com.cs6650.productservice.controller;

import com.cs6650.productservice.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class ProductController {

    // Thread-safe counter for generating product IDs in responses
    private final AtomicInteger productIdCounter = new AtomicInteger(1);

    /**
     * Create a new product.
     *
     * Receives product data via POST request and returns HTTP 201.
     *
     * @param product the product data from request body (JSON)
     * @return HTTP 201 Created with the generated product ID
     */
    @PostMapping("/product")
    public ResponseEntity<Map<String, Integer>> createProduct(@RequestBody Product product) {
        // Generate a product ID for the response
        int generatedId = productIdCounter.getAndIncrement();

        // Return 201 Created with the product ID
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("product_id", generatedId));
    }

    /**
     * Health check endpoint for container orchestration.
     *
     * @return simple health status message with HTTP 200
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Product Service is running!");
    }
}