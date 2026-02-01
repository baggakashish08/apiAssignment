# Technical Documentation

*Auto-generated documentation for this repository.*

**Last updated:** 2026-02-01 16:14

---

<!-- Documentation sections will be automatically added below -->

---

<!-- START:TestController.java -->
# Service Spec — TestController.java  

**Service name:** TestController.java  
**Last updated:** 2026-02-01 16:14

---  

# 1. Ownership  

| Role | Name | Responsibility |
|------|------|----------------|
| **Lead** | TBD | Tech direction, ownership, incidents |
| **Service owner** | TBD | Roadmap, SLAs, stakeholders |
| **Team members** | TBD | Development and maintenance |

---  

# 2. Logic & Purpose  

**What does this service do?**  
Provides a configurable test REST API with CRUD operations, health‑check, configuration exposure, utility endpoints (ping, info, calculation, random number) and optional metadata/debug information.

**Scope**  
- **In scope:**  
  - Handling HTTP requests under `/api/test`.  
  - Building JSON responses using `ResponseBuilder` and manual maps.  
  - Applying runtime configuration (enable/disable, response delay, mode, metadata, logging).  
  - Supplying auxiliary endpoints for health, config, ping, info, arithmetic calculations, and random number generation.  

**Features / Capabilities**  

| Feature | What it does | Exposed via |
|---------|--------------|-------------|
| CRUD endpoints | Basic create, read (list & by‑id), update, delete operations for a generic “test” resource. | HTTP API (`GET`, `POST`, `PUT`, `DELETE`) |
| Health check | Returns service status (`UP` or `DISABLED`) together with mode. | HTTP API (`GET /api/test/health`) |
| Config dump | Returns the current values of `TestApiConfig`. | HTTP API (`GET /api/test/config`) |
| Ping | Simple “pong” response to verify reachability. | HTTP API (`GET /api/test/ping`) |
| Info | Returns static application information (app name, Java version, OS). | HTTP API (`GET /api/test/info`) |
| Calculation | Performs basic arithmetic (`add`, `subtract`, `multiply`, `divide`) on two doubles. | HTTP API (`GET /api/test/calculate`) |
| Random number | Generates a random integer between supplied `min` and `max`. | HTTP API (`GET /api/test/random`) |
| Mode‑specific enrichment | Adds extra fields (`operation`, `maxItemsAllowed`, `debug`) based on `config.mode` (`simple`, `detailed`, `debug`). | Internal response building |
| Optional metadata | Adds timestamp, version, and mode block when `config.includeMetadata` is true. | Internal response building |
| Request logging | Logs method, path and body when `config.logRequests` is true. | Internal logging |
| Configurable delay | Sleeps for `config.responseDelayMs` milliseconds before responding. | Internal processing |

---  

# 3. Architecture — Resources, Framework, Data  

**Core Framework**  

| What | Version | Purpose |
|------|---------|---------|
| Language | Java | Primary implementation language |
| Framework | Spring Boot (Spring MVC) | REST controller, dependency injection, request mapping |
| Logging | SLF4J + Logback (via `LoggerFactory`) | Structured request logging |
| Utility | `ResponseBuilder` (custom) | Fluent builder for standardised JSON responses |

**API Endpoints** *(controller)*  

