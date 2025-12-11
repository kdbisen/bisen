/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.repository;

import com.resttester.model.ApiMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApiMonitorRepository extends JpaRepository<ApiMonitor, Long> {
    
    List<ApiMonitor> findByEnabledTrueOrderByNextCheckAtAsc();
    
    @Query("SELECT m FROM ApiMonitor m WHERE m.enabled = true AND m.nextCheckAt <= :now")
    List<ApiMonitor> findMonitorsDueForCheck(LocalDateTime now);
    
    List<ApiMonitor> findBySavedRequestId(Long savedRequestId);
    
    List<ApiMonitor> findByStatus(String status);
}

