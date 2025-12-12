/**
 * BISEN - API Testing Tool
 * E2E Tests for Project Management Flow
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.e2e.tests;

import com.resttester.e2e.BaseTest;
import com.resttester.e2e.pages.HomePage;
import com.resttester.e2e.pages.ProjectsPage;
import com.resttester.e2e.utils.TestHelpers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectManagementTest extends BaseTest {
    
    @Test
    @DisplayName("Should create a new project")
    public void testCreateProject() {
        ProjectsPage projectsPage = new ProjectsPage(page);
        String projectName = "Test Project " + TestHelpers.generateRandomString(5);
        
        projectsPage.navigate()
            .clickAddProject()
            .enterProjectName(projectName)
            .enterProjectUrl("https://test.example.com")
            .enterProjectDescription("Test project description")
            .clickSaveProject();
        
        projectsPage.waitForProjectToAppear(projectName);
        
        assertTrue(projectsPage.isProjectVisible(projectName), "Project should be visible after creation");
    }
    
    @Test
    @DisplayName("Should navigate to projects page from home")
    public void testNavigateToProjects() {
        HomePage homePage = new HomePage(page);
        
        homePage.navigate();
        
        // Click on Projects link in navigation
        page.locator("a:has-text('Projects')").click();
        
        // Wait for projects page to load
        page.waitForURL("**/projects");
        
        assertTrue(page.url().contains("/projects"), "Should be on projects page");
    }
}

