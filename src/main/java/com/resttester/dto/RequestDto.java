/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.dto;

public class RequestDto {
    private String method;
    private String url;
    private String headers;
    private String body;
    private String name;
    private Long collectionId;
    
    // Authorization fields
    private String authType; // "none", "basic", "bearer", "apikey", "digest"
    private String username;
    private String password;
    private String token;
    private String apiKey;
    private String apiKeyHeader; // Header name for API key (e.g., "X-API-Key")
    
    // Certificate fields
    private String certificateFile; // Base64 encoded certificate file
    private String certificatePassword;
    private String certificateType; // "p12", "pem", "jks"
    private Boolean ignoreSslErrors; // For self-signed certificates
    
    // Variables for substitution (JSON string: {"key":"value"})
    private String variables;
    
    // Request timeout in milliseconds
    private Integer timeout;
    
    // Environment ID for variable substitution
    private Long environmentId;
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getHeaders() {
        return headers;
    }
    
    public void setHeaders(String headers) {
        this.headers = headers;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Long getCollectionId() {
        return collectionId;
    }
    
    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }
    
    // Authorization getters and setters
    public String getAuthType() {
        return authType;
    }
    
    public void setAuthType(String authType) {
        this.authType = authType;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getApiKeyHeader() {
        return apiKeyHeader;
    }
    
    public void setApiKeyHeader(String apiKeyHeader) {
        this.apiKeyHeader = apiKeyHeader;
    }
    
    // Certificate getters and setters
    public String getCertificateFile() {
        return certificateFile;
    }
    
    public void setCertificateFile(String certificateFile) {
        this.certificateFile = certificateFile;
    }
    
    public String getCertificatePassword() {
        return certificatePassword;
    }
    
    public void setCertificatePassword(String certificatePassword) {
        this.certificatePassword = certificatePassword;
    }
    
    public String getCertificateType() {
        return certificateType;
    }
    
    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }
    
    public Boolean getIgnoreSslErrors() {
        return ignoreSslErrors;
    }
    
    public void setIgnoreSslErrors(Boolean ignoreSslErrors) {
        this.ignoreSslErrors = ignoreSslErrors;
    }
    
    public String getVariables() {
        return variables;
    }
    
    public void setVariables(String variables) {
        this.variables = variables;
    }
    
    public Integer getTimeout() {
        return timeout;
    }
    
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
    
    public Long getEnvironmentId() {
        return environmentId;
    }
    
    public void setEnvironmentId(Long environmentId) {
        this.environmentId = environmentId;
    }
}

