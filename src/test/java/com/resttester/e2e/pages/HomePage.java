/**
 * BISEN - API Testing Tool
 * Home Page Object Model for E2E Tests
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.e2e.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage {
    
    private final Page page;
    
    // Locators
    private final Locator methodSelect;
    private final Locator urlInput;
    private final Locator sendButton;
    private final Locator saveButton;
    private final Locator requestNameInput;
    private final Locator responseSection;
    private final Locator statusBadge;
    private final Locator responseBody;
    private final Locator loadingIndicator;
    
    public HomePage(Page page) {
        this.page = page;
        this.methodSelect = page.locator("#method");
        this.urlInput = page.locator("#url");
        this.sendButton = page.locator("button:has-text('Send')");
        this.saveButton = page.locator("button:has-text('Save')");
        this.requestNameInput = page.locator("#name");
        this.responseSection = page.locator("#responseSection");
        this.statusBadge = page.locator("#statusBadge");
        this.responseBody = page.locator("#responseBody");
        this.loadingIndicator = page.locator("#loading");
    }
    
    public HomePage navigate() {
        page.navigate("/");
        page.waitForLoadState();
        return this;
    }
    
    public HomePage selectMethod(String method) {
        methodSelect.selectOption(method);
        return this;
    }
    
    public HomePage enterUrl(String url) {
        urlInput.fill(url);
        return this;
    }
    
    public HomePage enterRequestName(String name) {
        requestNameInput.fill(name);
        return this;
    }
    
    public HomePage clickSend() {
        sendButton.click();
        return this;
    }
    
    public HomePage clickSave() {
        saveButton.click();
        return this;
    }
    
    public boolean isResponseVisible() {
        return responseSection.isVisible();
    }
    
    public String getStatusBadgeText() {
        return statusBadge.textContent();
    }
    
    public String getResponseBodyText() {
        return responseBody.textContent();
    }
    
    public boolean isLoadingVisible() {
        return loadingIndicator.isVisible();
    }
    
    public HomePage waitForResponse() {
        page.waitForSelector("#responseSection:visible", new Page.WaitForSelectorOptions().setTimeout(30000));
        return this;
    }
    
    public HomePage waitForLoadingToComplete() {
        page.waitForSelector("#loading", new Page.WaitForSelectorOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN).setTimeout(30000));
        return this;
    }
    
    public HomePage addHeader(String key, String value) {
        // Click add header button
        page.locator("button:has-text('+ Add Header')").click();
        
        // Find the last header row and fill it
        Locator headerRows = page.locator(".header-row");
        int count = headerRows.count();
        Locator lastRow = headerRows.nth(count - 1);
        lastRow.locator(".header-key").fill(key);
        lastRow.locator(".header-value").fill(value);
        
        return this;
    }
    
    public HomePage enterBody(String body) {
        page.locator("#body").fill(body);
        return this;
    }
    
    public HomePage selectBodyType(String bodyType) {
        page.locator("#bodyType").selectOption(bodyType);
        return this;
    }
}

