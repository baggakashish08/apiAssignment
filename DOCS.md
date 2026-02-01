# Technical Documentation

*Auto-generated documentation for this repository.*

**Last updated:** 2026-02-01 15:45

---

<!-- Documentation sections will be automatically added below -->

---

<!-- START:TestController.java -->
# TestController.java

## Overview
`TestController` is a Spring Boot REST controller that provides a set of demo API endpoints under the base path **`/api/test`**.  
It demonstrates typical CRUD operations, health‑check, configuration exposure, utility endpoints (ping, info, calculate) and includes configurable behavior such as:

* Enabling/disabling the API (`TestApiConfig.enabled`)
* Optional response delay
* Conditional logging of incoming requests
* Mode‑specific response enrichment (`simple`, `detailed`, `debug`)
* Optional metadata injection

All responses are built with the helper class `ResponseBuilder` and returned as `Map<String,Object>` JSON payloads.

---

## Key Components  

| Method | Purpose | Parameters | Returns |
|--------|---------|------------|---------|
| **`getTest()`** | Handles **GET** `/api/test`. Returns a generic success message. | *none* | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`getTestById(Integer id)`** | Handles **GET** `/api/test/{id}`. Returns data for a specific ID. | `@PathVariable Integer id` | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`createTest(Map<String,Object> requestBody)`** | Handles **POST** `/api/test`. Echoes the posted payload and signals creation. | `@RequestBody Map<String,Object> requestBody` | `ResponseEntity<Map<String,Object>>` (201 Created) |
| **`updateTest(Integer id, Map<String,Object> requestBody)`** | Handles **PUT** `/api/test/{id}`. Returns updated data. | `@PathVariable Integer id`, `@RequestBody Map<String,Object> requestBody` | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`deleteTest(Integer id)`** | Handles **DELETE** `/api/test/{id}`. Confirms deletion. | `@PathVariable Integer id` | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`healthCheck()`** | Handles **GET** `/api/test/health`. Returns service health (`UP` or `DISABLED`). | *none* | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`getConfig()`** | Handles **GET** `/api/test/config`. Exposes the current `TestApiConfig` values. | *none* | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`ping()`** | Handles **GET** `/api/test/ping`. Simple “pong” response. | *none* | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`getInfo()`** | Handles **GET** `/api/test/info`. Returns static API information (app name, Java version, OS). | *none* | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`calculate(Double a, Double b, String operation)`** | Handles **GET** `/api/test/calculate`. Performs basic arithmetic based on `operation` query param. | `@RequestParam Double a`, `@RequestParam Double b`, `@RequestParam String operation` | `ResponseEntity<Map<String,Object>>` (200 OK or 400 Bad Request) |
| **`addModeSpecificData(Map<String,Object> response, String operation)`** *(private)* | Enriches the response according to the configured mode (`simple`, `detailed`, `debug`). | `response` map, description of performed operation | *void* (mutates `response`) |
| **`addMetadataIfEnabled(Map<String,Object> response)`** *(private)* | Adds a `metadata` block (timestamp, version, mode) when `includeMetadata` is true. | `response` map | *void* |
| **`buildDebugInfo()`** *(private)* | Creates a debug map containing config flags, delay, thread name, free memory, etc. | *none* | `Map<String,Object>` |
| **`buildDisabledResponse()`** *(private)* | Generates a standard error payload when the API is disabled. | *none* | `ResponseEntity<Map<String,Object>>` (503 SERVICE_UNAVAILABLE) |
| **`applyDelay()`** *(private)* | Sleeps the current thread for `responseDelayMs` if configured. | *none* | *void* |
| **`logRequest(String method, String path, Object body)`** *(private)* | Logs incoming requests when `logRequests` is enabled. | HTTP method, request path, optional body | *void* |

---

## API Endpoints  

