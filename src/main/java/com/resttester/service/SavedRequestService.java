/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.service;

import com.resttester.dto.SavedRequestDto;
import com.resttester.model.Application;
import com.resttester.model.Collection;
import com.resttester.model.SavedRequest;
import com.resttester.repository.ApplicationRepository;
import com.resttester.repository.CollectionRepository;
import com.resttester.repository.SavedRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedRequestService {
    
    private static final Logger logger = LoggerFactory.getLogger(SavedRequestService.class);
    
    private final SavedRequestRepository repository;
    private final CollectionRepository collectionRepository;
    private final ApplicationRepository applicationRepository;
    
    public SavedRequestService(SavedRequestRepository repository, CollectionRepository collectionRepository, ApplicationRepository applicationRepository) {
        this.repository = repository;
        this.collectionRepository = collectionRepository;
        this.applicationRepository = applicationRepository;
    }
    
    public SavedRequest saveRequest(SavedRequestDto dto) {
        logger.info("Saving request: name={}, method={}, url={}, collectionId={}, applicationId={}", 
            dto.getName(), dto.getMethod(), dto.getUrl(), dto.getCollectionId(), dto.getApplicationId());
        
        SavedRequest savedRequest;
        
        if (dto.getId() != null) {
            // Update existing
            savedRequest = repository.findById(dto.getId()).orElse(new SavedRequest());
            logger.debug("Updating existing saved request with ID: {}", dto.getId());
        } else {
            // Create new
            savedRequest = new SavedRequest();
            logger.debug("Creating new saved request");
        }
        
        savedRequest.setName(dto.getName());
        savedRequest.setMethod(dto.getMethod());
        savedRequest.setUrl(dto.getUrl());
        savedRequest.setHeaders(dto.getHeaders());
        savedRequest.setBody(dto.getBody());
        
        if (dto.getCollectionId() != null) {
            Collection collection = collectionRepository.findById(dto.getCollectionId()).orElse(null);
            if (collection != null) {
                savedRequest.setCollection(collection);
                logger.debug("Assigned to collection: {}", collection.getName());
            } else {
                logger.warn("Collection with ID {} not found", dto.getCollectionId());
                savedRequest.setCollection(null);
            }
        } else {
            savedRequest.setCollection(null);
        }
        
        if (dto.getApplicationId() != null) {
            Application application = applicationRepository.findById(dto.getApplicationId()).orElse(null);
            if (application != null) {
                savedRequest.setApplication(application);
                logger.info("Assigned to application: {} (Project: {})", application.getName(), 
                    application.getProject() != null ? application.getProject().getName() : "Unknown");
            } else {
                logger.warn("Application with ID {} not found", dto.getApplicationId());
                savedRequest.setApplication(null);
            }
        } else {
            savedRequest.setApplication(null);
        }
        
        SavedRequest saved = repository.save(savedRequest);
        logger.info("Saved request successfully with ID: {}", saved.getId());
        return saved;
    }
    
    public List<SavedRequest> getAllSavedRequests() {
        return repository.findAllByOrderByUpdatedAtDesc();
    }
    
    public SavedRequest getSavedRequestById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    public void deleteSavedRequest(Long id) {
        repository.deleteById(id);
    }
    
    public List<SavedRequest> getSavedRequestsByCollection(Long collectionId) {
        return repository.findByCollectionIdOrderByUpdatedAtDesc(collectionId);
    }
    
    public List<SavedRequest> getSavedRequestsWithoutCollection() {
        return repository.findByCollectionIsNullOrderByUpdatedAtDesc();
    }
    
    public List<SavedRequest> getSavedRequestsByApplication(Long applicationId) {
        return repository.findByApplicationIdOrderByUpdatedAtDesc(applicationId);
    }
    
    public List<SavedRequest> getSavedRequestsWithoutApplication() {
        return repository.findByApplicationIsNullOrderByUpdatedAtDesc();
    }
}

