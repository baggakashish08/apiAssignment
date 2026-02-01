package com.springKT.apiAssignment.controller;

import com.springKT.apiAssignment.config.TestApiConfig;
import com.springKT.apiAssignment.util.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestApiConfig config;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTest() {
        logRequest("GET", "/api/test", null);
        
        if (!config.isEnabled()) {
            return buildDisabledResponse();
        }
        
        applyDelay();
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", config.getMessagePrefix() + " is working now!");
        
        addModeSpecificData(response, "GET request processed");
        addMetadataIfEnabled(response);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTestById(@PathVariable Integer id) {
        logRequest("GET", "/api/test/" + id, null);
        
        if (!config.isEnabled()) {
            return buildDisabledResponse();
        }
        
        applyDelay();
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("id", id);
        response.put("message", config.getMessagePrefix() + ": Retrieved item with id: " + id);
        
        addModeSpecificData(response, "GET by ID request processed");
        addMetadataIfEnabled(response);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTest(@RequestBody Map<String, Object> requestBody) {
        logRequest("POST", "/api/test", requestBody);
        
        if (!config.isEnabled()) {
            return buildDisabledResponse();
        }
        
        applyDelay();
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "created");
        response.put("message", config.getMessagePrefix() + ": Item created successfullyyy");
        response.put("data", requestBody);
        
        addModeSpecificData(response, "POST request processed");
        addMetadataIfEnabled(response);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTest(@PathVariable Integer id, @RequestBody Map<String, Object> requestBody) {
        logRequest("PUT", "/api/test/" + id, requestBody);
        
        if (!config.isEnabled()) {
            return buildDisabledResponse();
        }
        
        applyDelay();
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "updated");
        response.put("id", id);
        response.put("message", config.getMessagePrefix() + ": Item updated successfully");
        response.put("data", requestBody);
        
        addModeSpecificData(response, "PUT request processed");
        addMetadataIfEnabled(response);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTest(@PathVariable Integer id) {
        logRequest("DELETE", "/api/test/" + id, null);
        
        if (!config.isEnabled()) {
            return buildDisabledResponse();
        }
        
        applyDelay();
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "deleted");
        response.put("id", id);
        response.put("message", config.getMessagePrefix() + ": Item deleted successfully");
        
        addModeSpecificData(response, "DELETE request processed");
        addMetadataIfEnabled(response);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new ResponseBuilder()
                .status(config.isEnabled() ? "UP" : "DISABLED")
                .field("service", "apiAssignment")
                .field("mode", config.getMode())
                .timestamp()
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getConfig() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("mode", config.getMode());
        response.put("enabled", config.isEnabled());
        response.put("includeMetadata", config.isIncludeMetadata());
        response.put("responseDelayMs", config.getResponseDelayMs());
        response.put("messagePrefix", config.getMessagePrefix());
        response.put("maxItems", config.getMaxItems());
        response.put("logRequests", config.isLogRequests());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        return ResponseEntity.ok(ResponseBuilder.pong().build());
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> response = ResponseBuilder.success()
                .message("API Information")
                .field("appName", "apiAssignment")
                .field("javaVersion", System.getProperty("java.version"))
                .field("osName", System.getProperty("os.name"))
                .timestamp()
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculate(
            @RequestParam Double a,
            @RequestParam Double b,
            @RequestParam String operation) {
        
        Double result;
        String operationName;
        
        switch (operation.toLowerCase()) {
            case "add":
                result = a + b;
                operationName = "addition";
                break;
            case "subtract":
                result = a - b;
                operationName = "subtraction";
                break;
            case "multiply":
                result = a * b;
                operationName = "multiplication";
                break;
            case "divide":
                if (b == 0) {
                    return ResponseEntity.badRequest().body(
                            ResponseBuilder.error()
                                    .message("Cannot divide by zero")
                                    .timestamp()
                                    .build()
                    );
                }
                result = a / b;
                operationName = "division";
                break;
            default:
                return ResponseEntity.badRequest().body(
                        ResponseBuilder.error()
                                .message("Invalid operation. Use: add, subtract, multiply, divide")
                                .timestamp()
                                .build()
                );
        }
        
        Map<String, Object> response = ResponseBuilder.success()
                .message("Calculation completed")
                .field("operandA", a)
                .field("operandB", b)
                .field("operation", operationName)
                .field("result", result)
                .timestamp()
                .build();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/random")
    public ResponseEntity<Map<String, Object>> getRandomNumber(
            @RequestParam(defaultValue = "1") Integer min,
            @RequestParam(defaultValue = "100") Integer max) {
        
        if (min >= max) {
            return ResponseEntity.badRequest().body(
                    ResponseBuilder.error()
                            .message("Min must be less than max")
                            .timestamp()
                            .build()
            );
        }
        
        int randomNumber = min + (int) (Math.random() * (max - min + 1));
        
        Map<String, Object> response = ResponseBuilder.success()
                .message("Random number generated")
                .field("min", min)
                .field("max", max)
                .field("result", randomNumber)
                .timestamp()
                .build();
        
        return ResponseEntity.ok(response);
    }

    // ============ Helper Methods ============

    private void addModeSpecificData(Map<String, Object> response, String operation) {
        switch (config.getMode().toLowerCase()) {
            case "detailed":
                response.put("operation", operation);
                response.put("maxItemsAllowed", config.getMaxItems());
                break;
            case "debug":
                response.put("operation", operation);
                response.put("maxItemsAllowed", config.getMaxItems());
                response.put("debug", buildDebugInfo());
                break;
            case "simple":
            default:
                // Simple mode - no extra data added
                break;
        }
    }

    private void addMetadataIfEnabled(Map<String, Object> response) {
        if (config.isIncludeMetadata()) {
            Map<String, Object> metadata = new LinkedHashMap<>();
            metadata.put("timestamp", LocalDateTime.now().toString());
            metadata.put("version", "1.0.0");
            metadata.put("mode", config.getMode());
            response.put("metadata", metadata);
        }
    }

    private Map<String, Object> buildDebugInfo() {
        Map<String, Object> debug = new LinkedHashMap<>();
        debug.put("configEnabled", config.isEnabled());
        debug.put("delayApplied", config.getResponseDelayMs() + "ms");
        debug.put("metadataEnabled", config.isIncludeMetadata());
        debug.put("threadName", Thread.currentThread().getName());
        debug.put("freeMemory", Runtime.getRuntime().freeMemory() / 1024 / 1024 + " MB");
        return debug;
    }

    private ResponseEntity<Map<String, Object>> buildDisabledResponse() {
        Map<String, Object> response = ResponseBuilder.error()
                .message("Test API is currently disabled")
                .timestamp()
                .build();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    private void applyDelay() {
        if (config.getResponseDelayMs() > 0) {
            try {
                Thread.sleep(config.getResponseDelayMs());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void logRequest(String method, String path, Object body) {
        if (config.isLogRequests()) {
            logger.info("Request: {} {} | Body: {}", method, path, body);
        }
    }
}