| HTTP Method | Path | Description |
|-------------|------|-------------|
| **GET** | `/api/test` | Returns a generic success message. |
| **GET** | `/api/test/{id}` | Retrieves a mock item by its numeric ID. |
| **POST** | `/api/test` | Creates a new mock item; echoes the supplied JSON body. |
| **PUT** | `/api/test/{id}` | Updates a mock item identified by ID; echoes the supplied JSON body. |
| **DELETE** | `/api/test/{id}` | Deletes a mock item identified by ID. |
| **GET** | `/api/test/health` | Health‑check endpoint (`UP` / `DISABLED`). |
| **GET** | `/api/test/config` | Returns the current configuration values. |
| **GET** | `/api/test/ping` | Simple “pong” response for liveness checks. |
| **GET** | `/api/test/info` | Provides static API metadata (app name, Java version, OS). |
| **GET** | `/api/test/calculate?a=…&b=…&operation=add|subtract|multiply|divide` | Performs the requested arithmetic operation. |

*All endpoints respect the global `TestApiConfig` settings (enabled flag, delay, logging, mode, metadata inclusion).*

---

## Usage Examples  

### 1. Calling an endpoint with **cURL**

```bash
# Simple GET
curl -X GET http://localhost:8080/api/test

# GET by ID
curl -X GET http://localhost:8080/api/test/42

# POST (create)
curl -X POST http://localhost:8080/api/test \
     -H "Content-Type: application/json" \
     -d '{"name":"Sample","value":123}'

# PUT (update)
curl -X PUT http://localhost:8080/api/test/42 \
     -H "Content-Type: application/json" \
     -d '{"name":"Updated","value":456}'

# DELETE
curl -X DELETE http://localhost:8080/api/test/42

# Health check
curl http://localhost:8080/api/test/health

# Configuration dump
curl http://localhost:8080/api/test/config

# Ping
curl http://localhost:8080/api/test/ping

# Info
curl http://localhost:8080/api/test/info

# Calculate (addition)
curl "http://localhost:8080/api/test/calculate?a=10&b=5&operation=add"
```

### 2. Using **Spring `RestTemplate`** (Java)

```java
RestTemplate restTemplate = new RestTemplate();
String baseUrl = "http://localhost:8080/api/test";

// GET
ResponseEntity<Map> getResp = restTemplate.getForEntity(baseUrl, Map.class);
System.out.println(getResp.getBody());

// POST
Map<String,Object> payload = Map.of("name", "Demo", "value", 99);
ResponseEntity<Map> postResp = restTemplate.postForEntity(baseUrl, payload, Map.class);
System.out.println(postResp.getBody());

// PUT
HttpEntity<Map<String,Object>> putEntity = new HttpEntity<>(payload);
ResponseEntity<Map> putResp = restTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, putEntity, Map.class);
System.out.println(putResp.getBody());

// DELETE
restTemplate.delete(baseUrl + "/1");

// Calculate (division)
String calcUrl = baseUrl + "/calculate?a=20&b=4&operation=divide";
ResponseEntity<Map> calcResp = restTemplate.getForEntity(calcUrl, Map.class);
System.out.println(calcResp.getBody());
```

### 3. Using **WebClient** (reactive)

```java
WebClient client = WebClient.create("http://localhost:8080");

// GET
client.get()
      .uri("/api/test")
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
      .subscribe(System.out::println);

// POST
Map<String,Object> body = Map.of("title", "New", "count", 10);
client.post()
      .uri("/api/test")
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(body)
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
      .subscribe(System.out::println);

// Calculate (multiply)
client.get()
      .uri(uriBuilder -> uriBuilder.path("/api/test/calculate")
                                   .queryParam("a", 7)
                                   .queryParam("b", 6)
                                   .queryParam("operation", "multiply")
                                   .build())
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
      .subscribe(System.out::println);
```

---

*The controller’s behavior can be altered at runtime by adjusting the `TestApiConfig` bean (e.g., via `application.yml` or a dedicated configuration endpoint). The provided examples assume the service runs on `localhost:8080`.*

*Last updated: 2026-02-01 15:45*
<!-- END:TestController.java -->
