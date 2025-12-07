/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.service;

import com.resttester.dto.SavedRequestDto;
import com.resttester.model.Collection;
import com.resttester.model.SavedRequest;
import com.resttester.repository.CollectionRepository;
import com.resttester.repository.SavedRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedRequestService {
    
    private final SavedRequestRepository repository;
    private final CollectionRepository collectionRepository;
    
    public SavedRequestService(SavedRequestRepository repository, CollectionRepository collectionRepository) {
        this.repository = repository;
        this.collectionRepository = collectionRepository;
    }
    
    public SavedRequest saveRequest(SavedRequestDto dto) {
        SavedRequest savedRequest;
        
        if (dto.getId() != null) {
            // Update existing
            savedRequest = repository.findById(dto.getId()).orElse(new SavedRequest());
        } else {
            // Create new
            savedRequest = new SavedRequest();
        }
        
        savedRequest.setName(dto.getName());
        savedRequest.setMethod(dto.getMethod());
        savedRequest.setUrl(dto.getUrl());
        savedRequest.setHeaders(dto.getHeaders());
        savedRequest.setBody(dto.getBody());
        
        if (dto.getCollectionId() != null) {
            Collection collection = collectionRepository.findById(dto.getCollectionId()).orElse(null);
            savedRequest.setCollection(collection);
        } else {
            savedRequest.setCollection(null);
        }
        
        return repository.save(savedRequest);
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
}

