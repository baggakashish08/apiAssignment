# Technical Documentation

*Auto-generated documentation for this repository.*

**Last updated:** 2026-02-01 15:44

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
| **`getTest()`** | Handles `GET /api/test`. Returns a generic success message. | *none* | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`getTestById(Integer id)`** | Handles `GET /api/test/{id}`. Returns data for a specific ID. | `@PathVariable Integer id` | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`createTest(Map<String,Object> requestBody)`** | Handles `POST /api/test`. Echoes the posted payload and signals creation. | `@RequestBody Map<String,Object> requestBody` | `ResponseEntity<Map<String,Object>>` (201 Created) |
| **`updateTest(Integer id, Map<String,Object> requestBody)`** | Handles `PUT /api/test/{id}`. Returns updated data. | `@PathVariable Integer id`, `@RequestBody Map<String,Object> requestBody` | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`deleteTest(Integer id)`** | Handles `DELETE /api/test/{id}`. Confirms deletion. | `@PathVariable Integer id` | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`healthCheck()`** | Handles `GET /api/test/health`. Returns service health (`UP` or `DISABLED`). | *none* | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`getConfig()`** | Handles `GET /api/test/config`. Exposes the current `TestApiConfig` values. | *none* | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`ping()`** | Handles `GET /api/test/ping`. Simple “pong” response. | *none* | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`getInfo()`** | Handles `GET /api/test/info`. Returns static API information (app name, Java version, OS). | *none* | `ResponseEntity<Map<String,Object>>` (200 OK) |
| **`calculate(Double a, Double b, String operation)`** | Handles `GET /api/test/calculate`. Performs basic arithmetic based on `operation`. | `@RequestParam Double a`, `@RequestParam Double b`, `@RequestParam String operation` | `ResponseEntity<Map<String,Object>>` (200 OK or 400 Bad Request) |
| **`addModeSpecificData(Map<String,Object> response, String operation)`** *(private)* | Enriches the response according to the configured mode (`simple`, `detailed`, `debug`). | `response` map, description of the performed operation | *void* (modifies map) |
| **`addMetadataIfEnabled(Map<String,Object> response)`** *(private)* | Adds a `metadata` block (timestamp, version, mode) when `includeMetadata` is true. | `response` map | *void* |
| **`buildDebugInfo()`** *(private)* | Constructs a debug information map (config state, delay, thread, memory). | *none* | `Map<String,Object>` |
| **`buildDisabledResponse()`** *(private)* | Generates a standard error payload when the API is disabled. | *none* | `ResponseEntity<Map<String,Object>>` (503 Service Unavailable) |
| **`applyDelay()`** *(private)* | Sleeps the current thread for the configured `responseDelayMs`. | *none* | *void* |
| **`logRequest(String method, String path, Object body)`** *(private)* | Logs incoming requests when `logRequests` is enabled. | HTTP method, request path, optional body | *void* |

---

## API Endpoints

| HTTP Method | Path | Description |
|-------------|------|-------------|
| `GET` | `/api/test` | Simple health‑check style endpoint – returns a success message. |
| `GET` | `/api/test/{id}` | Retrieves a mock item by its numeric ID. |
| `POST` | `/api/test` | Creates a new mock item; echoes the submitted JSON payload. |
| `PUT` | `/api/test/{id}` | Updates a mock item identified by ID; returns the updated payload. |
| `DELETE` | `/api/test/{id}` | Deletes a mock item identified by ID. |
| `GET` | `/api/test/health` | Returns service health (`UP` / `DISABLED`). |
| `GET` | `/api/test/config` | Exposes the current configuration values. |
| `GET` | `/api/test/ping` | Minimal “pong” response for connectivity checks. |
| `GET` | `/api/test/info` | Provides static API metadata (app name, Java version, OS). |
| `GET` | `/api/test/calculate?a=…&b=…&operation=…` | Performs arithmetic (`add`, `subtract`, `multiply`, `divide`). |

*All endpoints respect the global configuration (`TestApiConfig`). If `enabled=false`, a **503 Service Unavailable** response is returned.*

---

## Usage Examples  

### 1. Simple `curl` calls  

```bash
# 1) GET all
curl -X GET http://localhost:8080/api/test

# 2) GET by ID
curl -X GET http://localhost:8080/api/test/42

# 3) POST (create)
curl -X POST http://localhost:8080/api/test \
     -H "Content-Type: application/json" \
     -d '{"name":"sample","value":123}'

# 4) PUT (update)
curl -X PUT http://localhost:8080/api/test/42 \
     -H "Content-Type: application/json" \
     -d '{"name":"updated","value":456}'

# 5) DELETE
curl -X DELETE http://localhost:8080/api/test/42

# 6) Health check
curl -X GET http://localhost:8080/api/test/health

# 7) Config dump
curl -X GET http://localhost:8080/api/test/config

# 8) Ping
curl -X GET http://localhost:8080/api/test/ping

# 9) Info
curl -X GET http://localhost:8080/api/test/info

# 10) Calculate (addition)
curl -G http://localhost:8080/api/test/calculate \
     --data-urlencode "a=10" \
     --data-urlencode "b=5" \
     --data-urlencode "operation=add"
```

### 2. Using Spring `RestTemplate` (Java)

```java
RestTemplate rest = new RestTemplate();
String base = "http://localhost:8080/api/test";

// GET all
ResponseEntity<Map> all = rest.getForEntity(base, Map.class);

// GET by ID
ResponseEntity<Map> byId = rest.getForEntity(base + "/{id}", Map.class, 42);

// POST (create)
Map<String,Object> payload = Map.of("name", "sample", "value", 123);
ResponseEntity<Map> created = rest.postForEntity(base, payload, Map.class);

// PUT (update)
HttpEntity<Map<String,Object>> request = new HttpEntity<>(payload);
ResponseEntity<Map> updated = rest.exchange(base + "/{id}", HttpMethod.PUT, request, Map.class, 42);

// DELETE
rest.delete(base + "/{id}", 42);

// Health
ResponseEntity<Map> health = rest.getForEntity(base + "/health", Map.class);

// Config
ResponseEntity<Map> config = rest.getForEntity(base + "/config", Map.class);

// Ping
ResponseEntity<Map> ping = rest.getForEntity(base + "/ping", Map.class);

// Info
ResponseEntity<Map> info = rest.getForEntity(base + "/info", Map.class);

// Calculate (multiply)
UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(base + "/calculate")
        .queryParam("a", 7)
        .queryParam("b", 3)
        .queryParam("operation", "multiply");

ResponseEntity<Map> calc = rest.getForEntity(builder.toUriString(), Map.class);
```

### 3. Expected JSON response (example for `GET /api/test` in **detailed** mode)

```json
{
  "status": "success",
  "message": "TestPrefix is working now!",
  "operation": "GET request processed",
  "maxItemsAllowed": 100,
  "metadata": {
    "timestamp": "2026-02-01T12:34:56.789",
    "version": "1.0.0",
    "mode": "detailed"
  }
}
```

*The exact fields depend on the current `mode` and `includeMetadata` configuration.*

--- 

*End of documentation.*

*Last updated: 2026-02-01 15:44*
<!-- END:TestController.java -->
