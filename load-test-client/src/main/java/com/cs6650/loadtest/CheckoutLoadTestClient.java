package com.cs6650.loadtest;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Run checkout load test with shared work counter
 */
public class CheckoutLoadTestClient {
    private static final int DEFAULT_TOTAL_REQUESTS = 200_000;
    private static final int MAX_RETRIES = 5;
    private static final String DEFAULT_OUTPUT_CSV = "checkout_results.csv";

    private final URI checkoutUri;
    private final int numThreads;
    private final int totalRequests;
    private final String outputCsv;
    private final AtomicInteger nextRequestIndex = new AtomicInteger(0);
    private final AtomicInteger successCounter = new AtomicInteger(0);
    private final AtomicInteger declinedCounter = new AtomicInteger(0);
    private final AtomicInteger failureCounter = new AtomicInteger(0);
    private final List<CheckoutWorker> workers = new ArrayList<>();

    public CheckoutLoadTestClient(String baseUrl, int numThreads, int totalRequests, String outputCsv) {
        this.checkoutUri = URI.create(normalizeBaseUrl(baseUrl) + "/shopping-cart/checkout");
        this.numThreads = numThreads;
        this.totalRequests = totalRequests;
        this.outputCsv = outputCsv;
    }

    public void run() {
        printHeader();

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch doneSignal = new CountDownLatch(numThreads);

        long start = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            CheckoutWorker worker = new CheckoutWorker(
                    client,
                    checkoutUri,
                    MAX_RETRIES,
                    totalRequests,
                    nextRequestIndex,
                    successCounter,
                    declinedCounter,
                    failureCounter,
                    doneSignal
            );
            workers.add(worker);
            executor.submit(worker);
        }

        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Load test interrupted");
        } finally {
            executor.shutdown();
        }

        long end = System.currentTimeMillis();
        List<RequestRecord> records = collectRecords();
        LatencyStats stats = new LatencyStats(records);

        printSummary(end - start, stats);
        writeCsv(records);
    }

    private void printHeader() {
        System.out.println("============================================================");
        System.out.println("CS6650 Assignment 3 Checkout Load Test");
        System.out.println("============================================================");
        System.out.println("Target endpoint: " + checkoutUri);
        System.out.println("Total requests:  " + totalRequests);
        System.out.println("Thread count:    " + numThreads);
        System.out.println("CSV output:      " + outputCsv);
        System.out.println("============================================================");
    }

    private List<RequestRecord> collectRecords() {
        List<RequestRecord> all = new ArrayList<>(totalRequests);
        for (CheckoutWorker worker : workers) {
            all.addAll(worker.getRecords());
        }
        return all;
    }

    private void printSummary(long totalTimeMs, LatencyStats stats) {
        int success = successCounter.get();
        int declined = declinedCounter.get();
        int failed = failureCounter.get();
        int unsuccessful = declined + failed;
        int completed = success + unsuccessful;

        double totalSeconds = totalTimeMs / 1000.0;
        double throughput = completed / totalSeconds;

        System.out.println();
        System.out.println("RESULTS");
        System.out.println("============================================================");
        System.out.println("Successful requests (2xx): " + success);
        System.out.println("Declined requests (402):   " + declined);
        System.out.println("Failed requests:           " + failed);
        System.out.println("Unsuccessful requests:     " + unsuccessful);
        System.out.println("Completed requests:        " + completed);
        System.out.println("Wall time:                 " + totalTimeMs + " ms (" + String.format("%.2f", totalSeconds) + " s)");
        System.out.println("Throughput:                " + String.format("%.2f", throughput) + " req/s");
        System.out.println("============================================================");

        System.out.println();
        System.out.println("LATENCY");
        System.out.println("============================================================");
        System.out.println("Mean:   " + String.format("%.2f", stats.mean()) + " ms");
        System.out.println("Median: " + stats.median() + " ms");
        System.out.println("P99:    " + stats.p99() + " ms");
        System.out.println("Min:    " + stats.min() + " ms");
        System.out.println("Max:    " + stats.max() + " ms");
        System.out.println("============================================================");
    }

    private void writeCsv(List<RequestRecord> records) {
        try {
            CsvWriter.write(outputCsv, records);
            System.out.println("CSV written: " + outputCsv);
            System.out.println("CSV rows:    " + records.size());
        } catch (Exception e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
        }
    }

    private static String normalizeBaseUrl(String baseUrl) {
        if (baseUrl.endsWith("/")) {
            return baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar load-test-client.jar <server-url> <num-threads> [output-csv] [total-requests]");
            System.exit(1);
        }

        String baseUrl = args[0];
        int threads = Integer.parseInt(args[1]);
        String outputCsv = args.length > 2 ? args[2] : DEFAULT_OUTPUT_CSV;
        int totalRequests = args.length > 3 ? Integer.parseInt(args[3]) : DEFAULT_TOTAL_REQUESTS;

        if (threads <= 0 || totalRequests <= 0) {
            System.err.println("Error: num-threads and total-requests must be positive");
            System.exit(1);
        }

        CheckoutLoadTestClient client = new CheckoutLoadTestClient(baseUrl, threads, totalRequests, outputCsv);
        client.run();
    }
}

