/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.service;

import com.resttester.dto.CollectionDto;
import com.resttester.model.Collection;
import com.resttester.repository.CollectionRepository;
import com.resttester.repository.SavedRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {
    
    private final CollectionRepository repository;
    private final SavedRequestRepository savedRequestRepository;
    
    public CollectionService(CollectionRepository repository, SavedRequestRepository savedRequestRepository) {
        this.repository = repository;
        this.savedRequestRepository = savedRequestRepository;
    }
    
    public Collection createCollection(CollectionDto collectionDto) {
        Collection collection = new Collection();
        collection.setName(collectionDto.getName());
        collection.setDescription(collectionDto.getDescription());
        return repository.save(collection);
    }
    
    public List<Collection> getAllCollections() {
        return repository.findAllByOrderByCreatedAtDesc();
    }
    
    public Collection getCollectionById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    public Collection updateCollection(Long id, CollectionDto collectionDto) {
        Collection collection = repository.findById(id).orElse(null);
        if (collection != null) {
            collection.setName(collectionDto.getName());
            collection.setDescription(collectionDto.getDescription());
            return repository.save(collection);
        }
        return null;
    }
    
    public void deleteCollection(Long id) {
        repository.deleteById(id);
    }
}

