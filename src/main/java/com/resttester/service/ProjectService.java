/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.service;

import com.resttester.dto.ProjectDto;
import com.resttester.model.Project;
import com.resttester.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
    private final ProjectRepository repository;
    
    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public Project createProject(ProjectDto projectDto) {
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setUrl(projectDto.getUrl());
        logger.info("Created new project: {}", project.getName());
        return repository.save(project);
    }
    
    public List<Project> getAllProjects() {
        return repository.findAllByOrderByCreatedAtDesc();
    }
    
    public Project getProjectById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    @Transactional
    public Project updateProject(Long id, ProjectDto projectDto) {
        return repository.findById(id).map(project -> {
            project.setName(projectDto.getName());
            project.setDescription(projectDto.getDescription());
            project.setUrl(projectDto.getUrl());
            logger.info("Updated project: {}", project.getName());
            return repository.save(project);
        }).orElse(null);
    }
    
    @Transactional
    public void deleteProject(Long id) {
        repository.deleteById(id);
        logger.info("Deleted project with ID: {}", id);
    }
}

