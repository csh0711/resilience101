# Resilience4j in Action

This repository contains examples for the [Resilience4j](https://resilience4j.readme.io/) library. 
The examples are written in [Kotlin](https://kotlinlang.org/) and 
use the [Spring Boot Annotations of Resilience4J](https://resilience4j.readme.io/docs/getting-started-3).

## Scenario
Two services are involved in the scenario, the **order-service** and the **transaction-log-service**:

<img src="scenario.png" alt="Scenario" width="500"/>

The **order-service** calls synchronously the **transaction-log-service** to log the created order. This call can fail.

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

### Execute requests
The following command will do a `POST` request to the `/orders`endpoint every 0.5 seconds 
and pretty print the response's payload:
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
(You might have to install `curl` and `watch`, e.g. on macOS with `brew install curl`and `brew install watch`)
