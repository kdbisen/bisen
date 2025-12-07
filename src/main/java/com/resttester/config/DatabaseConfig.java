/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;

@Configuration
public class DatabaseConfig implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        try {
            Environment environment = event.getEnvironment();
            
            // Get the datasource URL with property resolution
            String datasourceUrl = environment.getProperty("spring.datasource.url");
            
            if (datasourceUrl == null || datasourceUrl.isEmpty()) {
                logger.warn("Warning: spring.datasource.url is not configured");
                return;
            }
            
            // Extract directory path from JDBC URL
            // Format: jdbc:sqlite:/path/to/database.db
            String dbPath = datasourceUrl.replace("jdbc:sqlite:", "");
            File dbFile = new File(dbPath);
            File dbDirectory = dbFile.getParentFile();
            
            if (dbDirectory != null && !dbDirectory.exists()) {
                boolean created = dbDirectory.mkdirs();
                if (created) {
                    logger.info("Created database directory: {}", dbDirectory.getAbsolutePath());
                } else {
                    logger.error("Failed to create database directory: {}", dbDirectory.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            logger.error("Error ensuring database directory exists: {}", e.getMessage(), e);
        }
    }
}

