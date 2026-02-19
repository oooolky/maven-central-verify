package com.cs6650.client;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class to calculate required statistical metrics from collected data
 */
public class LatencyStats {

    private final long[] latencies;
    private final double mean;
    private final long median;
    private final long p99;
    private final long min;
    private final long max;

    /**
     * Calculates statistics from a list of request records.
     *
     * @param records list of RequestRecord objects containing latency data
     */
    public LatencyStats(List<RequestRecord> records) {
        if (records == null || records.isEmpty()) {
            this.latencies = new long[0];
            this.mean = 0;
            this.median = 0;
            this.p99 = 0;
            this.min = 0;
            this.max = 0;
            return;
        }

        // Extract latencies into array for efficient processing
        this.latencies = records.stream()
                .mapToLong(RequestRecord::latency)
                .toArray();

        // Sort for percentile calculations
        long[] sorted = latencies.clone();
        Arrays.sort(sorted);

        this.min = sorted[0];
        this.max = sorted[sorted.length - 1];
        this.mean = calculateMean(sorted);
        this.median = calculatePercentile(sorted, 50);
        this.p99 = calculatePercentile(sorted, 99);
    }

    private double calculateMean(long[] sorted) {
        long sum = 0;
        for (long latency : sorted) {
            sum += latency;
        }
        return (double) sum / sorted.length;
    }

    private long calculatePercentile(long[] sorted, int percentile) {
        if (sorted.length == 0) return 0;
        int index = (int) Math.ceil((percentile / 100.0) * sorted.length) - 1;
        index = Math.max(0, Math.min(index, sorted.length - 1));
        return sorted[index];
    }

    public double getMean() {
        return mean;
    }

    public long getMedian() {
        return median;
    }

    public long getP99() {
        return p99;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    @Override
    public String toString() {
        return String.format(
                "Mean: %.2f ms | Median: %d ms | p99: %d ms | Min: %d ms | Max: %d ms",
                mean, median, p99, min, max
        );
    }
}