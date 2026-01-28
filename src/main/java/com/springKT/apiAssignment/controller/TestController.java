package com.springKT.apiAssignment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/test")
public class TestController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTest() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Test API is working!");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTestById(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("id", id);
        response.put("message", "Retrieved test item with id: " + id);
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTest(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "created");
        response.put("message", "Test item created successfully");
        response.put("data", requestBody);
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTest(@PathVariable Integer id, @RequestBody Map<String, Object> requestBody) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "updated");
        response.put("id", id);
        response.put("message", "Test item updated successfully");
        response.put("data", requestBody);
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTest(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "deleted");
        response.put("id", id);
        response.put("message", "Test item deleted successfully");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "apiAssignment");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
}
