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
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class LoggingConfig implements ApplicationListener<ApplicationReadyEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);
    
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            // Log application startup information
            String userHome = System.getProperty("user.home");
            String dbPath = userHome + "/.bisen-api-tester/bisen-api-tester.db";
            String logPath = userHome + "/.bisen-api-tester/bisen.log";
            
            logger.info("========================================");
            logger.info("BISEN API Testing Tool");
            logger.info("Version: 1.2 BETA");
            logger.info("Author: Kuldeep Bisen");
            logger.info("========================================");
            logger.info("Application started successfully!");
            logger.info("Database location: {}", dbPath);
            logger.info("Log file location: {}", logPath);
            logger.info("Access application at: http://localhost:2000");
            logger.info("========================================");
            
            // Verify log directory exists
            Path logDir = Paths.get(userHome, ".bisen-api-tester");
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
                logger.info("Created log directory: {}", logDir);
            }
            
        } catch (Exception e) {
            logger.error("Error during logging configuration: {}", e.getMessage(), e);
        }
    }
}


