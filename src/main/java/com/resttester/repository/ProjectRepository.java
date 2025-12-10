/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.repository;

import com.resttester.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    @EntityGraph(attributePaths = {"applications"})
    List<Project> findAllByOrderByCreatedAtDesc();
    
    @EntityGraph(attributePaths = {"applications"})
    Optional<Project> findById(Long id);
    
    List<Project> findAllByOrderByNameAsc();
}


