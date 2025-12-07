/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.repository;

import com.resttester.model.ApiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApiRequestRepository extends JpaRepository<ApiRequest, Long> {
    List<ApiRequest> findAllByOrderByCreatedAtDesc();
    List<ApiRequest> findByCollectionIdOrderByCreatedAtDesc(Long collectionId);
    List<ApiRequest> findByCollectionIsNullOrderByCreatedAtDesc();
}

