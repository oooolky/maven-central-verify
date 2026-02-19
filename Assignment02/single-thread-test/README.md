# CS6650 Assignment 2 - Single Thread Test

## Overview

Single-threaded baseline test client that sends 10,000 POST requests
to the Product Service endpoint to establish baseline throughput.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- OpenAPI client library installed in local Maven repository

## Build
```bash
cd single-thread-test
mvn clean package
```

## Run
```bash
java -jar target/single-thread-test-1.0.0-jar-with-dependencies.jar <server-url>

# Example with AWS ECS endpoint
java -jar target/single-thread-test-1.0.0-jar-with-dependencies.jar http://44.255.97.134:8080

# Example with local server
java -jar target/single-thread-test-1.0.0-jar-with-dependencies.jar http://localhost:8080
```

## Output

- Progress updates every 1,000 requests
- Total successful and failed requests
- Total execution time
- Throughput (requests per second)
- Predicted multi-threaded performance