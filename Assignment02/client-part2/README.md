# CS6650 Assignment 2 - Client Part 2

## Overview

Multi-threaded load test client with latency statistics collection.
Extends Part 1 by recording per-request timing data and calculating
mean, median, p99, min, and max response times.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- OpenAPI client library installed in local Maven repository

## Build
```bash
cd client-part2
mvn clean package
```

## Run
```bash
java -jar target/client-part2-1.0.0-jar-with-dependencies.jar <server-url> <num-threads> [output-csv]

# Example with 200 threads, output to results.csv
java -jar target/client-part2-1.0.0-jar-with-dependencies.jar http://44.255.97.134:8080 200 results.csv
```

## Output

Console output includes:
- Number of threads, successful/failed requests
- Wall time and throughput
- Latency statistics: mean, median, p99, min, max
- Performance analysis comparing to baseline

CSV file contains:
- start_time: Unix timestamp when request started
- request_type: HTTP method (POST)
- latency_ms: Request latency in milliseconds
- response_code: HTTP response code

## Generating Throughput Plot

Use the CSV file to create a throughput-over-time plot:
```python
import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv('results.csv')
df['second'] = (df['start_time'] - df['start_time'].min()) // 1000
throughput = df.groupby('second').size()
plt.plot(throughput.index, throughput.values)
plt.xlabel('Time (seconds)')
plt.ylabel('Throughput (requests/second)')
plt.title('Throughput Over Time')
plt.savefig('throughput_plot.png')
```