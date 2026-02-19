package com.cs6650.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Client Part 1 sends 200K POST requests to the Product API using multiple threads
 *
 * Usage: java -jar client-part1.jar <server-url> <num-threads>
 */
public class LoadTestClientPart1 {

    private static final int TOTAL_REQUESTS = 200000;

    private final String serverUrl;
    private final int numThreads;
    private final AtomicInteger successCounter = new AtomicInteger(0);
    private final AtomicInteger failureCounter = new AtomicInteger(0);

    public LoadTestClientPart1(String serverUrl, int numThreads) {
        this.serverUrl = serverUrl;
        this.numThreads = numThreads;
    }

    public void runTest() {
        System.out.println("=".repeat(60));
        System.out.println("CS6650 Assignment 2 - Client Part 1");
        System.out.println("Multi-Threaded Load Test");
        System.out.println("=".repeat(60));
        System.out.println("Server URL:     " + serverUrl);
        System.out.println("Total Requests: " + TOTAL_REQUESTS);
        System.out.println("Thread Count:   " + numThreads);
        System.out.println("-".repeat(60));

        // Calculate requests per thread
        int requestsPerThread = TOTAL_REQUESTS / numThreads;
        int remainingRequests = TOTAL_REQUESTS % numThreads;

        // Create thread pool and latch
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        System.out.println("Starting load test...");
        long startTime = System.currentTimeMillis();

        // Submit tasks to thread pool
        for (int i = 0; i < numThreads; i++) {
            // Distribute remaining requests to first few threads
            int requests = requestsPerThread + (i < remainingRequests ? 1 : 0);

            RequestTask task = new RequestTask(
                    serverUrl, requests, latch, successCounter, failureCounter
            );
            executor.submit(task);
        }

        // Wait for complete
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Test interrupted!");
        }

        long endTime = System.currentTimeMillis();
        executor.shutdown();

        printResults(endTime - startTime);
    }

    /**
     * Prints test results
     */
    private void printResults(long totalTimeMs) {
        int successful = successCounter.get();
        int failed = failureCounter.get();
        double totalTimeSeconds = totalTimeMs / 1000.0;
        double throughput = successful / totalTimeSeconds;

        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println("TEST RESULTS");
        System.out.println("=".repeat(60));
        System.out.println("Number of Threads:   " + numThreads);
        System.out.println("Total Requests:      " + TOTAL_REQUESTS);
        System.out.println("Successful Requests: " + successful);
        System.out.println("Failed Requests:     " + failed);
        System.out.println("Wall Time:           " + totalTimeMs + " ms (" +
                String.format("%.2f", totalTimeSeconds) + " seconds)");
        System.out.println("Throughput:          " + String.format("%.2f", throughput) +
                " requests/second");
        System.out.println("=".repeat(60));

        // Calculate expected vs actual performance
        double singleThreadThroughput = 76.76; // Hard coded from single-thread test
        double expectedThroughput = singleThreadThroughput * numThreads;
        double efficiency = (throughput / expectedThroughput) * 100;

        System.out.println();
        System.out.println("PERFORMANCE ANALYSIS");
        System.out.println("-".repeat(60));
        System.out.println("Single-thread baseline: " + singleThreadThroughput + " req/s");
        System.out.println("Expected throughput:    " + String.format("%.2f", expectedThroughput) + " req/s");
        System.out.println("Actual throughput:      " + String.format("%.2f", throughput) + " req/s");
        System.out.println("Scaling efficiency:     " + String.format("%.1f", efficiency) + "%");
        System.out.println("=".repeat(60));
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar client-part1.jar <server-url> <num-threads>");
            System.out.println("Example: java -jar client-part1.jar http://44.255.97.134:8080 100");
            System.exit(1);
        }

        String serverUrl = args[0];
        int numThreads = Integer.parseInt(args[1]);

        if (numThreads <= 0) {
            System.err.println("Error: Number of threads must be positive");
            System.exit(1);
        }

        LoadTestClientPart1 client = new LoadTestClientPart1(serverUrl, numThreads);
        client.runTest();
    }
}