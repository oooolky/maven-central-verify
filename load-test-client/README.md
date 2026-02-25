# Checkout Load Test Client

## Build

```bash
mvn clean package
```

## Run

```bash
java -jar target/load-test-client-1.0-SNAPSHOT-jar-with-dependencies.jar <server-url> <num-threads> [output-csv] [total-requests]
```

Example

```bash
java -jar target/load-test-client-1.0-SNAPSHOT-jar-with-dependencies.jar http://localhost:8081 100 checkout_results.csv 200000
```

## Request body format

```json
{"shoppingCartId":12345,"creditCard":"1234-5678-9012-3456"}
```

Update field names in `CheckoutWorker.buildCheckoutBody()` if API names change

## Retry strategy

- Retry 5xx and connection exceptions up to 5 attempts
- Do not retry 402
- Record one CSV row per logical request
- Include retry time in the recorded latency

## CSV columns

- `start_time`
- `request_type`
- `latency_ms`
- `response_code`

## Throughput plot

```bash
python src/main/java/plot.py <csv_file> [output_png]
```
