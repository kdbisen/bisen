/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.controller;

// Collections are deprecated - removed CollectionDto and Collection imports
import com.resttester.dto.RequestDto;
import com.resttester.dto.ResponseDto;
import com.resttester.dto.SavedRequestDto;
import com.resttester.model.ApiRequest;
import com.resttester.model.SavedRequest;
import com.resttester.dto.BisenExportDto;
import com.resttester.dto.EnvironmentDto;
import com.resttester.model.Environment;
import com.resttester.service.ApiRequestService;
import com.resttester.service.ApplicationService;
// Collections are deprecated - CollectionService removed
// import com.resttester.service.CollectionService;
import com.resttester.service.EnvironmentService;
import com.resttester.service.ExportImportService;
import com.resttester.service.ProjectService;
import com.resttester.service.SavedRequestService;
import com.resttester.service.SwaggerImportService;
import com.resttester.dto.ProjectDto;
import com.resttester.dto.ApplicationDto;
import com.resttester.model.Project;
import com.resttester.model.Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ApiTesterController {
    
    @Autowired
    private ApiRequestService apiRequestService;
    
    // Collections are deprecated - CollectionService removed
    // @Autowired
    // private CollectionService collectionService;
    
    @Autowired
    private SavedRequestService savedRequestService;
    
    @Autowired
    private SwaggerImportService swaggerImportService;
    
    @Autowired
    private EnvironmentService environmentService;
    
    @Autowired
    private ExportImportService exportImportService;
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private ApplicationService applicationService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("requestDto", new RequestDto());
        model.addAttribute("methods", List.of("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS"));
        // Get all projects with their applications
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        // Get all saved requests
        List<SavedRequest> allSavedRequests = savedRequestService.getAllSavedRequests();
        model.addAttribute("savedRequests", allSavedRequests);
        // Get requests without application
        List<SavedRequest> requestsWithoutApplication = savedRequestService.getSavedRequestsWithoutApplication();
        model.addAttribute("requestsWithoutApplication", requestsWithoutApplication);
        // Get environments
        List<Environment> environments = environmentService.getAllEnvironments();
        model.addAttribute("environments", environments);
        Environment defaultEnv = environmentService.getDefaultEnvironment();
        model.addAttribute("defaultEnvironment", defaultEnv);
        return "index";
    }
    
    @PostMapping("/api/execute")
    @ResponseBody
    public ResponseEntity<ResponseDto> executeRequest(@RequestBody RequestDto requestDto) {
        ResponseDto response = apiRequestService.executeRequest(requestDto);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/history")
    public String history(Model model) {
        List<ApiRequest> requests = apiRequestService.getAllRequests();
        model.addAttribute("requests", requests);
        return "history";
    }
    
    @GetMapping("/guide")
    public String guide() {
        return "guide";
    }
    
    @GetMapping("/history/{id}")
    @ResponseBody
    public ResponseEntity<ApiRequest> getRequest(@PathVariable Long id) {
        ApiRequest request = apiRequestService.getRequestById(id);
        if (request != null) {
            return ResponseEntity.ok(request);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/history/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        apiRequestService.deleteRequest(id);
        return ResponseEntity.ok().build();
    }
    
    // Collections are deprecated - endpoints removed
    // Use Projects and Applications instead
    
    // Saved Request endpoints
    @GetMapping("/saved")
    public String savedRequests(Model model) {
        List<SavedRequest> savedRequests = savedRequestService.getAllSavedRequests();
        model.addAttribute("savedRequests", savedRequests);
        // Collections are deprecated - removed from model
        return "saved-requests";
    }
    
    @PostMapping("/api/saved")
    @ResponseBody
    public ResponseEntity<SavedRequest> saveRequest(@RequestBody SavedRequestDto dto) {
        SavedRequest savedRequest = savedRequestService.saveRequest(dto);
        return ResponseEntity.ok(savedRequest);
    }
    
    @GetMapping("/api/saved/{id}")
    @ResponseBody
    public ResponseEntity<SavedRequest> getSavedRequest(@PathVariable Long id) {
        SavedRequest savedRequest = savedRequestService.getSavedRequestById(id);
        if (savedRequest != null) {
            // The application and its environments will be loaded via EntityGraph in the repository
            return ResponseEntity.ok(savedRequest);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/api/saved/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteSavedRequest(@PathVariable Long id) {
        savedRequestService.deleteSavedRequest(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/api/saved")
    @ResponseBody
    public ResponseEntity<List<SavedRequest>> getAllSavedRequests() {
        return ResponseEntity.ok(savedRequestService.getAllSavedRequests());
    }
    
    @PutMapping("/api/saved/{id}/collection")
    @ResponseBody
    public ResponseEntity<SavedRequest> updateSavedRequestCollection(
            @PathVariable Long id, 
            @RequestBody Map<String, Long> request) {
        Long collectionId = request.get("collectionId");
        SavedRequest savedRequest = savedRequestService.getSavedRequestById(id);
        if (savedRequest == null) {
            return ResponseEntity.notFound().build();
        }
        SavedRequestDto dto = new SavedRequestDto();
        dto.setId(savedRequest.getId());
        dto.setName(savedRequest.getName());
        dto.setMethod(savedRequest.getMethod());
        dto.setUrl(savedRequest.getUrl());
        dto.setHeaders(savedRequest.getHeaders());
        dto.setBody(savedRequest.getBody());
        dto.setCollectionId(collectionId);
        SavedRequest updated = savedRequestService.saveRequest(dto);
        return ResponseEntity.ok(updated);
    }
    
    // Swagger Import endpoints
    @GetMapping("/import")
    public String importSwagger(Model model) {
        return "import-swagger";
    }
    
    @PostMapping("/api/import/url")
    @ResponseBody
    public ResponseEntity<?> importFromUrl(@RequestBody Map<String, String> request) {
        try {
            String url = request.get("url");
            SwaggerImportService.ImportResult result = swaggerImportService.importFromUrl(url);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            SwaggerImportService.ImportResult errorResult = new SwaggerImportService.ImportResult();
            errorResult.setSuccess(false);
            errorResult.setMessage("Error importing from URL: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResult);
        }
    }
    
    @PostMapping("/api/import/file")
    @ResponseBody
    public ResponseEntity<?> importFromFile(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            SwaggerImportService.ImportResult result = swaggerImportService.importFromFile(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            SwaggerImportService.ImportResult errorResult = new SwaggerImportService.ImportResult();
            errorResult.setSuccess(false);
            errorResult.setMessage("Error importing file: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResult);
        }
    }
    
    // Environment endpoints (application-based)
    @GetMapping("/api/applications/{applicationId}/environments")
    @ResponseBody
    public ResponseEntity<List<Environment>> getEnvironmentsByApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(environmentService.getEnvironmentsByApplication(applicationId));
    }
    
    @GetMapping("/api/environments/{id}")
    @ResponseBody
    public ResponseEntity<Environment> getEnvironment(@PathVariable Long id) {
        Environment env = environmentService.getEnvironmentById(id);
        if (env != null) {
            return ResponseEntity.ok(env);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/api/environments")
    @ResponseBody
    public ResponseEntity<?> createEnvironment(@RequestBody EnvironmentDto dto) {
        try {
            if (dto.getApplicationId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Application ID is required"));
            }
            Environment env = environmentService.createEnvironment(dto);
            return ResponseEntity.ok(env);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/api/environments/{id}")
    @ResponseBody
    public ResponseEntity<?> updateEnvironment(@PathVariable Long id, @RequestBody EnvironmentDto dto) {
        try {
            Environment env = environmentService.updateEnvironment(id, dto);
            if (env != null) {
                return ResponseEntity.ok(env);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/api/environments/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteEnvironment(@PathVariable Long id) {
        environmentService.deleteEnvironment(id);
        return ResponseEntity.ok().build();
    }
    
    // Export/Import endpoints
    @GetMapping("/api/export")
    @ResponseBody
    public ResponseEntity<BisenExportDto> exportAll() {
        BisenExportDto export = exportImportService.exportAll();
        return ResponseEntity.ok(export);
    }
    
    @PostMapping("/api/import/bisen")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> importBisen(@RequestBody BisenExportDto importData) {
        Map<String, Object> result = exportImportService.importBisenFormat(importData);
        return ResponseEntity.ok(result);
    }
    
    // Project endpoints
    @GetMapping("/projects")
    public String projects(Model model) {
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        return "projects";
    }
    
    @GetMapping("/api/projects")
    @ResponseBody
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }
    
    @GetMapping("/api/projects/{id}")
    @ResponseBody
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/api/projects")
    @ResponseBody
    public ResponseEntity<Project> createProject(@RequestBody ProjectDto projectDto) {
        Project project = projectService.createProject(projectDto);
        return ResponseEntity.ok(project);
    }
    
    @PutMapping("/api/projects/{id}")
    @ResponseBody
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        Project project = projectService.updateProject(id, projectDto);
        if (project != null) {
            return ResponseEntity.ok(project);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/api/projects/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
    
    // Application endpoints
    @GetMapping("/api/applications")
    @ResponseBody
    public ResponseEntity<List<Application>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }
    
    @GetMapping("/api/projects/{projectId}/applications")
    @ResponseBody
    public ResponseEntity<List<Application>> getApplicationsByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(applicationService.getApplicationsByProject(projectId));
    }
    
    @GetMapping("/api/applications/{id}")
    @ResponseBody
    public ResponseEntity<Application> getApplication(@PathVariable Long id) {
        Application application = applicationService.getApplicationById(id);
        if (application != null) {
            return ResponseEntity.ok(application);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/api/applications")
    @ResponseBody
    public ResponseEntity<Application> createApplication(@RequestBody ApplicationDto applicationDto) {
        Application application = applicationService.createApplication(applicationDto);
        return ResponseEntity.ok(application);
    }
    
    @PutMapping("/api/applications/{id}")
    @ResponseBody
    public ResponseEntity<Application> updateApplication(@PathVariable Long id, @RequestBody ApplicationDto applicationDto) {
        Application application = applicationService.updateApplication(id, applicationDto);
        if (application != null) {
            return ResponseEntity.ok(application);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/api/applications/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.ok().build();
    }
}

