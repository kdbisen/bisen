/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_requests", indexes = {
    @Index(name = "idx_api_request_created_at", columnList = "created_at"),
    @Index(name = "idx_api_request_method", columnList = "method"),
    @Index(name = "idx_api_request_collection_id", columnList = "collection_id"),
    @Index(name = "idx_api_request_status_code", columnList = "status_code")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ApiRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String method;
    
    @Column(nullable = false, length = 2000)
    private String url;
    
    @Column(columnDefinition = "TEXT")
    private String headers;
    
    @Column(columnDefinition = "TEXT")
    private String body;
    
    @Column(columnDefinition = "TEXT")
    private String response;
    
    private Integer statusCode;
    
    private Long responseTime;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id")
    @JsonIgnore
    private Collection collection;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public Integer getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
    
    public Long getResponseTime() {
        return responseTime;
    }
    
    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Collection getCollection() {
        return collection;
    }
    
    public void setCollection(Collection collection) {
        this.collection = collection;
    }
}

