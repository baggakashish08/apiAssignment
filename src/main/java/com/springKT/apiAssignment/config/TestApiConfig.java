package com.springKT.apiAssignment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "test.api")
public class TestApiConfig {

    /**
     * API mode: "simple", "detailed", or "debug"
     * - simple: Returns minimal response
     * - detailed: Returns full response with metadata
     * - debug: Returns response with debug information
     */
    private String mode = "simple";

    /**
     * Whether to include metadata (timestamps, version info) in responses
     */
    private boolean includeMetadata = true;

    /**
     * Response delay in milliseconds (for testing latency scenarios)
     */
    private long responseDelayMs = 0;

    /**
     * Whether the test API is enabled
     */
    private boolean enabled = true;

    /**
     * Custom greeting message prefix
     */
    private String messagePrefix = "Test API";

    /**
     * Maximum items to return in list responses
     */
    private int maxItems = 100;

    /**
     * Whether to log incoming requests
     */
    private boolean logRequests = false;

    // Getters and Setters
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isIncludeMetadata() {
        return includeMetadata;
    }

    public void setIncludeMetadata(boolean includeMetadata) {
        this.includeMetadata = includeMetadata;
    }

    public long getResponseDelayMs() {
        return responseDelayMs;
    }

    public void setResponseDelayMs(long responseDelayMs) {
        this.responseDelayMs = responseDelayMs;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getMessagePrefix() {
        return messagePrefix;
    }

    public void setMessagePrefix(String messagePrefix) {
        this.messagePrefix = messagePrefix;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }

    public boolean isLogRequests() {
        return logRequests;
    }

    public void setLogRequests(boolean logRequests) {
        this.logRequests = logRequests;
    }
}
