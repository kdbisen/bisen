/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resttester.dto.ApplicationDto;
import com.resttester.dto.EnvironmentDto;
import com.resttester.dto.ProjectDto;
import com.resttester.dto.SavedRequestDto;
import com.resttester.model.Project;
import com.resttester.repository.ProjectRepository;
import com.resttester.service.ApplicationService;
import com.resttester.service.EnvironmentService;
import com.resttester.service.ProjectService;
import com.resttester.service.SavedRequestService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private ApplicationService applicationService;
    
    @Autowired
    private SavedRequestService savedRequestService;
    
    @Autowired
    private EnvironmentService environmentService;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @PostConstruct
    public void init() {
        // Check if sample project already exists
        List<Project> existingProjects = projectRepository.findAll();
        boolean sampleExists = existingProjects.stream()
            .anyMatch(p -> "Sample APIs".equals(p.getName()));
        
        if (!sampleExists) {
            logger.info("Creating sample API project with one API of each type...");
            createSampleProject();
            logger.info("Sample API project created successfully!");
        } else {
            logger.info("Sample API project already exists, skipping initialization.");
        }
    }
    
    private void createSampleProject() {
        // Create Project
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Sample APIs");
        projectDto.setDescription("Sample project with one API of each HTTP method type for testing");
        projectDto.setUrl("https://httpbin.org");
        Project project = projectService.createProject(projectDto);
        Long projectId = project.getId();
        
        // Create Application
        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setName("Sample Application");
        applicationDto.setDescription("Sample application with environment variables");
        applicationDto.setVersion("1.0.0");
        applicationDto.setProjectId(projectId);
        var application = applicationService.createApplication(applicationDto);
        Long applicationId = application.getId();
        
        // Create Environment with variables
        EnvironmentDto environmentDto = new EnvironmentDto();
        environmentDto.setName("Development");
        environmentDto.setDescription("Development environment with sample variables");
        environmentDto.setApplicationId(applicationId);
        
        // Create environment variables as JSON
        Map<String, String> variables = new HashMap<>();
        variables.put("baseUrl", "https://httpbin.org");
        variables.put("apiKey", "sample-api-key-12345");
        variables.put("userId", "1");
        variables.put("timeout", "5000");
        variables.put("version", "v1");
        
        try {
            String variablesJson = objectMapper.writeValueAsString(variables);
            environmentDto.setVariables(variablesJson);
            environmentService.createEnvironment(environmentDto);
            logger.info("Created environment with variables: {}", variablesJson);
        } catch (Exception e) {
            logger.error("Failed to create environment variables: {}", e.getMessage());
        }
        
        // Create one API of each HTTP method type
        // GET
        createSavedRequest(applicationId, "GET Request", "GET", 
            "https://httpbin.org/get?name=BISEN&type=API", 
            "Content-Type: application/json", null);
        
        // POST
        createSavedRequest(applicationId, "POST Request", "POST", 
            "https://httpbin.org/post", 
            "Content-Type: application/json", 
            "{\n  \"message\": \"Hello from BISEN API Tester\",\n  \"timestamp\": \"2024-01-01T00:00:00Z\"\n}");
        
        // PUT
        createSavedRequest(applicationId, "PUT Request", "PUT", 
            "https://httpbin.org/put", 
            "Content-Type: application/json", 
            "{\n  \"data\": \"Updated data\",\n  \"status\": \"success\"\n}");
        
        // PATCH
        createSavedRequest(applicationId, "PATCH Request", "PATCH", 
            "https://httpbin.org/patch", 
            "Content-Type: application/json", 
            "{\n  \"patch\": true,\n  \"field\": \"value\"\n}");
        
        // DELETE
        createSavedRequest(applicationId, "DELETE Request", "DELETE", 
            "https://httpbin.org/delete", 
            "Content-Type: application/json", null);
        
        // HEAD
        createSavedRequest(applicationId, "HEAD Request", "HEAD", 
            "https://httpbin.org/get", 
            "Content-Type: application/json", null);
        
        // OPTIONS
        createSavedRequest(applicationId, "OPTIONS Request", "OPTIONS", 
            "https://httpbin.org/get", 
            "Content-Type: application/json", null);
    }
    
    private void createSavedRequest(Long applicationId, String name, String method, 
                                   String url, String headers, String body) {
        SavedRequestDto dto = new SavedRequestDto();
        dto.setName(name);
        dto.setMethod(method);
        dto.setUrl(url);
        dto.setHeaders(headers);
        dto.setBody(body);
        dto.setApplicationId(applicationId);
        savedRequestService.saveRequest(dto);
    }
}

