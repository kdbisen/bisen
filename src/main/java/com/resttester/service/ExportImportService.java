/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resttester.dto.BisenExportDto;
import com.resttester.model.Collection;
import com.resttester.model.Environment;
import com.resttester.model.SavedRequest;
import com.resttester.repository.CollectionRepository;
import com.resttester.repository.EnvironmentRepository;
import com.resttester.repository.SavedRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExportImportService {
    
    private final CollectionRepository collectionRepository;
    private final SavedRequestRepository savedRequestRepository;
    private final EnvironmentRepository environmentRepository;
    private final ObjectMapper objectMapper;
    
    public ExportImportService(
            CollectionRepository collectionRepository,
            SavedRequestRepository savedRequestRepository,
            EnvironmentRepository environmentRepository,
            ObjectMapper objectMapper) {
        this.collectionRepository = collectionRepository;
        this.savedRequestRepository = savedRequestRepository;
        this.environmentRepository = environmentRepository;
        this.objectMapper = objectMapper;
    }
    
    public BisenExportDto exportAll() {
        BisenExportDto export = new BisenExportDto();
        export.setExportedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        
        // Export collections with their requests
        List<Collection> collections = collectionRepository.findAll();
        List<BisenExportDto.CollectionExportDto> collectionExports = new ArrayList<>();
        
        for (Collection collection : collections) {
            BisenExportDto.CollectionExportDto collectionExport = new BisenExportDto.CollectionExportDto();
            collectionExport.setName(collection.getName());
            collectionExport.setDescription(collection.getDescription());
            
            List<SavedRequest> requests = savedRequestRepository.findByCollectionIdOrderByUpdatedAtDesc(collection.getId());
            collectionExport.setRequests(requests.stream().map(this::toSavedRequestExport).collect(Collectors.toList()));
            
            collectionExports.add(collectionExport);
        }
        export.setCollections(collectionExports);
        
        // Export saved requests without collection
        List<SavedRequest> unassignedRequests = savedRequestRepository.findByCollectionIsNullOrderByUpdatedAtDesc();
        export.setSavedRequests(unassignedRequests.stream().map(this::toSavedRequestExport).collect(Collectors.toList()));
        
        // Export environments
        List<Environment> environments = environmentRepository.findAll();
        export.setEnvironments(environments.stream().map(this::toEnvironmentExport).collect(Collectors.toList()));
        
        return export;
    }
    
    @Transactional
    public Map<String, Object> importBisenFormat(BisenExportDto importData) {
        Map<String, Object> result = new java.util.HashMap<>();
        int collectionsCreated = 0;
        int requestsCreated = 0;
        int environmentsCreated = 0;
        
        // Import environments
        // Note: isDefault field is deprecated - environments are now application-based
        // Import functionality for environments is limited without applicationId
        // Consider updating export format to include applicationId
        if (importData.getEnvironments() != null) {
            for (BisenExportDto.EnvironmentExportDto envExport : importData.getEnvironments()) {
                // Skip environments without applicationId - they cannot be imported in the new model
                // TODO: Update export format to include applicationId for proper import
                // For now, we'll skip these environments as they require an application
                // Environment env = new Environment();
                // env.setName(envExport.getName());
                // env.setDescription(envExport.getDescription());
                // env.setVariables(envExport.getVariables());
                // Note: isDefault is deprecated and not used anymore
                // environmentRepository.save(env);
                // environmentsCreated++;
            }
        }
        
        // Import collections
        if (importData.getCollections() != null) {
            for (BisenExportDto.CollectionExportDto collectionExport : importData.getCollections()) {
                Collection collection = new Collection();
                collection.setName(collectionExport.getName());
                collection.setDescription(collectionExport.getDescription());
                collection = collectionRepository.save(collection);
                collectionsCreated++;
                
                // Import requests in collection
                if (collectionExport.getRequests() != null) {
                    for (BisenExportDto.SavedRequestExportDto requestExport : collectionExport.getRequests()) {
                        SavedRequest request = toSavedRequest(requestExport);
                        request.setCollection(collection);
                        savedRequestRepository.save(request);
                        requestsCreated++;
                    }
                }
            }
        }
        
        // Import unassigned requests
        if (importData.getSavedRequests() != null) {
            for (BisenExportDto.SavedRequestExportDto requestExport : importData.getSavedRequests()) {
                SavedRequest request = toSavedRequest(requestExport);
                savedRequestRepository.save(request);
                requestsCreated++;
            }
        }
        
        result.put("collectionsCreated", collectionsCreated);
        result.put("requestsCreated", requestsCreated);
        result.put("environmentsCreated", environmentsCreated);
        return result;
    }
    
    private BisenExportDto.SavedRequestExportDto toSavedRequestExport(SavedRequest request) {
        BisenExportDto.SavedRequestExportDto export = new BisenExportDto.SavedRequestExportDto();
        export.setName(request.getName());
        export.setMethod(request.getMethod());
        export.setUrl(request.getUrl());
        export.setHeaders(request.getHeaders());
        export.setBody(request.getBody());
        return export;
    }
    
    private SavedRequest toSavedRequest(BisenExportDto.SavedRequestExportDto export) {
        SavedRequest request = new SavedRequest();
        request.setName(export.getName());
        request.setMethod(export.getMethod());
        request.setUrl(export.getUrl());
        request.setHeaders(export.getHeaders());
        request.setBody(export.getBody());
        return request;
    }
    
    private BisenExportDto.EnvironmentExportDto toEnvironmentExport(Environment env) {
        BisenExportDto.EnvironmentExportDto export = new BisenExportDto.EnvironmentExportDto();
        export.setName(env.getName());
        export.setDescription(env.getDescription());
        export.setVariables(env.getVariables());
        // Note: isDefault is deprecated - environments are now application-based
        // Set to false for backward compatibility with old export format
        export.setIsDefault(false);
        return export;
    }
}

