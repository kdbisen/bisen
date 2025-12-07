/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.resttester.model.Collection;
import com.resttester.model.SavedRequest;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SwaggerImportService {
    
    private static final Logger logger = LoggerFactory.getLogger(SwaggerImportService.class);
    
    private final CollectionService collectionService;
    private final SavedRequestService savedRequestService;
    private final ObjectMapper jsonMapper;
    private final ObjectMapper yamlMapper;
    
    public SwaggerImportService(CollectionService collectionService, SavedRequestService savedRequestService) {
        this.collectionService = collectionService;
        this.savedRequestService = savedRequestService;
        this.jsonMapper = new ObjectMapper();
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
    }
    
    public ImportResult importFromFile(MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        String fileName = file.getOriginalFilename();
        
        return parseAndImport(content, fileName);
    }
    
    public ImportResult importFromUrl(String url) throws IOException {
        // Fetch the content from URL
        java.net.URL swaggerUrl = new java.net.URL(url);
        try (java.io.InputStream in = swaggerUrl.openStream()) {
            String content = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            return parseAndImport(content, url);
        }
    }
    
    private ImportResult parseAndImport(String content, String source) {
        ImportResult result = new ImportResult();
        
        try {
            // Try to parse as OpenAPI/Swagger
            OpenAPIV3Parser parser = new OpenAPIV3Parser();
            SwaggerParseResult parseResult = parser.readContents(content, null, null);
            
            if (parseResult.getOpenAPI() == null) {
                result.setSuccess(false);
                result.setMessage("Failed to parse Swagger/OpenAPI specification: " + 
                    (parseResult.getMessages() != null ? String.join(", ", parseResult.getMessages()) : "Unknown error"));
                return result;
            }
            
            OpenAPI openAPI = parseResult.getOpenAPI();
            String baseUrl = getBaseUrl(openAPI);
            
            // Group endpoints by tags
            Map<String, List<EndpointInfo>> endpointsByTag = new HashMap<>();
            
            if (openAPI.getPaths() != null) {
                openAPI.getPaths().forEach((path, pathItem) -> {
                    if (pathItem.getGet() != null) {
                        addEndpoint(endpointsByTag, pathItem.getGet().getTags(), "GET", path, 
                            pathItem.getGet().getSummary(), pathItem.getGet().getRequestBody(), openAPI);
                    }
                    if (pathItem.getPost() != null) {
                        addEndpoint(endpointsByTag, pathItem.getPost().getTags(), "POST", path, 
                            pathItem.getPost().getSummary(), pathItem.getPost().getRequestBody(), openAPI);
                    }
                    if (pathItem.getPut() != null) {
                        addEndpoint(endpointsByTag, pathItem.getPut().getTags(), "PUT", path, 
                            pathItem.getPut().getSummary(), pathItem.getPut().getRequestBody(), openAPI);
                    }
                    if (pathItem.getDelete() != null) {
                        addEndpoint(endpointsByTag, pathItem.getDelete().getTags(), "DELETE", path, 
                            pathItem.getDelete().getSummary(), pathItem.getDelete().getRequestBody(), openAPI);
                    }
                    if (pathItem.getPatch() != null) {
                        addEndpoint(endpointsByTag, pathItem.getPatch().getTags(), "PATCH", path, 
                            pathItem.getPatch().getSummary(), pathItem.getPatch().getRequestBody(), openAPI);
                    }
                });
            }
            
            // Create collections and saved requests
            String mainCollectionName = openAPI.getInfo() != null && openAPI.getInfo().getTitle() != null 
                ? openAPI.getInfo().getTitle() 
                : "Imported from " + source;
            
            // Create main collection
            com.resttester.dto.CollectionDto mainCollectionDto = new com.resttester.dto.CollectionDto();
            mainCollectionDto.setName(mainCollectionName);
            mainCollectionDto.setDescription(openAPI.getInfo() != null ? openAPI.getInfo().getDescription() : 
                "Imported from " + source);
            Collection mainCollection = collectionService.createCollection(mainCollectionDto);
            result.getCreatedCollections().add(mainCollection.getName());
            
            final AtomicInteger totalRequests = new AtomicInteger(0);
            final String finalBaseUrl = baseUrl;
            final Long mainCollectionId = mainCollection.getId();
            
            // Create collections for each tag and add endpoints
            for (Map.Entry<String, List<EndpointInfo>> entry : endpointsByTag.entrySet()) {
                String tag = entry.getKey();
                List<EndpointInfo> endpoints = entry.getValue();
                
                // Create collection for this tag
                com.resttester.dto.CollectionDto tagCollectionDto = new com.resttester.dto.CollectionDto();
                tagCollectionDto.setName(tag);
                tagCollectionDto.setDescription("Endpoints for " + tag);
                Collection tagCollection = collectionService.createCollection(tagCollectionDto);
                result.getCreatedCollections().add(tagCollection.getName());
                
                // Create saved requests for each endpoint
                for (EndpointInfo endpoint : endpoints) {
                    com.resttester.dto.SavedRequestDto requestDto = new com.resttester.dto.SavedRequestDto();
                    requestDto.setName(endpoint.getName());
                    requestDto.setMethod(endpoint.getMethod());
                    requestDto.setUrl(finalBaseUrl + endpoint.getPath());
                    requestDto.setHeaders("Content-Type: application/json");
                    requestDto.setBody(endpoint.getRequestBody());
                    requestDto.setCollectionId(tagCollection.getId());
                    
                    savedRequestService.saveRequest(requestDto);
                    totalRequests.incrementAndGet();
                }
            }
            
            // If no tags, add all endpoints to main collection
            if (endpointsByTag.isEmpty() && openAPI.getPaths() != null) {
                openAPI.getPaths().forEach((path, pathItem) -> {
                    if (pathItem.getGet() != null) {
                        createSavedRequest("GET", path, finalBaseUrl, pathItem.getGet().getSummary(), null, mainCollectionId);
                        totalRequests.incrementAndGet();
                    }
                    if (pathItem.getPost() != null) {
                        createSavedRequest("POST", path, finalBaseUrl, pathItem.getPost().getSummary(), 
                            pathItem.getPost().getRequestBody(), mainCollectionId);
                        totalRequests.incrementAndGet();
                    }
                    if (pathItem.getPut() != null) {
                        createSavedRequest("PUT", path, finalBaseUrl, pathItem.getPut().getSummary(), 
                            pathItem.getPut().getRequestBody(), mainCollectionId);
                        totalRequests.incrementAndGet();
                    }
                    if (pathItem.getDelete() != null) {
                        createSavedRequest("DELETE", path, finalBaseUrl, pathItem.getDelete().getSummary(), null, mainCollectionId);
                        totalRequests.incrementAndGet();
                    }
                    if (pathItem.getPatch() != null) {
                        createSavedRequest("PATCH", path, finalBaseUrl, pathItem.getPatch().getSummary(), 
                            pathItem.getPatch().getRequestBody(), mainCollectionId);
                        totalRequests.incrementAndGet();
                    }
                });
            }
            
            int totalCount = totalRequests.get();
            result.setSuccess(true);
            result.setMessage(String.format("Successfully imported %d endpoints into %d collections", 
                totalCount, result.getCreatedCollections().size()));
            result.setTotalEndpoints(totalCount);
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Error importing Swagger: " + e.getMessage());
            logger.error("Error importing Swagger: {}", e.getMessage(), e);
        }
        
        return result;
    }
    
    private void addEndpoint(Map<String, List<EndpointInfo>> endpointsByTag, List<String> tags, 
                            String method, String path, String summary, 
                            io.swagger.v3.oas.models.parameters.RequestBody requestBody, OpenAPI openAPI) {
        String tag = (tags != null && !tags.isEmpty()) ? tags.get(0) : "Default";
        String name = summary != null ? summary : method + " " + path;
        String body = extractRequestBody(requestBody, openAPI);
        
        endpointsByTag.computeIfAbsent(tag, k -> new ArrayList<>())
            .add(new EndpointInfo(method, path, name, body));
    }
    
    private void createSavedRequest(String method, String path, String baseUrl, String summary, 
                                   io.swagger.v3.oas.models.parameters.RequestBody requestBody, Long collectionId) {
        com.resttester.dto.SavedRequestDto requestDto = new com.resttester.dto.SavedRequestDto();
        requestDto.setName(summary != null ? summary : method + " " + path);
        requestDto.setMethod(method);
        requestDto.setUrl(baseUrl + path);
        requestDto.setHeaders("Content-Type: application/json");
        requestDto.setBody(extractRequestBody(requestBody, null));
        requestDto.setCollectionId(collectionId);
        savedRequestService.saveRequest(requestDto);
    }
    
    private String extractRequestBody(io.swagger.v3.oas.models.parameters.RequestBody requestBody, OpenAPI openAPI) {
        if (requestBody == null || requestBody.getContent() == null) {
            return "";
        }
        
        try {
            io.swagger.v3.oas.models.media.MediaType mediaType = requestBody.getContent().get("application/json");
            if (mediaType != null && mediaType.getSchema() != null) {
                // Try to generate example JSON
                return generateExampleJson(mediaType.getSchema(), openAPI);
            }
        } catch (Exception e) {
            // Return empty if we can't generate example
        }
        
        return "{}";
    }
    
    private String generateExampleJson(io.swagger.v3.oas.models.media.Schema<?> schema, OpenAPI openAPI) {
        if (schema == null) {
            return "{}";
        }
        
        Map<String, Object> example = new HashMap<>();
        
        if (schema.getProperties() != null) {
            schema.getProperties().forEach((key, propSchema) -> {
                Object value = getExampleValue(propSchema);
                example.put(key, value);
            });
        }
        
        try {
            return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(example);
        } catch (Exception e) {
            return "{}";
        }
    }
    
    private Object getExampleValue(io.swagger.v3.oas.models.media.Schema<?> schema) {
        if (schema == null) {
            return "";
        }
        
        if (schema.getExample() != null) {
            return schema.getExample();
        }
        
        String type = schema.getType();
        if (type == null && schema.get$ref() != null) {
            return "{}";
        }
        
        switch (type != null ? type : "") {
            case "string":
                return schema.getFormat() != null && schema.getFormat().equals("date-time") ? "" : "string";
            case "integer":
            case "number":
                return 0;
            case "boolean":
                return false;
            case "array":
                return new ArrayList<>();
            case "object":
                return new HashMap<>();
            default:
                return "";
        }
    }
    
    private String getBaseUrl(OpenAPI openAPI) {
        if (openAPI.getServers() != null && !openAPI.getServers().isEmpty()) {
            String serverUrl = openAPI.getServers().get(0).getUrl();
            // Remove trailing slash
            return serverUrl.endsWith("/") ? serverUrl.substring(0, serverUrl.length() - 1) : serverUrl;
        }
        return "http://localhost:8080";
    }
    
    private static class EndpointInfo {
        private String method;
        private String path;
        private String name;
        private String requestBody;
        
        public EndpointInfo(String method, String path, String name, String requestBody) {
            this.method = method;
            this.path = path;
            this.name = name;
            this.requestBody = requestBody;
        }
        
        public String getMethod() { return method; }
        public String getPath() { return path; }
        public String getName() { return name; }
        public String getRequestBody() { return requestBody; }
    }
    
    public static class ImportResult {
        private boolean success;
        private String message;
        private int totalEndpoints;
        private List<String> createdCollections = new ArrayList<>();
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public int getTotalEndpoints() { return totalEndpoints; }
        public void setTotalEndpoints(int totalEndpoints) { this.totalEndpoints = totalEndpoints; }
        
        public List<String> getCreatedCollections() { return createdCollections; }
    }
}
