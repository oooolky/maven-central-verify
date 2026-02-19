package com.cs6650.client;

/**
 * Immutable record class storing per-request data such as
 * start time, request type, latency, and response code.
 */
public record RequestRecord(
        long startTime,
        String requestType,
        long latency,
        int responseCode
) {
    /**
     * Converts the record to CSV
     *
     * @return comma-separated string representation
     */
    public String toCsvLine() {
        return startTime + "," + requestType + "," + latency + "," + responseCode;
    }

    /**
     * Returns CSV header line
     */
    public static String csvHeader() {
        return "start_time,request_type,latency_ms,response_code";
    }
}