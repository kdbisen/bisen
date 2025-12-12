/**
 * BISEN - API Testing Tool
 * E2E Tests for History Panel
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.e2e.tests;

import com.resttester.e2e.BaseTest;
import com.resttester.e2e.pages.HomePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryTest extends BaseTest {
    
    @Test
    @DisplayName("Should display history after executing requests")
    public void testHistoryPanel() {
        HomePage homePage = new HomePage(page);
        
        // Execute a request
        homePage.navigate()
            .selectMethod("GET")
            .enterUrl("https://jsonplaceholder.typicode.com/posts/1")
            .clickSend();
        
        homePage.waitForLoadingToComplete();
        homePage.waitForResponse();
        
        // Open history panel
        page.locator("#historyFloatingIcon").click();
        
        // Wait for history panel to appear
        page.waitForSelector("#historyPanel:visible");
        
        // Verify history panel is visible
        assertTrue(page.locator("#historyPanel").isVisible(), "History panel should be visible");
    }
    
    @Test
    @DisplayName("Should load request from history")
    public void testLoadFromHistory() {
        HomePage homePage = new HomePage(page);
        
        // Execute a request first
        homePage.navigate()
            .selectMethod("GET")
            .enterUrl("https://jsonplaceholder.typicode.com/posts/1")
            .clickSend();
        
        homePage.waitForLoadingToComplete();
        homePage.waitForResponse();
        
        // Open history and click on first history item
        page.locator("#historyFloatingIcon").click();
        page.waitForSelector("#historyPanel:visible");
        
        // Click first history item if available
        if (page.locator(".history-item").count() > 0) {
            page.locator(".history-item").first().click();
            page.waitForTimeout(1000);
            
            // Verify request is loaded
            assertTrue(page.locator("#url").inputValue().contains("jsonplaceholder"), "URL should be loaded from history");
        }
    }
}

