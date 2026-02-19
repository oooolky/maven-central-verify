package com.cs6650.client;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ProductApi;
import io.swagger.client.model.Product;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Task that sends multiple POST requests to the Product API
 *
 * Each task is assigned a portion of the total requests and executes them
 * sequentially within its own thread. Failed requests are retried up to
 * MAX_RETRIES times before being counted as failures.
 */
public class RequestTask implements Runnable {

    private static final int MAX_RETRIES = 5;

    private final ProductApi productApi;
    private final ProductGenerator productGenerator;
    private final int requestCount;
    private final CountDownLatch latch;
    private final AtomicInteger successCounter;
    private final AtomicInteger failureCounter;

    /**
     * Creates new RequestTask
     *
     * @param serverUrl the base URL of the server
     * @param requestCount number of requests this task should send
     * @param latch countdown latch to signal completion
     * @param successCounter shared counter for successful requests
     * @param failureCounter shared counter for failed requests
     */
    public RequestTask(String serverUrl, int requestCount, CountDownLatch latch,
                       AtomicInteger successCounter, AtomicInteger failureCounter) {
        ApiClient apiClient = new ApiClient();
        apiClient.updateBaseUri(serverUrl);

        this.productApi = new ProductApi(apiClient);
        this.productGenerator = new ProductGenerator();
        this.requestCount = requestCount;
        this.latch = latch;
        this.successCounter = successCounter;
        this.failureCounter = failureCounter;
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
     * Sends single POST request with retry logic
     * Retries up to MAX_RETRIES times to avoid 4xx or 5xx errors
     */
    private void sendRequestWithRetry() {
        Product product = productGenerator.generateRandomProduct();

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                productApi.createProduct(product);
                successCounter.incrementAndGet();
                return;
            } catch (ApiException e) {
                int statusCode = e.getCode();

                // Retry on 4xx or 5xx errors
                if ((statusCode >= 400 || statusCode == 0) && attempt < MAX_RETRIES) {
                    continue;
                }

                // Final attempt failed
                if (attempt == MAX_RETRIES) {
                    failureCounter.incrementAndGet();
                }
            } catch (Exception e) {
                if (attempt == MAX_RETRIES) {
                    failureCounter.incrementAndGet();
                }
            }
        }
    }
}