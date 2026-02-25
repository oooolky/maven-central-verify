package com.cs6650.loadtest;

import java.util.Arrays;
import java.util.List;

/**
 * Calculate latency metrics after test completes
 */
public class LatencyStats {
    private final double mean;
    private final long median;
    private final long p99;
    private final long min;
    private final long max;

    public LatencyStats(List<RequestRecord> records) {
        if (records == null || records.isEmpty()) {
            this.mean = 0;
            this.median = 0;
            this.p99 = 0;
            this.min = 0;
            this.max = 0;
            return;
        }

        long[] values = records.stream().mapToLong(RequestRecord::latencyMs).toArray();
        Arrays.sort(values);

        this.min = values[0];
        this.max = values[values.length - 1];
        this.mean = calculateMean(values);
        this.median = percentile(values, 50);
        this.p99 = percentile(values, 99);
    }

    private double calculateMean(long[] values) {
        long sum = 0;
        for (long v : values) {
            sum += v;
        }
        return (double) sum / values.length;
    }

    private long percentile(long[] values, int p) {
        if (values.length == 0) {
            return 0;
        }
        int index = (int) Math.ceil((p / 100.0) * values.length) - 1;
        index = Math.max(0, Math.min(index, values.length - 1));
        return values[index];
    }

    public double mean() {
        return mean;
    }

    public long median() {
        return median;
    }

    public long p99() {
        return p99;
    }

    public long min() {
        return min;
    }

    public long max() {
        return max;
    }
}

