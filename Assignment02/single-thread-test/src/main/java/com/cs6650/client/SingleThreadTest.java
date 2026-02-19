package com.cs6650.client;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.ProductApi;
import io.swagger.client.model.Product;

/**
 * Single-threaded baseline test establishes baseline performance by sending 10K POST requests
 * sequentially to the Product API
 *
 * Usage: java -jar single-thread-test.jar [server-url]
 */
public class SingleThreadTest {

    private static final int TOTAL_REQUESTS = 10000;
    private static final int MAX_RETRIES = 5;

    private final ProductApi productApi;
    private final ProductGenerator productGenerator;

    private int successfulRequests = 0;
    private int failedRequests = 0;

    public SingleThreadTest(String serverUrl) {
        ApiClient apiClient = new ApiClient();
        apiClient.updateBaseUri(serverUrl);

        this.productApi = new ProductApi(apiClient);
        this.productGenerator = new ProductGenerator();

        System.out.println("Testing connection to: " + serverUrl);
    }

    public void runTest() {
        System.out.println("=".repeat(60));
        System.out.println("CS6650 Assignment 2 - Single Thread Test");
        System.out.println("=".repeat(60));
        System.out.println("Total Requests: " + TOTAL_REQUESTS);
        System.out.println("Max Retries per Request: " + MAX_RETRIES);
        System.out.println("-".repeat(60));

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            sendRequestWithRetry();

            // Progress indicator
            if ((i + 1) % 1000 == 0) {
                System.out.println("Progress: " + (i + 1) + "/" + TOTAL_REQUESTS + " requests completed");
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        printResults(totalTime);
    }

    /**
     * Sends a single POST request with retry logic.
     * Retries up to MAX_RETRIES times to avoid 4xx or 5xx errors.
     */
    private void sendRequestWithRetry() {
        Product product = productGenerator.generateRandomProduct();

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                productApi.createProduct(product);
                successfulRequests++;
                return;
            } catch (ApiException e) {
                // Print first error for debugging
                if (failedRequests == 0 && attempt == MAX_RETRIES) {
                    System.err.println("API Error - Status: " + e.getCode() +
                            ", Message: " + e.getMessage() +
                            ", Body: " + e.getResponseBody());
                }

                int statusCode = e.getCode();

                if (statusCode >= 400 && attempt < MAX_RETRIES) {
                    continue;
                }

                if (attempt == MAX_RETRIES) {
                    failedRequests++;
                }
            } catch (Exception e) {
                // Print unexpected error
                if (failedRequests == 0 && attempt == MAX_RETRIES) {
                    System.err.println("Unexpected Error: " + e.getClass().getName() +
                            " - " + e.getMessage());
                }

                if (attempt == MAX_RETRIES) {
                    failedRequests++;
                }
            }
        }
    }

    /**
     * Prints test results
     */
    private void printResults(long totalTimeMs) {
        double totalTimeSeconds = totalTimeMs / 1000.0;
        double throughput = successfulRequests / totalTimeSeconds;

        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println("TEST RESULTS");
        System.out.println("=".repeat(60));
        System.out.println("Successful Requests: " + successfulRequests);
        System.out.println("Failed Requests:     " + failedRequests);
        System.out.println("Total Time:          " + totalTimeMs + " ms (" +
                String.format("%.2f", totalTimeSeconds) + " seconds)");
        System.out.println("Throughput:          " + String.format("%.2f", throughput) +
                " requests/second");
        System.out.println("=".repeat(60));

        System.out.println();
        System.out.println("MULTI-THREADED PERFORMANCE PREDICTION");
        System.out.println("-".repeat(60));
        System.out.println("Based on single-threaded throughput of " +
                String.format("%.2f", throughput) + " req/s:");
        System.out.println("  - 10 threads:  ~" + String.format("%.0f", throughput * 10) + " req/s");
        System.out.println("  - 50 threads:  ~" + String.format("%.0f", throughput * 50) + " req/s");
        System.out.println("  - 100 threads: ~" + String.format("%.0f", throughput * 100) + " req/s");
        System.out.println("  - 200 threads: ~" + String.format("%.0f", throughput * 200) + " req/s");
        System.out.println("=".repeat(60));
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar single-thread-test.jar <server-url>");
            System.exit(1);
        }

        String serverUrl = args[0];
        System.out.println("Server URL: " + serverUrl);

        SingleThreadTest test = new SingleThreadTest(serverUrl);
        test.runTest();
    }
}