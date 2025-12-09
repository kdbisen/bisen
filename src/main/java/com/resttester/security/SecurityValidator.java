/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.security;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class SecurityValidator {

    // Maximum sizes
    private static final int MAX_URL_LENGTH = 2048;
    private static final int MAX_HEADER_SIZE = 8192; // 8KB
    private static final int MAX_BODY_SIZE = 10 * 1024 * 1024; // 10MB
    private static final int MAX_HEADER_COUNT = 50;

    // Allowed localhost hosts for local testing
    private static final Set<String> ALLOWED_LOCALHOST_HOSTS = new HashSet<>(Arrays.asList(
        "localhost",
        "127.0.0.1",
        "::1",
        "[::1]"
    ));

    // Blocked hosts (excluding localhost for local testing)
    private static final Set<String> BLOCKED_HOSTS = new HashSet<>(Arrays.asList(
        "0.0.0.0"
    ));

    // Blocked private IP ranges (excluding localhost)
    // Note: We allow localhost (127.0.0.1) for local testing
    private static final Pattern PRIVATE_IP_PATTERN = Pattern.compile(
        "^(10\\.|172\\.(1[6-9]|2[0-9]|3[01])\\.|192\\.168\\.|169\\.254\\.)"
    );

    // Allowed URL schemes
    private static final Set<String> ALLOWED_SCHEMES = new HashSet<>(Arrays.asList(
        "http", "https"
    ));

    // Dangerous headers that should not be set by users
    private static final Set<String> BLOCKED_HEADERS = new HashSet<>(Arrays.asList(
        "host",
        "connection",
        "upgrade",
        "content-length",
        "transfer-encoding",
        "expect"
    ));

    /**
     * Validates and sanitizes URL
     */
    public ValidationResult validateUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return ValidationResult.error("URL cannot be empty");
        }

        url = url.trim();

        // Check URL length
        if (url.length() > MAX_URL_LENGTH) {
            return ValidationResult.error("URL exceeds maximum length of " + MAX_URL_LENGTH + " characters");
        }

        try {
            URI uri = new URI(url);

            // Validate scheme
            String scheme = uri.getScheme();
            if (scheme == null) {
                return ValidationResult.error("URL must include a scheme (http:// or https://)");
            }

            scheme = scheme.toLowerCase();
            if (!ALLOWED_SCHEMES.contains(scheme)) {
                return ValidationResult.error("Only HTTP and HTTPS schemes are allowed");
            }

            // Validate host
            String host = uri.getHost();
            if (host == null) {
                return ValidationResult.error("Invalid URL: missing host");
            }

            host = host.toLowerCase();

            // Allow localhost for local testing
            if (ALLOWED_LOCALHOST_HOSTS.contains(host) || 
                host.equals("127.0.0.1") || 
                host.startsWith("localhost") || 
                host.contains(".local") ||
                host.equals("[::1]")) {
                // Allow localhost - this is a local testing tool
                return ValidationResult.success(url);
            }

            // Check for blocked hosts (excluding localhost)
            if (BLOCKED_HOSTS.contains(host)) {
                return ValidationResult.error("Requests to " + host + " are blocked for security");
            }

            // Check for private IP ranges (but allow localhost)
            if (PRIVATE_IP_PATTERN.matcher(host).find()) {
                // Allow localhost IPs (127.0.0.1) but block other private IPs
                if (!host.equals("127.0.0.1") && !host.startsWith("127.")) {
                    return ValidationResult.error("Requests to private IP ranges are blocked for security. Use localhost for local testing.");
                }
            }

            // Block 0.0.0.0 specifically
            if (host.contains("0.0.0.0")) {
                return ValidationResult.error("Requests to 0.0.0.0 are blocked for security");
            }

            return ValidationResult.success(url);

        } catch (URISyntaxException e) {
            return ValidationResult.error("Invalid URL format: " + e.getMessage());
        }
    }

    /**
     * Validates HTTP method
     */
    public ValidationResult validateMethod(String method) {
        if (method == null || method.trim().isEmpty()) {
            return ValidationResult.error("HTTP method cannot be empty");
        }

        String upperMethod = method.trim().toUpperCase();
        Set<String> allowedMethods = new HashSet<>(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS"
        ));

        if (!allowedMethods.contains(upperMethod)) {
            return ValidationResult.error("Invalid HTTP method: " + method);
        }

        return ValidationResult.success(upperMethod);
    }

    /**
     * Validates and sanitizes headers
     */
    public ValidationResult validateHeaders(String headersString) {
        if (headersString == null || headersString.trim().isEmpty()) {
            return ValidationResult.success("");
        }

        // Check total header size
        if (headersString.length() > MAX_HEADER_SIZE) {
            return ValidationResult.error("Headers exceed maximum size of " + MAX_HEADER_SIZE + " bytes");
        }

        String[] lines = headersString.split("\n");
        if (lines.length > MAX_HEADER_COUNT) {
            return ValidationResult.error("Too many headers. Maximum allowed: " + MAX_HEADER_COUNT);
        }

        StringBuilder sanitized = new StringBuilder();
        for (String line : lines) {
            if (line != null && line.contains(":")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    // Check for blocked headers
                    if (BLOCKED_HEADERS.contains(key.toLowerCase())) {
                        return ValidationResult.error("Header '" + key + "' is not allowed for security reasons");
                    }

                    // Sanitize: remove newlines and control characters from value
                    value = value.replaceAll("[\\r\\n]", "").replaceAll("[\\x00-\\x1F]", "");

                    // Check for header injection attempts
                    if (value.contains("\n") || value.contains("\r")) {
                        return ValidationResult.error("Header injection detected in header: " + key);
                    }

                    sanitized.append(key).append(": ").append(value).append("\n");
                }
            }
        }

        return ValidationResult.success(sanitized.toString().trim());
    }

    /**
     * Validates request body size
     */
    public ValidationResult validateBodySize(String body) {
        if (body == null) {
            return ValidationResult.success("");
        }

        if (body.length() > MAX_BODY_SIZE) {
            return ValidationResult.error("Request body exceeds maximum size of " + (MAX_BODY_SIZE / 1024 / 1024) + "MB");
        }

        return ValidationResult.success(body);
    }

    /**
     * Validates timeout value
     */
    public ValidationResult validateTimeout(Integer timeout) {
        if (timeout == null) {
            return ValidationResult.success(30000); // Default 30 seconds
        }

        if (timeout < 1000) {
            return ValidationResult.error("Timeout must be at least 1000ms (1 second)");
        }

        if (timeout > 300000) {
            return ValidationResult.error("Timeout cannot exceed 300000ms (5 minutes)");
        }

        return ValidationResult.success(timeout);
    }

    /**
     * Masks sensitive data in strings (for logging)
     */
    public String maskSensitiveData(String data) {
        if (data == null || data.isEmpty()) {
            return data;
        }

        // Mask passwords, tokens, API keys
        String masked = data
            .replaceAll("(?i)(password|pass|pwd)[\"'\\s:=]+([^\"'\\s,}]+)", "$1: ****")
            .replaceAll("(?i)(token|bearer|apikey|api_key|api-key)[\"'\\s:=]+([^\"'\\s,}]+)", "$1: ****")
            .replaceAll("(?i)(authorization)[\"'\\s:]+([^\"'\\s]+)", "$1: ****")
            .replaceAll("(?i)(secret|key)[\"'\\s:=]+([^\"'\\s,}]+)", "$1: ****");

        return masked;
    }

    /**
     * Validation result class
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String message;
        private final Object value;

        private ValidationResult(boolean valid, String message, Object value) {
            this.valid = valid;
            this.message = message;
            this.value = value;
        }

        public static ValidationResult success(Object value) {
            return new ValidationResult(true, null, value);
        }

        public static ValidationResult error(String message) {
            return new ValidationResult(false, message, null);
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }

        @SuppressWarnings("unchecked")
        public <T> T getValue() {
            return (T) value;
        }
    }
}

