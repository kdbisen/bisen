/**
 * BISEN - API Testing Tool
 * Projects Page Object Model for E2E Tests
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.e2e.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ProjectsPage {
    
    private final Page page;
    
    // Locators
    private final Locator addProjectButton;
    private final Locator projectNameInput;
    private final Locator projectUrlInput;
    private final Locator projectDescriptionInput;
    private final Locator saveProjectButton;
    
    public ProjectsPage(Page page) {
        this.page = page;
        this.addProjectButton = page.locator("button:has-text('Add Project')");
        this.projectNameInput = page.locator("#projectName");
        this.projectUrlInput = page.locator("#projectUrl");
        this.projectDescriptionInput = page.locator("#projectDescription");
        this.saveProjectButton = page.locator("button:has-text('Save Project')");
    }
    
    public ProjectsPage navigate() {
        page.navigate("/projects");
        page.waitForLoadState();
        return this;
    }
    
    public ProjectsPage clickAddProject() {
        addProjectButton.click();
        return this;
    }
    
    public ProjectsPage enterProjectName(String name) {
        projectNameInput.fill(name);
        return this;
    }
    
    public ProjectsPage enterProjectUrl(String url) {
        projectUrlInput.fill(url);
        return this;
    }
    
    public ProjectsPage enterProjectDescription(String description) {
        projectDescriptionInput.fill(description);
        return this;
    }
    
    public ProjectsPage clickSaveProject() {
        saveProjectButton.click();
        return this;
    }
    
    public boolean isProjectVisible(String projectName) {
        return page.locator("text=" + projectName).isVisible();
    }
    
    public ProjectsPage waitForProjectToAppear(String projectName) {
        page.waitForSelector("text=" + projectName, new Page.WaitForSelectorOptions().setTimeout(10000));
        return this;
    }
}

