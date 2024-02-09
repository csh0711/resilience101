# Resilience4j in Action

This repository contains examples for the [Resilience4j](https://resilience4j.readme.io/) library. 
The examples are written in [Kotlin](https://kotlinlang.org/) and 
use the [Spring Boot Annotations of Resilience4J](https://resilience4j.readme.io/docs/getting-started-3).

## Scenario
Two services are involved in the scenario, the **order-service** and the **transaction-log-service**:

<img src="scenario.png" alt="Scenario" width="500"/>

The **order-service** calls synchronously the **transaction-log-service** to log the created order 
and relies on its response. However, this call can fail.

## Switch to the different pattern implementations
For each implemented resilience pattern there's a separate Git branch to be checked out. 
The following branches are available:
- [**master**](https://github.com/csh0711/resilience101): 
  The initial state of the example, no resilience pattern is applied.
- [**retry**](https://github.com/csh0711/resilience101/tree/retry): 
  Shows how the retry pattern might be applied.
- [**rateLimiter**](https://github.com/csh0711/resilience101/tree/rateLimiter): 
  Shows how the rate limiter pattern might be applied.
- [**circuitBreaker**](https://github.com/csh0711/resilience101/tree/circuitBreaker): 
  Shows how the circuit breaker pattern might be applied.
- [**fallback**](https://github.com/csh0711/resilience101/tree/fallback):
  Shows how the fallback pattern might be applied.

## Run the example

### Start the applications
The following command will start the **order-service** on port `8081`:
```shell script
cd order-service &&./gradlew bootRun --parallel
```
And the **transaction-log-service** on port `8082`:
```shell script
cd transaction-log-service &&./gradlew bootRun --parallel
```

### Make the transaction-log-service fail
The **transaction-log-service** can be made to fail pseudo-randomly by setting the  `might-fail` 
property in the `application.yml` to `true`:
```yaml
transaction-log-service:
  feature-toggles:
    might-fail: true
```

### Execute requests
The following `curl` command will do a `POST` request to the `/orders`endpoint and pretty print the response's payload:

```shell script
curl --request POST 'http://localhost:8081/orders' \
--header 'Content-Type: application/json' \
--data-raw '
{
  "userId": "aef251cb-ee9f-4317-8f86-1d3c907e4b5f",
  "items": [
    {
      "itemId": "b5106ea0-9d02-42f5-a7c0-93a71a0d02c1",
      "name": "Product A",
      "quantity": 2
    }
  ]
}
' | json_pp
```

If you need a repeating `POST` request, e.g. every half second, you could wrap it in a `watch` command:
```shell script
watch -n 0.5 "curl --request POST 'http://localhost:8081/orders' \
--header 'Content-Type: application/json' \
--data-raw '
{
  \"userId\": \"aef251cb-ee9f-4317-8f86-1d3c907e4b5f\",
  \"items\": [
    {
      \"itemId\": \"b5106ea0-9d02-42f5-a7c0-93a71a0d02c1\",
      \"name\": \"Product A\",
      \"quantity\": 2
    }
  ]
}
' | json_pp"
```
(You might have to install `curl` and `watch`, e.g. on macOS with `brew install curl`and `brew install watch`.)

Alternatively you could use `ab` of [Apache Bench](https://httpd.apache.org/docs/2.4/programs/ab.html) to do 
a tiny load test. There's already a `post.json` file provided in the project's root directory.
```shell script
ab -T 'application/json' -n 20 -v 4 -p post.json http://localhost:8081/orders
```