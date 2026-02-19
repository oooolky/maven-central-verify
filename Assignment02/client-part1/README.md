# CS6650 Assignment 2 - Client Part 1

## Overview

Multi-threaded load test client that sends 200,000 POST requests
to the Product Service endpoint using configurable number of threads.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- OpenAPI client library installed in local Maven repository

## Build
```bash
cd client-part1
mvn clean package
```

## Run
```bash
java -jar target/client-part1-1.0.0-jar-with-dependencies.jar <server-url> <num-threads>

# Example with 100 threads
java -jar target/client-part1-1.0.0-jar-with-dependencies.jar http://44.255.97.134:8080 100

# Example with 200 threads
java -jar target/client-part1-1.0.0-jar-with-dependencies.jar http://44.255.97.134:8080 200
```

## Output

- Number of threads used
- Total successful and failed requests
- Wall time (total execution time)
- Throughput (requests per second)
- Performance analysis comparing to single-threaded baseline

## Expected Results

- Throughput should scale approximately linearly with thread count
- Expected throughput ≈ numThreads × singleThreadThroughput (74.02 req/s)
- Scaling efficiency should be close to 100% for optimal performance