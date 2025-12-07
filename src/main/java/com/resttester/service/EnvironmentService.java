/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.service;

import com.resttester.dto.EnvironmentDto;
import com.resttester.model.Environment;
import com.resttester.repository.EnvironmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnvironmentService {
    
    private final EnvironmentRepository repository;
    
    public EnvironmentService(EnvironmentRepository repository) {
        this.repository = repository;
    }
    
    public Environment createEnvironment(EnvironmentDto dto) {
        Environment environment = new Environment();
        environment.setName(dto.getName());
        environment.setDescription(dto.getDescription());
        environment.setVariables(dto.getVariables());
        environment.setIsDefault(dto.getIsDefault() != null && dto.getIsDefault());
        
        // If this is set as default, unset other defaults
        if (environment.getIsDefault()) {
            repository.findAll().forEach(env -> {
                if (env.getIsDefault() && !env.getId().equals(environment.getId())) {
                    env.setIsDefault(false);
                    repository.save(env);
                }
            });
        }
        
        return repository.save(environment);
    }
    
    public List<Environment> getAllEnvironments() {
        return repository.findAllByOrderByCreatedAtAsc();
    }
    
    public Environment getEnvironmentById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    public Environment getDefaultEnvironment() {
        return repository.findByIsDefaultTrue().orElse(null);
    }
    
    @Transactional
    public Environment updateEnvironment(Long id, EnvironmentDto dto) {
        Environment environment = repository.findById(id).orElse(null);
        if (environment == null) {
            return null;
        }
        
        environment.setName(dto.getName());
        environment.setDescription(dto.getDescription());
        environment.setVariables(dto.getVariables());
        
        if (dto.getIsDefault() != null && dto.getIsDefault()) {
            // Unset other defaults
            repository.findAll().forEach(env -> {
                if (env.getIsDefault() && !env.getId().equals(id)) {
                    env.setIsDefault(false);
                    repository.save(env);
                }
            });
            environment.setIsDefault(true);
        }
        
        return repository.save(environment);
    }
    
    public void deleteEnvironment(Long id) {
        repository.deleteById(id);
    }
    
    @Transactional
    public Environment setDefaultEnvironment(Long id) {
        // Unset all defaults
        repository.findAll().forEach(env -> {
            env.setIsDefault(false);
            repository.save(env);
        });
        
        // Set new default
        Environment environment = repository.findById(id).orElse(null);
        if (environment != null) {
            environment.setIsDefault(true);
            return repository.save(environment);
        }
        return null;
    }
}

