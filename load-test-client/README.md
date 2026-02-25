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

## Simplified Run Command

```bash
java -jar target/load-test-client-1.0-SNAPSHOT-jar-with-dependencies.jar \
  http://localhost:8081 100 results_200k.csv 200000
```

```bash
wc -l results_200k.csv
```

```bash
awk -F, 'NR>1{c[$4]++} END{for(k in c) print k, c[k]}' 
```
## Request body format

```json
{"credit_card_number":"1234-5678-9012-3456"}
```

Request path format

`/shopping-carts/{shoppingCartId}/checkout`

Update path or field names in `CheckoutWorker.buildCheckoutUri()` and `CheckoutWorker.buildCheckoutBody()` if API names change

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
python src/main/java/plot.py <csv_file> [output_png] [thread_count]
```
