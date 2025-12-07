/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.config;

import com.resttester.dto.CollectionDto;
import com.resttester.dto.SavedRequestDto;
import com.resttester.model.Collection;
import com.resttester.repository.CollectionRepository;
import com.resttester.service.CollectionService;
import com.resttester.service.SavedRequestService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private CollectionService collectionService;
    
    @Autowired
    private SavedRequestService savedRequestService;
    
    @Autowired
    private CollectionRepository collectionRepository;
    
    @PostConstruct
    public void init() {
        // Check if sample collection already exists
        List<Collection> existingCollections = collectionRepository.findAll();
        boolean sampleExists = existingCollections.stream()
            .anyMatch(c -> "Sample APIs - All Methods".equals(c.getName()));
        
        if (!sampleExists) {
            logger.info("Creating sample API collection...");
            createSampleCollection();
            logger.info("Sample API collection created successfully!");
        } else {
            logger.info("Sample API collection already exists, skipping initialization.");
        }
    }
    
    private void createSampleCollection() {
        // Create main collection
        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setName("Sample APIs - All Methods");
        collectionDto.setDescription("Example collection with working APIs for all HTTP methods (GET, POST, PUT, DELETE, PATCH). These are public test APIs you can use to test BISEN API Tester.");
        Collection collection = collectionService.createCollection(collectionDto);
        Long collectionId = collection.getId();
        
        // GET Requests
        createSavedRequest(collectionId, "GET - Get All Posts", "GET", 
            "https://jsonplaceholder.typicode.com/posts", 
            "Content-Type: application/json", null);
        
        createSavedRequest(collectionId, "GET - Get Single Post", "GET", 
            "https://jsonplaceholder.typicode.com/posts/1", 
            "Content-Type: application/json", null);
        
        createSavedRequest(collectionId, "GET - Get Users", "GET", 
            "https://reqres.in/api/users?page=1", 
            "Content-Type: application/json", null);
        
        createSavedRequest(collectionId, "GET - Get Single User", "GET", 
            "https://reqres.in/api/users/2", 
            "Content-Type: application/json", null);
        
        createSavedRequest(collectionId, "GET - HTTPBin Get", "GET", 
            "https://httpbin.org/get?name=BISEN&type=API", 
            "Content-Type: application/json", null);
        
        // POST Requests
        createSavedRequest(collectionId, "POST - Create Post", "POST", 
            "https://jsonplaceholder.typicode.com/posts", 
            "Content-Type: application/json", 
            "{\n  \"title\": \"BISEN API Tester\",\n  \"body\": \"Testing POST request\",\n  \"userId\": 1\n}");
        
        createSavedRequest(collectionId, "POST - Create User", "POST", 
            "https://reqres.in/api/users", 
            "Content-Type: application/json", 
            "{\n  \"name\": \"BISEN User\",\n  \"job\": \"API Tester\"\n}");
        
        createSavedRequest(collectionId, "POST - HTTPBin Post", "POST", 
            "https://httpbin.org/post", 
            "Content-Type: application/json", 
            "{\n  \"message\": \"Hello from BISEN API Tester\",\n  \"timestamp\": \"2024-01-01T00:00:00Z\"\n}");
        
        createSavedRequest(collectionId, "POST - Login", "POST", 
            "https://reqres.in/api/login", 
            "Content-Type: application/json", 
            "{\n  \"email\": \"eve.holt@reqres.in\",\n  \"password\": \"cityslicka\"\n}");
        
        // PUT Requests
        createSavedRequest(collectionId, "PUT - Update Post", "PUT", 
            "https://jsonplaceholder.typicode.com/posts/1", 
            "Content-Type: application/json", 
            "{\n  \"id\": 1,\n  \"title\": \"Updated Title\",\n  \"body\": \"Updated body content\",\n  \"userId\": 1\n}");
        
        createSavedRequest(collectionId, "PUT - Update User", "PUT", 
            "https://reqres.in/api/users/2", 
            "Content-Type: application/json", 
            "{\n  \"name\": \"Updated Name\",\n  \"job\": \"Updated Job\"\n}");
        
        createSavedRequest(collectionId, "PUT - HTTPBin Put", "PUT", 
            "https://httpbin.org/put", 
            "Content-Type: application/json", 
            "{\n  \"data\": \"Updated data\",\n  \"status\": \"success\"\n}");
        
        // PATCH Requests
        createSavedRequest(collectionId, "PATCH - Patch Post", "PATCH", 
            "https://jsonplaceholder.typicode.com/posts/1", 
            "Content-Type: application/json", 
            "{\n  \"title\": \"Patched Title\"\n}");
        
        createSavedRequest(collectionId, "PATCH - Patch User", "PATCH", 
            "https://reqres.in/api/users/2", 
            "Content-Type: application/json", 
            "{\n  \"name\": \"Patched Name\"\n}");
        
        createSavedRequest(collectionId, "PATCH - HTTPBin Patch", "PATCH", 
            "https://httpbin.org/patch", 
            "Content-Type: application/json", 
            "{\n  \"patch\": true,\n  \"field\": \"value\"\n}");
        
        // DELETE Requests
        createSavedRequest(collectionId, "DELETE - Delete Post", "DELETE", 
            "https://jsonplaceholder.typicode.com/posts/1", 
            "Content-Type: application/json", null);
        
        createSavedRequest(collectionId, "DELETE - Delete User", "DELETE", 
            "https://reqres.in/api/users/2", 
            "Content-Type: application/json", null);
        
        createSavedRequest(collectionId, "DELETE - HTTPBin Delete", "DELETE", 
            "https://httpbin.org/delete", 
            "Content-Type: application/json", null);
        
        // HEAD Request
        createSavedRequest(collectionId, "HEAD - HTTPBin Head", "HEAD", 
            "https://httpbin.org/get", 
            "Content-Type: application/json", null);
        
        // OPTIONS Request
        createSavedRequest(collectionId, "OPTIONS - HTTPBin Options", "OPTIONS", 
            "https://httpbin.org/get", 
            "Content-Type: application/json", null);
        
        // Additional useful examples
        createSavedRequest(collectionId, "GET - Delayed Response", "GET", 
            "https://httpbin.org/delay/2", 
            "Content-Type: application/json", null);
        
        createSavedRequest(collectionId, "GET - Status Code 200", "GET", 
            "https://httpbin.org/status/200", 
            "Content-Type: application/json", null);
        
        createSavedRequest(collectionId, "GET - Status Code 404", "GET", 
            "https://httpbin.org/status/404", 
            "Content-Type: application/json", null);
        
        createSavedRequest(collectionId, "GET - JSON Response", "GET", 
            "https://httpbin.org/json", 
            "Content-Type: application/json", null);
        
        createSavedRequest(collectionId, "POST - Form Data", "POST", 
            "https://httpbin.org/post", 
            "Content-Type: application/x-www-form-urlencoded", 
            "name=BISEN&type=API+Tester");
    }
    
    private void createSavedRequest(Long collectionId, String name, String method, 
                                   String url, String headers, String body) {
        SavedRequestDto dto = new SavedRequestDto();
        dto.setName(name);
        dto.setMethod(method);
        dto.setUrl(url);
        dto.setHeaders(headers);
        dto.setBody(body);
        dto.setCollectionId(collectionId);
        savedRequestService.saveRequest(dto);
    }
}

