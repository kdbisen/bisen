/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.repository;

import com.resttester.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    @EntityGraph(attributePaths = {"project", "savedRequests"})
    List<Application> findByProjectIdOrderByCreatedAtDesc(Long projectId);
    
    @EntityGraph(attributePaths = {"project"})
    Optional<Application> findById(Long id);
    
    @EntityGraph(attributePaths = {"project"})
    List<Application> findAllByOrderByCreatedAtDesc();
    
    // Note: Cannot fetch both savedRequests and environments simultaneously (MultipleBagFetchException)
    // Fetch savedRequests here, environments will be loaded separately when needed
    @EntityGraph(attributePaths = {"project", "savedRequests"})
    List<Application> findByProjectIdOrderByNameAsc(Long projectId);
}

