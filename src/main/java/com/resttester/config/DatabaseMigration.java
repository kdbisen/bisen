/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.config;

import com.resttester.model.Application;
import com.resttester.model.Project;
import com.resttester.repository.ApplicationRepository;
import com.resttester.repository.EnvironmentRepository;
import com.resttester.repository.ProjectRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Database migration component to handle schema changes
 * Runs on application startup to migrate existing databases
 */
@Component
@Order(1) // Run before DataInitializer
public class DatabaseMigration {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseMigration.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private EnvironmentRepository environmentRepository;
    
    @PostConstruct
    @Transactional
    public void migrate() {
        try {
            migrateEnvironmentsTable();
        } catch (Exception e) {
            logger.error("Database migration failed: {}", e.getMessage(), e);
            // Don't throw - allow application to start even if migration fails
            // User can manually fix the database
        }
    }
    
    private void migrateEnvironmentsTable() {
        try {
            // Check if is_default column exists (old column that needs to be removed)
            boolean isDefaultExists = checkColumnExists("environments", "is_default");
            
            // Check if application_id column exists
            boolean applicationIdExists = checkColumnExists("environments", "application_id");
            
            // If is_default exists, we need to remove it by recreating the table
            if (isDefaultExists || !applicationIdExists) {
                logger.info("Migrating environments table: removing is_default column and ensuring application_id column...");
                
                // Recreate table without is_default column
                recreateEnvironmentsTableWithoutIsDefault(applicationIdExists);
                
                logger.info("Environments table migration completed successfully");
            } else {
                logger.debug("Environments table is already up to date, skipping migration");
            }
        } catch (Exception e) {
            logger.error("Error migrating environments table: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    private boolean checkColumnExists(String tableName, String columnName) {
        try {
            // Use PRAGMA table_info to check if column exists
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                "PRAGMA table_info(" + tableName + ")"
            );
            
            for (Map<String, Object> column : columns) {
                String name = (String) column.get("name");
                if (columnName.equalsIgnoreCase(name)) {
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            // If table doesn't exist or error occurs, assume column doesn't exist
            logger.debug("Error checking column existence: {}", e.getMessage());
            return false;
        }
    }
    
    private Application getOrCreateDefaultApplication() {
        // Try to find an existing application
        List<Application> applications = applicationRepository.findAll();
        
        if (!applications.isEmpty()) {
            // Use the first available application
            return applications.get(0);
        }
        
        // Need to create a default application, but it requires a project
        Project defaultProject = getOrCreateDefaultProject();
        
        // Create a default application
        Application defaultApp = new Application();
        defaultApp.setName("Default Application");
        defaultApp.setDescription("Default application created during migration");
        defaultApp.setProject(defaultProject);
        defaultApp = applicationRepository.save(defaultApp);
        logger.info("Created default application with ID: {} under project ID: {}", 
                   defaultApp.getId(), defaultProject.getId());
        
        return defaultApp;
    }
    
    private Project getOrCreateDefaultProject() {
        // Try to find an existing project
        List<Project> projects = projectRepository.findAll();
        
        if (!projects.isEmpty()) {
            // Use the first available project
            return projects.get(0);
        }
        
        // Create a default project
        Project defaultProject = new Project();
        defaultProject.setName("Default Project");
        defaultProject.setDescription("Default project created during migration");
        defaultProject = projectRepository.save(defaultProject);
        logger.info("Created default project with ID: {}", defaultProject.getId());
        
        return defaultProject;
    }
    
    private void recreateEnvironmentsTableWithoutIsDefault(boolean applicationIdExists) {
        try {
            // SQLite doesn't support ALTER COLUMN or DROP COLUMN, so we need to:
            // 1. Create new table without is_default column
            // 2. Copy data (excluding is_default)
            // 3. Drop old table
            // 4. Rename new table
            
            logger.info("Recreating environments table without is_default column...");
            
            // Create new table without is_default
            // SQLite uses INTEGER PRIMARY KEY AUTOINCREMENT, not BIGINT
            jdbcTemplate.execute("""
                CREATE TABLE environments_new (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name VARCHAR(255) NOT NULL,
                    description TEXT,
                    variables TEXT,
                    application_id INTEGER NOT NULL,
                    created_at TIMESTAMP NOT NULL,
                    updated_at TIMESTAMP NOT NULL,
                    FOREIGN KEY (application_id) REFERENCES applications(id)
                )
            """);
            
            // Copy data - handle both cases: with and without application_id
            if (applicationIdExists) {
                // Copy all data except is_default
                jdbcTemplate.execute("""
                    INSERT INTO environments_new (id, name, description, variables, application_id, created_at, updated_at)
                    SELECT id, name, description, variables, application_id, created_at, updated_at
                    FROM environments
                """);
            } else {
                // Need to assign environments to a default application
                Application defaultApplication = getOrCreateDefaultApplication();
                
                // Copy data and assign to default application
                jdbcTemplate.update("""
                    INSERT INTO environments_new (id, name, description, variables, application_id, created_at, updated_at)
                    SELECT id, name, description, variables, ?, created_at, updated_at
                    FROM environments
                """, defaultApplication.getId());
                
                logger.info("Assigned existing environments to default application");
            }
            
            // Drop old table
            jdbcTemplate.execute("DROP TABLE environments");
            
            // Rename new table
            jdbcTemplate.execute("ALTER TABLE environments_new RENAME TO environments");
            
            logger.info("Successfully recreated environments table without is_default column");
        } catch (Exception e) {
            logger.error("Error recreating table: {}", e.getMessage(), e);
            // If recreation fails, log the error but don't crash
            logger.warn("Table recreation failed. You may need to manually fix the database schema.");
        }
    }
}

