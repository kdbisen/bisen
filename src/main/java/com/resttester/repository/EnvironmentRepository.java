/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.repository;

import com.resttester.model.Environment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnvironmentRepository extends JpaRepository<Environment, Long> {
    
    @EntityGraph(attributePaths = "application")
    List<Environment> findByApplicationIdOrderByNameAsc(Long applicationId);
    
    @EntityGraph(attributePaths = "application")
    Optional<Environment> findById(Long id);
    
    @EntityGraph(attributePaths = "application")
    List<Environment> findByApplicationIdOrderByCreatedAtAsc(Long applicationId);
    
    // Deprecated: Global environments no longer supported
    // List<Environment> findAllByOrderByCreatedAtAsc();
    // Optional<Environment> findByIsDefaultTrue();
    // Optional<Environment> findByName(String name);
}

