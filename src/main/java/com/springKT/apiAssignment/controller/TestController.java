package com.springKT.apiAssignment.controller;

import com.springKT.apiAssignment.config.TestApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/test")
public class TestController {

    @Autowired
    private TestApiConfig config;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTest() {
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
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", config.isEnabled() ? "UP" : "DISABLED");
        response.put("service", "apiAssignment");
        response.put("mode", config.getMode());
        response.put("timestamp", LocalDateTime.now().toString());
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
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "error");
        response.put("message", "Test API is currently disabled");
        response.put("timestamp", LocalDateTime.now().toString());
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
}
