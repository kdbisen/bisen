/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.repository;

import com.resttester.model.SavedRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SavedRequestRepository extends JpaRepository<SavedRequest, Long> {
    
    @EntityGraph(attributePaths = {"collection", "application", "application.project"})
    List<SavedRequest> findAllByOrderByUpdatedAtDesc();
    
    @EntityGraph(attributePaths = {"collection", "application", "application.project"})
    Optional<SavedRequest> findById(Long id);
    
    @EntityGraph(attributePaths = {"collection", "application", "application.project"})
    List<SavedRequest> findByCollectionIdOrderByUpdatedAtDesc(Long collectionId);
    
    @EntityGraph(attributePaths = {"collection", "application", "application.project"})
    List<SavedRequest> findByApplicationIdOrderByUpdatedAtDesc(Long applicationId);
    
    List<SavedRequest> findByCollectionIsNullOrderByUpdatedAtDesc();
    
    List<SavedRequest> findByApplicationIsNullOrderByUpdatedAtDesc();
}

