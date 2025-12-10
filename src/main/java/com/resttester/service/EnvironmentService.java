/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.service;

import com.resttester.dto.EnvironmentDto;
import com.resttester.model.Application;
import com.resttester.model.Environment;
import com.resttester.repository.ApplicationRepository;
import com.resttester.repository.EnvironmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnvironmentService {
    
    private static final Logger logger = LoggerFactory.getLogger(EnvironmentService.class);
    private final EnvironmentRepository repository;
    private final ApplicationRepository applicationRepository;
    
    public EnvironmentService(EnvironmentRepository repository, ApplicationRepository applicationRepository) {
        this.repository = repository;
        this.applicationRepository = applicationRepository;
    }
    
    @Transactional
    public Environment createEnvironment(EnvironmentDto dto) {
        if (dto.getApplicationId() == null) {
            throw new IllegalArgumentException("Application ID is required for environment creation");
        }
        
        Application application = applicationRepository.findById(dto.getApplicationId())
            .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + dto.getApplicationId()));
        
        Environment environment = new Environment();
        environment.setName(dto.getName());
        environment.setDescription(dto.getDescription());
        environment.setVariables(dto.getVariables());
        environment.setApplication(application);
        
        logger.info("Created new environment '{}' for application '{}'", environment.getName(), application.getName());
        return repository.save(environment);
    }
    
    public List<Environment> getEnvironmentsByApplication(Long applicationId) {
        return repository.findByApplicationIdOrderByNameAsc(applicationId);
    }
    
    /**
     * Get all environments across all applications
     * Note: Since environments are now application-based, this returns all environments
     */
    public List<Environment> getAllEnvironments() {
        return repository.findAll();
    }
    
    /**
     * Get default environment
     * Note: Since default environments are deprecated, this returns the first environment
     * or null if no environments exist. Consider using application-specific environments instead.
     */
    public Environment getDefaultEnvironment() {
        List<Environment> allEnvironments = repository.findAll();
        return allEnvironments.isEmpty() ? null : allEnvironments.get(0);
    }
    
    public Environment getEnvironmentById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    @Transactional
    public Environment updateEnvironment(Long id, EnvironmentDto dto) {
        Environment environment = repository.findById(id).orElse(null);
        if (environment == null) {
            return null;
        }
        
        // Verify applicationId matches if provided
        if (dto.getApplicationId() != null && !environment.getApplication().getId().equals(dto.getApplicationId())) {
            throw new IllegalArgumentException("Cannot change application of an existing environment");
        }
        
        environment.setName(dto.getName());
        environment.setDescription(dto.getDescription());
        environment.setVariables(dto.getVariables());
        
        logger.info("Updated environment: {}", environment.getName());
        return repository.save(environment);
    }
    
    @Transactional
    public void deleteEnvironment(Long id) {
        repository.deleteById(id);
        logger.info("Deleted environment with ID: {}", id);
    }
}