| Method | Endpoint | Description | Parameters |
|--------|----------|-------------|------------|
| GET | `/api/test` | Returns a generic success message; respects mode & metadata settings. | none |
| GET | `/api/test/{id}` | Returns a success payload for the supplied `id`. | `@PathVariable Integer id` |
| POST | `/api/test` | Echoes back the posted JSON body with a “created” status. | `@RequestBody Map<String,Object> requestBody` |
| PUT | `/api/test/{id}` | Echoes back the posted JSON body with an “updated” status for the supplied `id`. | `@PathVariable Integer id`, `@RequestBody Map<String,Object> requestBody` |
| DELETE | `/api/test/{id}` | Returns a “deleted” status for the supplied `id`. | `@PathVariable Integer id` |
| GET | `/api/test/health` | Health‑check endpoint – reports `UP` or `DISABLED`. | none |
| GET | `/api/test/config` | Dumps the current `TestApiConfig` values. | none |
| GET | `/api/test/ping` | Simple “pong” response. | none |
| GET | `/api/test/info` | Returns static application information. | none |
| GET | `/api/test/calculate` | Performs arithmetic based on query parameters. | `@RequestParam Double a`, `@RequestParam Double b`, `@RequestParam String operation` |
| GET | `/api/test/random` | Generates a random integer between `min` and `max`. | `@RequestParam(defaultValue="1") Integer min`, `@RequestParam(defaultValue="100") Integer max` |

**Dependencies**  

| Dependency | Purpose | How it's used |
|------------|---------|---------------|
| `TestApiConfig` (autowired) | Holds runtime configuration (enabled, mode, delay, etc.). | All request handlers read flags, decide whether to process, add mode‑specific data, apply delay, etc. |
| `ResponseBuilder` | Fluent builder for consistent JSON structures (success, error, pong). | Used in health, ping, info, calculate, random, and disabled‑response helpers. |
| `Logger` (`org.slf4j.Logger`) | Logging of incoming requests when enabled. | `logRequest` method writes INFO logs. |
| Spring MVC annotations (`@RestController`, `@RequestMapping`, `@GetMapping`, etc.) | Maps HTTP requests to Java methods. | Declares routes and HTTP verbs. |

---  

# 4. Key Components  

**Methods / Functions**  

| Method | Purpose | Parameters | Returns |
|--------|---------|------------|---------|
| `getTest()` | Handles `GET /api/test`; builds generic success response. | none | `ResponseEntity<Map<String,Object>>` (200 OK) |
| `getTestById(Integer id)` | Handles `GET /api/test/{id}`; returns payload with supplied id. | `@PathVariable Integer id` | `ResponseEntity<Map<String,Object>>` (200 OK) |
| `createTest(Map<String,Object> requestBody)` | Handles `POST /api/test`; echoes request body with created status. | `@RequestBody Map<String,Object> requestBody` | `ResponseEntity<Map<String,Object>>` (201 Created) |
| `updateTest(Integer id, Map<String,Object> requestBody)` | Handles `PUT /api/test/{id}`; echoes request body with updated status. | `@PathVariable Integer id`, `@RequestBody Map<String,Object> requestBody` | `ResponseEntity<Map<String,Object>>` (200 OK) |
| `deleteTest(Integer id)` | Handles `DELETE /api/test/{id}`; returns deleted status. | `@PathVariable Integer id` | `ResponseEntity<Map<String,Object>>` (200 OK) |
| `healthCheck()` | Returns service health (`UP`/`DISABLED`) plus mode and timestamp. | none | `ResponseEntity<Map<String,Object>>` (200 OK) |
| `getConfig()` | Exposes current configuration values. | none | `ResponseEntity<Map<String,Object>>` (200 OK) |
| `ping()` | Simple liveness endpoint returning a static pong payload. | none | `ResponseEntity<Map<String,Object>>` (200 OK) |
| `getInfo()` | Returns static application metadata (name, Java version, OS). | none | `ResponseEntity<Map<String,Object>>` (200 OK) |
| `calculate(Double a, Double b, String operation)` | Performs arithmetic based on `operation`; validates division by zero and unknown ops. | `@RequestParam Double a`, `@RequestParam Double b`, `@RequestParam String operation` | `ResponseEntity<Map<String,Object>>` (200 OK or 400 Bad Request) |
| `getRandomNumber(Integer min, Integer max)` | Generates a random integer in `[min, max]`; validates range. | `@RequestParam Integer min`, `@RequestParam Integer max` | `ResponseEntity<Map<String,Object>>` (200 OK or 400 Bad Request) |
| `addModeSpecificData(Map<String,Object> response, String operation)` *(private)* | Enriches response based on `config.mode` (`simple`, `detailed`, `debug`). | `response` map, `operation` description | void (mutates map) |
| `addMetadataIfEnabled(Map<String,Object> response)` *(private)* | Adds timestamp, version, mode block when `config.includeMetadata` is true. | `response` map | void |
| `buildDebugInfo()` *(private)* | Constructs a debug map with config flags, delay, thread name, free memory. | none | `Map<String,Object>` |
| `buildDisabledResponse()` *(private)* | Returns a 503 Service Unavailable payload when API is disabled. | none | `ResponseEntity<Map<String,Object>>` |
| `applyDelay()` *(private)* | Sleeps for `config.responseDelayMs` ms if > 0. | none | void |
| `logRequest(String method, String path, Object body)` *(private)* | Logs incoming request details when `config.logRequests` is true. | HTTP method, path, optional body | void |

