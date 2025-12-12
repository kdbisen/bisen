/**
 * BISEN - API Testing Tool
 * Base Test Class for E2E Tests
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.e2e;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

public class BaseTest {
    
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    
    protected static final String BASE_URL = "http://localhost:2000";
    
    @BeforeAll
    public static void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
            .setHeadless(false) // Set to true for CI/CD
            .setSlowMo(50)); // Slow down operations for visibility
    }
    
    @AfterAll
    public static void tearDown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
    
    @BeforeEach
    public void setUpTest() {
        context = browser.newContext(new Browser.NewContextOptions()
            .setViewportSize(1920, 1080)
            .setRecordVideoDir(Paths.get("test-results/videos/"))
            .setRecordVideoSize(1920, 1080));
        
        page = context.newPage();
        page.navigate(BASE_URL);
        
        // Wait for page to load
        page.waitForLoadState();
    }
    
    @AfterEach
    public void tearDownTest() {
        if (context != null) {
            context.close();
        }
    }
    
    protected void takeScreenshot(String name) {
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(Paths.get("test-results/screenshots/" + name + ".png"))
            .setFullPage(true));
    }
}

