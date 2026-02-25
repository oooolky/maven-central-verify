package com.cs6650.loadtest;

/**
 * Store one logical checkout request result
 */
public record RequestRecord(
        long startTime,
        long latencyMs,
        int responseCode
) {
    public static String csvHeader() {
        return "start_time,request_type,latency_ms,response_code";
    }

    public String toCsvLine() {
        return startTime + ",POST," + latencyMs + "," + responseCode;
    }
}