**Configuration** *(from `TestApiConfig` – values are injected, defaults defined in that class)*  

| Property | Default | Description |
|----------|---------|-------------|
| `mode` | N/A | Determines response enrichment: `simple`, `detailed`, or `debug`. |
| `enabled` | N/A | Global switch – when `false` all CRUD endpoints return 503. |
| `includeMetadata` | N/A | When `true`, adds a `metadata` block (timestamp, version, mode) to responses. |
| `responseDelayMs` | N/A | Milliseconds to pause before sending a response (simulates latency). |
| `messagePrefix` | N/A | Prefix used in most success messages (e.g., `"TestAPI"`). |
| `maxItems` | N/A | Upper bound reported in detailed/debug modes (`maxItemsAllowed`). |
| `logRequests` | N/A | Enables request‑level INFO logging. |

---  

# 5. Key Flows  

- **Flow 1 – Standard request handling (CRUD & utility endpoints)**  
  1. **Log request** – `logRequest` is invoked if `config.logRequests` is true.  
  2. **Enable check** – If `config.enabled` is `false`, `buildDisabledResponse` returns a 503 error and the flow ends.  
  3. **Apply artificial delay** – `applyDelay` sleeps for `config.responseDelayMs` (if > 0).  
  4. **Build base response map** – Populate `status`, `message`, and any endpoint‑specific fields (`id`, `data`, etc.).  
  5. **Add mode‑specific data** – `addModeSpecificData` injects `operation`, `maxItemsAllowed`, and optionally a `debug` map based on `config.mode`.  
  6. **Add optional metadata** – `addMetadataIfEnabled` adds a `metadata` block when enabled.  
  7. **Return** – Wrap the map in a `ResponseEntity` with the appropriate HTTP status (`200`, `201`, etc.).  

- **Flow 2 – Calculation endpoint**  
  1. Receive query parameters `a`, `b`, `operation`.  
  2. Switch on `operation` (case‑insensitive) to compute the result.  
  3. If the operation is `divide` and `b == 0`, return **400 Bad Request** with an error payload via `ResponseBuilder.error()`.  
  4. If the operation string is not recognized, return **400 Bad Request** with a descriptive error.  
  5. On success, build a success payload containing operands, operation name, and result, then return **200 OK**.  

- **Flow 3 – Random number endpoint**  
  1. Parse optional `min` (default 1) and `max` (default 100).  
  2. Validate that `min < max`; otherwise return **400 Bad Request** with an error message.  
  3. Compute a random integer in the inclusive range.  
  4. Build a success response containing `min`, `max`, and the generated `result`.  
  5. Return **200 OK**.  

These flows illustrate how the controller centralises cross‑cutting concerns (logging, enable‑check, delay, mode‑specific enrichment, metadata) while delegating endpoint‑specific logic to the individual handler methods.  

*Last updated: 2026-02-01 16:14*
<!-- END:TestController.java -->
