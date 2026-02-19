package com.cs6650.client;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ProductApi;
import io.swagger.client.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Task that sends multiple POST requests to the Product API
 * and records timing statistics
 *
 * Each request's start time, latency, and response code are recorded
 * for later statistical analysis. Records are stored in a thread-local
 * list to avoid synchronization overhead during the test.
 */
public class RequestTaskWithStats implements Runnable {

    private static final int MAX_RETRIES = 5;

    private final ProductApi productApi;
    private final ProductGenerator productGenerator;
    private final int requestCount;
    private final CountDownLatch latch;
    private final AtomicInteger successCounter;
    private final AtomicInteger failureCounter;

    // Thread-local storage for request records to minimize synchronization
    private final List<RequestRecord> records;

    public RequestTaskWithStats(String serverUrl, int requestCount, CountDownLatch latch,
                                AtomicInteger successCounter, AtomicInteger failureCounter) {
        ApiClient apiClient = new ApiClient();
        apiClient.updateBaseUri(serverUrl);

        this.productApi = new ProductApi(apiClient);
        this.productGenerator = new ProductGenerator();
        this.requestCount = requestCount;
        this.latch = latch;
        this.successCounter = successCounter;
        this.failureCounter = failureCounter;
        this.records = new ArrayList<>(requestCount);
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < requestCount; i++) {
                sendRequestWithRetry();
            }
        } finally {
            latch.countDown();
        }
    }

    /**
     * Sends a single POST request with retry logic and records timing
     */
    private void sendRequestWithRetry() {
        Product product = productGenerator.generateRandomProduct();

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            long startTime = System.currentTimeMillis();

            try {
                productApi.createProduct(product);
                long endTime = System.currentTimeMillis();
                long latency = endTime - startTime;

                // Record successful request
                records.add(new RequestRecord(startTime, "POST", latency, 201));
                successCounter.incrementAndGet();
                return;

            } catch (ApiException e) {
                long endTime = System.currentTimeMillis();
                long latency = endTime - startTime;
                int statusCode = e.getCode();

                // Retry on 4xx or 5xx errors
                if ((statusCode >= 400 || statusCode == 0) && attempt < MAX_RETRIES) {
                    continue;
                }

                // Final attempt
                if (attempt == MAX_RETRIES) {
                    records.add(new RequestRecord(startTime, "POST", latency, statusCode));
                    failureCounter.incrementAndGet();
                }
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                long latency = endTime - startTime;

                if (attempt == MAX_RETRIES) {
                    records.add(new RequestRecord(startTime, "POST", latency, 0));
                    failureCounter.incrementAndGet();
                }
            }
        }
    }

    /**
     * Returns the collected request records
     * Should only be called after the task has completed
     */
    public List<RequestRecord> getRecords() {
        return records;
    }
}