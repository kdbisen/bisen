/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.service;

import com.resttester.config.RestTemplateConfig;
import com.resttester.dto.RequestDto;
import com.resttester.dto.ResponseDto;
import com.resttester.model.ApiRequest;
import com.resttester.model.Collection;
import com.resttester.model.Environment;
import com.resttester.repository.ApiRequestRepository;
import com.resttester.repository.CollectionRepository;
import com.resttester.repository.EnvironmentRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resttester.security.SecurityValidator;
import com.resttester.security.SecurityValidator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ApiRequestService {
    
    private static final Logger logger = LoggerFactory.getLogger(ApiRequestService.class);
    
    private final ApiRequestRepository repository;
    private final CollectionRepository collectionRepository;
    private final EnvironmentRepository environmentRepository;
    private final RestTemplate restTemplate;
    private final SecurityValidator securityValidator;

    public ApiRequestService(ApiRequestRepository repository, CollectionRepository collectionRepository, EnvironmentRepository environmentRepository, RestTemplate restTemplate, SecurityValidator securityValidator) {
        this.repository = repository;
        this.collectionRepository = collectionRepository;
        this.environmentRepository = environmentRepository;
        this.restTemplate = restTemplate;
        this.securityValidator = securityValidator;
    }
    
    public ResponseDto executeRequest(RequestDto requestDto) {
        long startTime = System.currentTimeMillis();
        ResponseDto responseDto = new ResponseDto();
        
        // Validate request
        if (requestDto == null) {
            responseDto.setStatusCode(0);
            responseDto.setResponse("Error: Request data is null");
            responseDto.setResponseTime(0L);
            return responseDto;
        }
        
        // Security validation: Validate HTTP method
        ValidationResult methodValidation = securityValidator.validateMethod(requestDto.getMethod());
        if (!methodValidation.isValid()) {
            responseDto.setStatusCode(0);
            responseDto.setResponse("Security Error: " + methodValidation.getMessage());
            responseDto.setResponseTime(0L);
            return responseDto;
        }
        String validatedMethod = methodValidation.getValue();
        
        // Security validation: Validate URL (SSRF protection)
        ValidationResult urlValidation = securityValidator.validateUrl(requestDto.getUrl());
        if (!urlValidation.isValid()) {
            responseDto.setStatusCode(0);
            responseDto.setResponse("Security Error: " + urlValidation.getMessage());
            responseDto.setResponseTime(0L);
            return responseDto;
        }
        String validatedUrl = urlValidation.getValue();
        
        // Security validation: Validate headers
        ValidationResult headersValidation = securityValidator.validateHeaders(requestDto.getHeaders());
        if (!headersValidation.isValid()) {
            responseDto.setStatusCode(0);
            responseDto.setResponse("Security Error: " + headersValidation.getMessage());
            responseDto.setResponseTime(0L);
            return responseDto;
        }
        String validatedHeaders = headersValidation.getValue();
        
        // Security validation: Validate body size
        ValidationResult bodyValidation = securityValidator.validateBodySize(requestDto.getBody());
        if (!bodyValidation.isValid()) {
            responseDto.setStatusCode(0);
            responseDto.setResponse("Security Error: " + bodyValidation.getMessage());
            responseDto.setResponseTime(0L);
            return responseDto;
        }
        
        // Security validation: Validate timeout
        ValidationResult timeoutValidation = securityValidator.validateTimeout(requestDto.getTimeout());
        if (!timeoutValidation.isValid()) {
            responseDto.setStatusCode(0);
            responseDto.setResponse("Security Error: " + timeoutValidation.getMessage());
            responseDto.setResponseTime(0L);
            return responseDto;
        }
        
        try {
            // Get or create RestTemplate based on SSL settings
            RestTemplate template = getRestTemplate(requestDto);
            
            // Get variables from request or environment
            Map<String, String> variables = new HashMap<>();
            
            // First, load from environment if specified
            if (requestDto.getEnvironmentId() != null) {
                try {
                    Environment env = environmentRepository.findById(requestDto.getEnvironmentId()).orElse(null);
                    if (env != null && env.getVariables() != null) {
                        Map<String, String> envVars = parseVariables(env.getVariables());
                        variables.putAll(envVars);
                    }
                } catch (Exception e) {
                    // If environment loading fails, continue with request variables
                    logger.warn("Failed to load environment {}: {}", requestDto.getEnvironmentId(), e.getMessage());
                }
            }
            
            // Then, merge with request-specific variables (request variables override environment)
            Map<String, String> requestVars = parseVariables(requestDto.getVariables());
            variables.putAll(requestVars);
            
            // Substitute variables in URL, headers, and body
            String processedUrl = substituteVariables(validatedUrl, variables);
            String processedHeaders = substituteVariables(validatedHeaders, variables);
            String processedBody = substituteVariables(requestDto.getBody() != null ? requestDto.getBody() : "", variables);
            
            // Re-validate URL after variable substitution (security check)
            ValidationResult finalUrlValidation = securityValidator.validateUrl(processedUrl);
            if (!finalUrlValidation.isValid()) {
                responseDto.setStatusCode(0);
                responseDto.setResponse("Security Error: " + finalUrlValidation.getMessage() + " (after variable substitution)");
                responseDto.setResponseTime(System.currentTimeMillis() - startTime);
                return responseDto;
            }
            processedUrl = finalUrlValidation.getValue();
            
            // Re-validate headers after variable substitution
            ValidationResult finalHeadersValidation = securityValidator.validateHeaders(processedHeaders);
            if (!finalHeadersValidation.isValid()) {
                responseDto.setStatusCode(0);
                responseDto.setResponse("Security Error: " + finalHeadersValidation.getMessage() + " (after variable substitution)");
                responseDto.setResponseTime(System.currentTimeMillis() - startTime);
                return responseDto;
            }
            processedHeaders = finalHeadersValidation.getValue();
            
            HttpHeaders headers = parseHeaders(processedHeaders);
            applyAuthorization(headers, requestDto);
            
            HttpEntity<String> entity = new HttpEntity<>(
                processedBody != null ? processedBody : "", 
                headers
            );
            
            // Use validated method
            HttpMethod httpMethod = HttpMethod.valueOf(validatedMethod);
            
            logger.debug("Executing {} request to: {}", validatedMethod, processedUrl);
            
            ResponseEntity<String> response = template.exchange(
                processedUrl,
                httpMethod,
                entity,
                String.class
            );
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            responseDto.setStatusCode(response.getStatusCode() != null ? response.getStatusCode().value() : 0);
            responseDto.setResponse(response.getBody() != null ? response.getBody() : "");
            responseDto.setResponseTime(responseTime);
            responseDto.setHeaders(formatHeaders(response.getHeaders()));
            
            logger.info("Request completed: {} {} - Status: {} - Time: {}ms", 
                validatedMethod, processedUrl, responseDto.getStatusCode(), responseTime);
            
            saveRequest(requestDto, responseDto);
            
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            long responseTime = System.currentTimeMillis() - startTime;
            responseDto.setStatusCode(e.getStatusCode() != null ? e.getStatusCode().value() : 0);
            String errorBody = e.getResponseBodyAsString();
            responseDto.setResponse(errorBody != null ? errorBody : "HTTP Error: " + e.getMessage());
            responseDto.setResponseTime(responseTime);
            responseDto.setHeaders(formatHeaders(e.getResponseHeaders()));
            
            logger.warn("HTTP error for {} {}: Status {} - Time: {}ms", 
                requestDto.getMethod(), requestDto.getUrl(), responseDto.getStatusCode(), responseTime);
            
            saveRequest(requestDto, responseDto);
            
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            responseDto.setStatusCode(0);
            // Mask sensitive information in error messages
            String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            errorMessage = securityValidator.maskSensitiveData(errorMessage);
            responseDto.setResponse("Error: " + errorMessage);
            responseDto.setResponseTime(responseTime);
            responseDto.setHeaders("");
            
            logger.error("Error executing request {} {}: {}", 
                requestDto.getMethod(), requestDto.getUrl(), errorMessage, e);
            
            // Only save if we have valid request data (with masked sensitive data)
            if (requestDto.getMethod() != null && requestDto.getUrl() != null) {
                // Create a sanitized copy for saving
                RequestDto sanitizedDto = sanitizeRequestDto(requestDto);
                saveRequest(sanitizedDto, responseDto);
            }
        }
        
        return responseDto;
    }
    
    private RestTemplate getRestTemplate(RequestDto requestDto) {
        // If SSL errors should be ignored, create a custom RestTemplate
        if (requestDto.getIgnoreSslErrors() != null && requestDto.getIgnoreSslErrors()) {
            return RestTemplateConfig.createRestTemplateWithSslIgnore();
        }
        // TODO: Add certificate support for client certificates
        // For now, use the default RestTemplate
        return restTemplate;
    }
    
    private void applyAuthorization(HttpHeaders headers, RequestDto requestDto) {
        if (requestDto == null || headers == null) {
            return;
        }
        
        if (requestDto.getAuthType() == null || requestDto.getAuthType().trim().equalsIgnoreCase("none")) {
            return;
        }
        
        try {
            String authType = requestDto.getAuthType().toLowerCase().trim();
            
            switch (authType) {
                case "basic":
                    if (requestDto.getUsername() != null && requestDto.getPassword() != null) {
                        String username = requestDto.getUsername().trim();
                        String password = requestDto.getPassword().trim();
                        if (!username.isEmpty() && !password.isEmpty()) {
                            String auth = username + ":" + password;
                            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
                            headers.set("Authorization", "Basic " + encodedAuth);
                        }
                    }
                    break;
                    
                case "bearer":
                    if (requestDto.getToken() != null && !requestDto.getToken().trim().isEmpty()) {
                        headers.set("Authorization", "Bearer " + requestDto.getToken().trim());
                    }
                    break;
                    
                case "apikey":
                    if (requestDto.getApiKey() != null && !requestDto.getApiKey().trim().isEmpty()) {
                        String headerName = (requestDto.getApiKeyHeader() != null && !requestDto.getApiKeyHeader().trim().isEmpty()) 
                            ? requestDto.getApiKeyHeader().trim() 
                            : "X-API-Key";
                        headers.set(headerName, requestDto.getApiKey().trim());
                    }
                    break;
                    
                case "digest":
                    // Digest auth requires multiple round trips, simplified for now
                    if (requestDto.getUsername() != null && requestDto.getPassword() != null) {
                        String username = requestDto.getUsername().trim();
                        String password = requestDto.getPassword().trim();
                        if (!username.isEmpty() && !password.isEmpty()) {
                            // For now, just set basic auth as fallback
                            // Full digest implementation would require additional logic
                            String auth = username + ":" + password;
                            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
                            headers.set("Authorization", "Basic " + encodedAuth);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            // If authorization fails, continue without auth
            logger.warn("Error applying authorization: {}", e.getMessage(), e);
        }
    }
    
    private HttpHeaders parseHeaders(String headersString) {
        HttpHeaders headers = new HttpHeaders();
        boolean hasContentType = false;
        
        if (headersString != null && !headersString.trim().isEmpty()) {
            try {
                String[] lines = headersString.split("\n");
                for (String line : lines) {
                    if (line != null && line.contains(":")) {
                        String[] parts = line.split(":", 2);
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();
                            if (!key.isEmpty() && !value.isEmpty()) {
                                headers.set(key, value);
                                if (key.equalsIgnoreCase("Content-Type")) {
                                    hasContentType = true;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // If header parsing fails, continue with empty headers
                logger.warn("Error parsing headers: {}", e.getMessage(), e);
            }
        }
        
        // Only set default Content-Type if not already specified
        if (!hasContentType) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        
        return headers;
    }
    
    private String formatHeaders(HttpHeaders headers) {
        if (headers == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            headers.forEach((key, values) -> {
                if (key != null && values != null && !values.isEmpty()) {
                    sb.append(key).append(": ").append(String.join(", ", values)).append("\n");
                }
            });
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
    
    private RequestDto sanitizeRequestDto(RequestDto requestDto) {
        // Create a copy and mask sensitive data
        RequestDto sanitized = new RequestDto();
        sanitized.setMethod(requestDto.getMethod());
        sanitized.setUrl(requestDto.getUrl());
        sanitized.setName(requestDto.getName());
        sanitized.setCollectionId(requestDto.getCollectionId());
        
        // Mask sensitive headers
        String headers = requestDto.getHeaders();
        if (headers != null) {
            sanitized.setHeaders(securityValidator.maskSensitiveData(headers));
        }
        
        // Mask sensitive body (if it contains sensitive patterns)
        String body = requestDto.getBody();
        if (body != null) {
            sanitized.setBody(securityValidator.maskSensitiveData(body));
        }
        
        // Don't save sensitive auth data
        sanitized.setAuthType(null);
        sanitized.setUsername(null);
        sanitized.setPassword(null);
        sanitized.setToken(null);
        sanitized.setApiKey(null);
        sanitized.setCertificateFile(null);
        sanitized.setCertificatePassword(null);
        
        return sanitized;
    }
    
    private void saveRequest(RequestDto requestDto, ResponseDto responseDto) {
        try {
            if (requestDto == null || responseDto == null) {
                return; // Don't save if data is null
            }
            
            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setMethod(requestDto.getMethod() != null ? requestDto.getMethod() : "GET");
            apiRequest.setUrl(requestDto.getUrl() != null ? requestDto.getUrl() : "");
            
            // Mask sensitive data in headers before saving
            String headers = requestDto.getHeaders() != null ? requestDto.getHeaders() : "";
            apiRequest.setHeaders(securityValidator.maskSensitiveData(headers));
            
            // Mask sensitive data in body before saving
            String body = requestDto.getBody() != null ? requestDto.getBody() : "";
            apiRequest.setBody(securityValidator.maskSensitiveData(body));
            
            // Mask sensitive data in response before saving
            String response = responseDto.getResponse() != null ? responseDto.getResponse() : "";
            apiRequest.setResponse(securityValidator.maskSensitiveData(response));
            
            apiRequest.setStatusCode(responseDto.getStatusCode() != null ? responseDto.getStatusCode() : 0);
            apiRequest.setResponseTime(responseDto.getResponseTime() != null ? responseDto.getResponseTime() : 0L);
            apiRequest.setName(requestDto.getName() != null ? requestDto.getName() : "");
            
            if (requestDto.getCollectionId() != null) {
                try {
                    Collection collection = collectionRepository.findById(requestDto.getCollectionId()).orElse(null);
                    apiRequest.setCollection(collection);
                } catch (Exception e) {
                    // If collection lookup fails, continue without collection
                    apiRequest.setCollection(null);
                }
            }
            
            repository.save(apiRequest);
        } catch (Exception e) {
            // Log error but don't fail the request execution
            // Mask sensitive data in error message
            String errorMsg = securityValidator.maskSensitiveData(e.getMessage() != null ? e.getMessage() : "Unknown error");
            logger.error("Error saving request to history: {}", errorMsg, e);
        }
    }
    
    public List<ApiRequest> getAllRequests() {
        return repository.findAllByOrderByCreatedAtDesc();
    }
    
    public ApiRequest getRequestById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    public void deleteRequest(Long id) {
        repository.deleteById(id);
    }
    
    public List<ApiRequest> getRequestsByCollection(Long collectionId) {
        return repository.findByCollectionIdOrderByCreatedAtDesc(collectionId);
    }
    
    public List<ApiRequest> getRequestsWithoutCollection() {
        return repository.findByCollectionIsNullOrderByCreatedAtDesc();
    }
    
    /**
     * Parse variables from JSON string
     */
    private Map<String, String> parseVariables(String variablesJson) {
        Map<String, String> variables = new HashMap<>();
        if (variablesJson != null && !variablesJson.trim().isEmpty()) {
            try {
                // Use Jackson ObjectMapper for proper JSON parsing
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {};
                variables = mapper.readValue(variablesJson, typeRef);
            } catch (Exception e) {
                // If parsing fails, return empty map
                logger.debug("Failed to parse variables JSON: {}", e.getMessage());
            }
        }
        return variables;
    }
    
    /**
     * Substitute variables in text using {{variableName}} syntax
     */
    private String substituteVariables(String text, Map<String, String> variables) {
        if (text == null || text.isEmpty() || variables == null || variables.isEmpty()) {
            return text != null ? text : "";
        }
        
        try {
            String result = text;
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    String placeholder = "{{" + entry.getKey() + "}}";
                    result = result.replace(placeholder, entry.getValue());
                }
            }
            return result;
        } catch (Exception e) {
            // If substitution fails, return original text
            return text;
        }
    }
}

