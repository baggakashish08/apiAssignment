package com.springKT.apiAssignment.util;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseBuilder {

    private final Map<String, Object> response;

    public ResponseBuilder() {
        this.response = new LinkedHashMap<>();
    }

    public ResponseBuilder status(String status) {
        response.put("status", status);
        return this;
    }

    public ResponseBuilder message(String message) {
        response.put("message", message);
        return this;
    }

    public ResponseBuilder data(Object data) {
        response.put("data", data);
        return this;
    }

    public ResponseBuilder id(Object id) {
        response.put("id", id);
        return this;
    }

    public ResponseBuilder field(String key, Object value) {
        response.put(key, value);
        return this;
    }

    public ResponseBuilder timestamp() {
        response.put("timestamp", LocalDateTime.now().toString());
        return this;
    }

    public ResponseBuilder metadata(String mode, String version) {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("timestamp", LocalDateTime.now().toString());
        meta.put("version", version);
        meta.put("mode", mode);
        response.put("metadata", meta);
        return this;
    }

    public Map<String, Object> build() {
        return response;
    }

    // Static factory methods
    public static ResponseBuilder success() {
        return new ResponseBuilder().status("success");
    }

    public static ResponseBuilder created() {
        return new ResponseBuilder().status("created");
    }

    public static ResponseBuilder updated() {
        return new ResponseBuilder().status("updated");
    }

    public static ResponseBuilder deleted() {
        return new ResponseBuilder().status("deleted");
    }

    public static ResponseBuilder error() {
        return new ResponseBuilder().status("error");
    }

    public static ResponseBuilder pong() {
        return new ResponseBuilder().status("pong").timestamp();
    }
}
