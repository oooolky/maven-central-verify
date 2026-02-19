package com.cs6650.client;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Client Part 2 extends Part 1 by recording per-request timing data and calculating
 * statistical metrics: mean, median, p99, min, and max response times.
 *
 * Usage: java -jar client-part2.jar <server-url> <num-threads> [output-csv]
 */
public class LoadTestClientPart2 {

    private static final int TOTAL_REQUESTS = 200000;

    private final String serverUrl;
    private final int numThreads;
    private final String outputCsvFile;
    private final AtomicInteger successCounter = new AtomicInteger(0);
    private final AtomicInteger failureCounter = new AtomicInteger(0);
    private final List<RequestTaskWithStats> tasks = new ArrayList<>();

    public LoadTestClientPart2(String serverUrl, int numThreads, String outputCsvFile) {
        this.serverUrl = serverUrl;
        this.numThreads = numThreads;
        this.outputCsvFile = outputCsvFile;
    }

    public void runTest() {
        System.out.println("=".repeat(60));
        System.out.println("CS6650 Assignment 2 - Client Part 2");
        System.out.println("Multi-Threaded Load Test with Latency Statistics");
        System.out.println("=".repeat(60));
        System.out.println("Server URL:     " + serverUrl);
        System.out.println("Total Requests: " + TOTAL_REQUESTS);
        System.out.println("Thread Count:   " + numThreads);
        System.out.println("Output CSV:     " + (outputCsvFile != null ? outputCsvFile : "none"));
        System.out.println("-".repeat(60));

        int requestsPerThread = TOTAL_REQUESTS / numThreads;
        int remainingRequests = TOTAL_REQUESTS % numThreads;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        System.out.println("Starting load test...");
        long startTime = System.currentTimeMillis();

        // Create and submit tasks
        for (int i = 0; i < numThreads; i++) {
            int requests = requestsPerThread + (i < remainingRequests ? 1 : 0);

            RequestTaskWithStats task = new RequestTaskWithStats(
                    serverUrl, requests, latch, successCounter, failureCounter
            );
            tasks.add(task);
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

        // Collect records
        List<RequestRecord> allRecords = new ArrayList<>();
        for (RequestTaskWithStats task : tasks) {
            allRecords.addAll(task.getRecords());
        }

        // Calculate statistics
        LatencyStats stats = new LatencyStats(allRecords);

        // Print test results
        printResults(endTime - startTime, stats);

        if (outputCsvFile != null) {
            writeCsvFile(allRecords);
        }
    }

    private void printResults(long totalTimeMs, LatencyStats stats) {
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

        System.out.println();
        System.out.println("LATENCY STATISTICS");
        System.out.println("-".repeat(60));
        System.out.println("Mean:   " + String.format("%.2f", stats.getMean()) + " ms");
        System.out.println("Median: " + stats.getMedian() + " ms");
        System.out.println("p99:    " + stats.getP99() + " ms");
        System.out.println("Min:    " + stats.getMin() + " ms");
        System.out.println("Max:    " + stats.getMax() + " ms");
        System.out.println("=".repeat(60));

        // Performance analysis
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

    private void writeCsvFile(List<RequestRecord> records) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputCsvFile))) {
            writer.println(RequestRecord.csvHeader());
            for (RequestRecord record : records) {
                writer.println(record.toCsvLine());
            }
            System.out.println();
            System.out.println("CSV file written to: " + outputCsvFile);
            System.out.println("Total records: " + records.size());
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar client-part2.jar <server-url> <num-threads> [output-csv]");
            System.exit(1);
        }

        String serverUrl = args[0];
        int numThreads = Integer.parseInt(args[1]);
        String outputCsv = args.length > 2 ? args[2] : "request_records.csv";

        if (numThreads <= 0) {
            System.err.println("Error: Number of threads must be positive");
            System.exit(1);
        }

        LoadTestClientPart2 client = new LoadTestClientPart2(serverUrl, numThreads, outputCsv);
        client.runTest();
    }
}