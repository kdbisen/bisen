/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.service;

import com.resttester.model.ApiMonitor;
import com.resttester.model.SavedRequest;
import com.resttester.repository.ApiMonitorRepository;
import com.resttester.repository.SavedRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ApiMonitorService {
    
    private static final Logger logger = LoggerFactory.getLogger(ApiMonitorService.class);
    
    private final ApiMonitorRepository monitorRepository;
    private final SavedRequestRepository savedRequestRepository;
    private final ApiRequestService apiRequestService;
    
    public ApiMonitorService(ApiMonitorRepository monitorRepository, 
                             SavedRequestRepository savedRequestRepository,
                             ApiRequestService apiRequestService) {
        this.monitorRepository = monitorRepository;
        this.savedRequestRepository = savedRequestRepository;
        this.apiRequestService = apiRequestService;
    }
    
    @Transactional
    public ApiMonitor createMonitor(ApiMonitor monitor) {
        if (monitor.getNextCheckAt() == null) {
            monitor.setNextCheckAt(LocalDateTime.now());
        }
        if (monitor.getStatus() == null) {
            monitor.setStatus("unknown");
        }
        logger.info("Created API monitor: {}", monitor.getName());
        return monitorRepository.save(monitor);
    }
    
    public List<ApiMonitor> getAllMonitors() {
        return monitorRepository.findAll();
    }
    
    public ApiMonitor getMonitorById(Long id) {
        return monitorRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public ApiMonitor updateMonitor(Long id, ApiMonitor monitor) {
        return monitorRepository.findById(id).map(existing -> {
            existing.setName(monitor.getName());
            existing.setSchedule(monitor.getSchedule());
            existing.setEnabled(monitor.getEnabled());
            existing.setMaxFailures(monitor.getMaxFailures());
            logger.info("Updated API monitor: {}", existing.getName());
            return monitorRepository.save(existing);
        }).orElse(null);
    }
    
    @Transactional
    public void deleteMonitor(Long id) {
        monitorRepository.deleteById(id);
        logger.info("Deleted API monitor with ID: {}", id);
    }
    
    /**
     * Check monitors that are due for execution
     * Runs every minute
     */
    @Scheduled(fixedRate = 60000) // Every minute
    @Transactional
    public void checkDueMonitors() {
        LocalDateTime now = LocalDateTime.now();
        List<ApiMonitor> dueMonitors = monitorRepository.findMonitorsDueForCheck(now);
        
        if (dueMonitors.isEmpty()) {
            return;
        }
        
        logger.debug("Checking {} monitors due for execution", dueMonitors.size());
        
        for (ApiMonitor monitor : dueMonitors) {
            if (!monitor.getEnabled()) {
                continue;
            }
            
            try {
                executeMonitor(monitor);
            } catch (Exception e) {
                logger.error("Error executing monitor {}: {}", monitor.getId(), e.getMessage(), e);
                updateMonitorFailure(monitor, null, 0, "Error: " + e.getMessage());
            }
        }
    }
    
    private void executeMonitor(ApiMonitor monitor) {
        SavedRequest savedRequest = monitor.getSavedRequest();
        if (savedRequest == null) {
            logger.warn("Monitor {} has no saved request", monitor.getId());
            return;
        }
        
        logger.info("Executing monitor: {} for request: {}", monitor.getName(), savedRequest.getName());
        
        // Convert SavedRequest to RequestDto and execute
        com.resttester.dto.RequestDto requestDto = new com.resttester.dto.RequestDto();
        requestDto.setMethod(savedRequest.getMethod());
        requestDto.setUrl(savedRequest.getUrl());
        requestDto.setHeaders(savedRequest.getHeaders());
        requestDto.setBody(savedRequest.getBody());
        
        long startTime = System.currentTimeMillis();
        com.resttester.dto.ResponseDto response = apiRequestService.executeRequest(requestDto);
        long responseTime = System.currentTimeMillis() - startTime;
        
        // Update monitor with results
        boolean isHealthy = response.getStatusCode() != null && 
                           response.getStatusCode() >= 200 && 
                           response.getStatusCode() < 400;
        
        if (isHealthy) {
            updateMonitorSuccess(monitor, response, responseTime);
        } else {
            updateMonitorFailure(monitor, response, responseTime, response.getResponse());
        }
        
        // Calculate next check time based on schedule
        LocalDateTime nextCheck = calculateNextCheck(monitor.getSchedule());
        monitor.setNextCheckAt(nextCheck);
        monitorRepository.save(monitor);
    }
    
    private void updateMonitorSuccess(ApiMonitor monitor, com.resttester.dto.ResponseDto response, long responseTime) {
        monitor.setStatus("healthy");
        monitor.setConsecutiveFailures(0);
        monitor.setLastCheckAt(LocalDateTime.now());
        monitor.setLastStatusCode(response.getStatusCode());
        monitor.setLastResponseTime(responseTime);
        if (response.getResponse() != null && response.getResponse().length() > 500) {
            monitor.setLastResponse(response.getResponse().substring(0, 500) + "...");
        } else {
            monitor.setLastResponse(response.getResponse());
        }
        monitorRepository.save(monitor);
        logger.info("Monitor {} check successful: Status {}, Time {}ms", 
                   monitor.getName(), response.getStatusCode(), responseTime);
    }
    
    private void updateMonitorFailure(ApiMonitor monitor, com.resttester.dto.ResponseDto response, 
                                     long responseTime, String errorMessage) {
        int failures = monitor.getConsecutiveFailures() != null ? monitor.getConsecutiveFailures() : 0;
        failures++;
        monitor.setConsecutiveFailures(failures);
        monitor.setLastCheckAt(LocalDateTime.now());
        
        if (response != null) {
            monitor.setLastStatusCode(response.getStatusCode());
            monitor.setLastResponseTime(responseTime);
            if (response.getResponse() != null && response.getResponse().length() > 500) {
                monitor.setLastResponse(response.getResponse().substring(0, 500) + "...");
            } else {
                monitor.setLastResponse(response.getResponse());
            }
        } else {
            monitor.setLastResponse(errorMessage);
        }
        
        if (failures >= monitor.getMaxFailures()) {
            monitor.setStatus("unhealthy");
            logger.warn("Monitor {} is now UNHEALTHY after {} consecutive failures", 
                       monitor.getName(), failures);
        } else {
            monitor.setStatus("degraded");
        }
        
        monitorRepository.save(monitor);
        logger.warn("Monitor {} check failed: Failures {}, Status {}", 
                   monitor.getName(), failures, monitor.getStatus());
    }
    
    /**
     * Calculate next check time based on schedule
     * Supports simple intervals like "5m", "1h", "30s" or cron expressions
     */
    private LocalDateTime calculateNextCheck(String schedule) {
        LocalDateTime now = LocalDateTime.now();
        
        // Simple interval format (e.g., "5m", "1h", "30s")
        if (schedule.matches("\\d+[smhd]")) {
            String unit = schedule.substring(schedule.length() - 1);
            int value = Integer.parseInt(schedule.substring(0, schedule.length() - 1));
            
            switch (unit) {
                case "s":
                    return now.plusSeconds(value);
                case "m":
                    return now.plusMinutes(value);
                case "h":
                    return now.plusHours(value);
                case "d":
                    return now.plusDays(value);
                default:
                    return now.plusMinutes(5); // Default 5 minutes
            }
        }
        
        // Default to 5 minutes if schedule format is not recognized
        return now.plusMinutes(5);
    }
    
    public List<ApiMonitor> getMonitorsByStatus(String status) {
        return monitorRepository.findByStatus(status);
    }
    
    public List<ApiMonitor> getHealthyMonitors() {
        return monitorRepository.findByStatus("healthy");
    }
    
    public List<ApiMonitor> getUnhealthyMonitors() {
        return monitorRepository.findByStatus("unhealthy");
    }
}

