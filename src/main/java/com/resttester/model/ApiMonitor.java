/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_monitors", indexes = {
    @Index(name = "idx_monitor_saved_request_id", columnList = "saved_request_id"),
    @Index(name = "idx_monitor_status", columnList = "status"),
    @Index(name = "idx_monitor_next_check", columnList = "next_check_at")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ApiMonitor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saved_request_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private SavedRequest savedRequest;
    
    @Column(nullable = false)
    private String schedule; // Cron expression or interval (e.g., "*/5 * * * *" or "5m")
    
    @Column(nullable = false)
    private Boolean enabled = true;
    
    @Column
    private String status; // "healthy", "unhealthy", "unknown"
    
    @Column
    private LocalDateTime lastCheckAt;
    
    @Column
    private LocalDateTime nextCheckAt;
    
    @Column
    private Integer consecutiveFailures = 0;
    
    @Column
    private Integer maxFailures = 3; // Alert after this many failures
    
    @Column(columnDefinition = "TEXT")
    private String lastResponse;
    
    @Column
    private Integer lastStatusCode;
    
    @Column
    private Long lastResponseTime;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (nextCheckAt == null) {
            nextCheckAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public SavedRequest getSavedRequest() {
        return savedRequest;
    }
    
    public void setSavedRequest(SavedRequest savedRequest) {
        this.savedRequest = savedRequest;
    }
    
    public String getSchedule() {
        return schedule;
    }
    
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    public Boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getLastCheckAt() {
        return lastCheckAt;
    }
    
    public void setLastCheckAt(LocalDateTime lastCheckAt) {
        this.lastCheckAt = lastCheckAt;
    }
    
    public LocalDateTime getNextCheckAt() {
        return nextCheckAt;
    }
    
    public void setNextCheckAt(LocalDateTime nextCheckAt) {
        this.nextCheckAt = nextCheckAt;
    }
    
    public Integer getConsecutiveFailures() {
        return consecutiveFailures;
    }
    
    public void setConsecutiveFailures(Integer consecutiveFailures) {
        this.consecutiveFailures = consecutiveFailures;
    }
    
    public Integer getMaxFailures() {
        return maxFailures;
    }
    
    public void setMaxFailures(Integer maxFailures) {
        this.maxFailures = maxFailures;
    }
    
    public String getLastResponse() {
        return lastResponse;
    }
    
    public void setLastResponse(String lastResponse) {
        this.lastResponse = lastResponse;
    }
    
    public Integer getLastStatusCode() {
        return lastStatusCode;
    }
    
    public void setLastStatusCode(Integer lastStatusCode) {
        this.lastStatusCode = lastStatusCode;
    }
    
    public Long getLastResponseTime() {
        return lastResponseTime;
    }
    
    public void setLastResponseTime(Long lastResponseTime) {
        this.lastResponseTime = lastResponseTime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

