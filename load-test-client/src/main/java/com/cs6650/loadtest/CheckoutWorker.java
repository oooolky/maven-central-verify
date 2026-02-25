package com.cs6650.loadtest;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Send checkout requests until shared counter reaches total target
 */
public class CheckoutWorker implements Runnable {
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    private final HttpClient client;
    private final String baseUrl;
    private final int maxRetries;
    private final int totalRequests;
    private final AtomicInteger sharedRequestCounter;
    private final AtomicInteger successCounter;
    private final AtomicInteger declinedCounter;
    private final AtomicInteger clientErrorCounter;
    private final AtomicInteger failureCounter;
    private final CountDownLatch doneSignal;
    private final List<RequestRecord> records = new ArrayList<>();

    public CheckoutWorker(HttpClient client,
                          String baseUrl,
                          int maxRetries,
                          int totalRequests,
                          AtomicInteger sharedRequestCounter,
                          AtomicInteger successCounter,
                          AtomicInteger declinedCounter,
                          AtomicInteger clientErrorCounter,
                          AtomicInteger failureCounter,
                          CountDownLatch doneSignal) {
        this.client = client;
        this.baseUrl = baseUrl;
        this.maxRetries = maxRetries;
        this.totalRequests = totalRequests;
        this.sharedRequestCounter = sharedRequestCounter;
        this.successCounter = successCounter;
        this.declinedCounter = declinedCounter;
        this.clientErrorCounter = clientErrorCounter;
        this.failureCounter = failureCounter;
        this.doneSignal = doneSignal;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int index = sharedRequestCounter.getAndIncrement();
                if (index >= totalRequests) {
                    return;
                }
                executeOneLogicalRequest();
            }
        } finally {
            doneSignal.countDown();
        }
    }

    private void executeOneLogicalRequest() {
        long logicalStart = System.currentTimeMillis();
        int finalStatusCode = 0;
        long shoppingCartId = ThreadLocalRandom.current().nextLong(1, 1_000_001);

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(buildCheckoutUri(shoppingCartId))
                    .timeout(REQUEST_TIMEOUT)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(buildCheckoutBody()))
                    .build();

            try {
                HttpResponse<String> response = client.send(
                        request, HttpResponse.BodyHandlers.ofString());
                finalStatusCode = response.statusCode();

                if (finalStatusCode >= 200 && finalStatusCode < 300) {
                    successCounter.incrementAndGet();
                    break;
                }

                if (finalStatusCode == 402) {
                    declinedCounter.incrementAndGet();
                    break;
                }

                if (finalStatusCode >= 500 && attempt < maxRetries) {
                    continue;
                }

                if (finalStatusCode >= 400 && finalStatusCode < 500) {
                    clientErrorCounter.incrementAndGet();
                } else {
                    failureCounter.incrementAndGet();
                }
                break;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                failureCounter.incrementAndGet();
                break;
            } catch (IOException e) {
                if (attempt >= maxRetries) {
                    failureCounter.incrementAndGet();
                }
            }
        }

        long latency = System.currentTimeMillis() - logicalStart;
        records.add(new RequestRecord(logicalStart, latency, finalStatusCode));
    }

    /**
     * Keep field names in one place
     * Update this method if teammate endpoint names change
     */
    private String buildCheckoutBody() {
        String creditCard = randomCardNumber();
        return "{\"credit_card_number\":\"" + creditCard + "\"}";
    }

    private java.net.URI buildCheckoutUri(long shoppingCartId) {
        return java.net.URI.create(baseUrl + "/shopping-carts/" + shoppingCartId + "/checkout");
    }

    private String randomCardNumber() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        return fourDigits(rnd) + "-" + fourDigits(rnd) + "-" + fourDigits(rnd) + "-" + fourDigits(rnd);
    }

    private String fourDigits(ThreadLocalRandom rnd) {
        int value = rnd.nextInt(0, 10_000);
        return String.format("%04d", value);
    }

    public List<RequestRecord> getRecords() {
        return records;
    }
}
