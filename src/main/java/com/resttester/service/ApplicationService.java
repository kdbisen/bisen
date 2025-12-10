/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.service;

import com.resttester.dto.ApplicationDto;
import com.resttester.model.Application;
import com.resttester.model.Project;
import com.resttester.repository.ApplicationRepository;
import com.resttester.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationService {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);
    private final ApplicationRepository repository;
    private final ProjectRepository projectRepository;
    
    public ApplicationService(ApplicationRepository repository, ProjectRepository projectRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
    }
    
    @Transactional
    public Application createApplication(ApplicationDto applicationDto) {
        Application application = new Application();
        application.setName(applicationDto.getName());
        application.setDescription(applicationDto.getDescription());
        application.setVersion(applicationDto.getVersion());
        
        if (applicationDto.getProjectId() != null) {
            Project project = projectRepository.findById(applicationDto.getProjectId()).orElse(null);
            if (project != null) {
                application.setProject(project);
                logger.info("Created new application: {} in project: {}", application.getName(), project.getName());
            } else {
                logger.warn("Project with ID {} not found when creating application", applicationDto.getProjectId());
            }
        }
        
        return repository.save(application);
    }
    
    public List<Application> getAllApplications() {
        return repository.findAllByOrderByCreatedAtDesc();
    }
    
    public List<Application> getApplicationsByProject(Long projectId) {
        return repository.findByProjectIdOrderByNameAsc(projectId);
    }
    
    public Application getApplicationById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    @Transactional
    public Application updateApplication(Long id, ApplicationDto applicationDto) {
        return repository.findById(id).map(application -> {
            application.setName(applicationDto.getName());
            application.setDescription(applicationDto.getDescription());
            application.setVersion(applicationDto.getVersion());
            
            if (applicationDto.getProjectId() != null) {
                Project project = projectRepository.findById(applicationDto.getProjectId()).orElse(null);
                if (project != null) {
                    application.setProject(project);
                }
            }
            
            logger.info("Updated application: {}", application.getName());
            return repository.save(application);
        }).orElse(null);
    }
    
    @Transactional
    public void deleteApplication(Long id) {
        repository.deleteById(id);
        logger.info("Deleted application with ID: {}", id);
    }
}

