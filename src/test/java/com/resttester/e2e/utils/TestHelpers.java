/**
 * BISEN - API Testing Tool
 * Test Helper Utilities for E2E Tests
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.e2e.utils;

import com.microsoft.playwright.Page;

public class TestHelpers {
    
    public static void waitForDialog(Page page) {
        page.waitForTimeout(500); // Wait for dialog animation
    }
    
    public static void acceptDialog(Page page) {
        page.onDialog(dialog -> dialog.accept());
    }
    
    public static void dismissDialog(Page page) {
        page.onDialog(dialog -> dialog.dismiss());
    }
    
    public static void waitForApiCall(Page page) {
        page.waitForLoadState();
    }
    
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }
    
    public static void scrollToElement(Page page, String selector) {
        page.evaluate("document.querySelector('" + selector + "').scrollIntoView({ behavior: 'smooth', block: 'center' })");
        page.waitForTimeout(500);
    }
}

