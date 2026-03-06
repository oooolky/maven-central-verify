package com.cs6650.loadtest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Write request records to CSV
 */
public final class CsvWriter {
    private CsvWriter() {
    }

    public static void write(String path, List<RequestRecord> records) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            writer.println(RequestRecord.csvHeader());
            for (RequestRecord record : records) {
                writer.println(record.toCsvLine());
            }
        }
    }
}

