/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
package com.resttester.dto;

import java.util.List;

public class BisenExportDto {
    private String version = "1.0";
    private String exportedAt;
    private List<CollectionExportDto> collections;
    private List<SavedRequestExportDto> savedRequests;
    private List<EnvironmentExportDto> environments;
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getExportedAt() {
        return exportedAt;
    }
    
    public void setExportedAt(String exportedAt) {
        this.exportedAt = exportedAt;
    }
    
    public List<CollectionExportDto> getCollections() {
        return collections;
    }
    
    public void setCollections(List<CollectionExportDto> collections) {
        this.collections = collections;
    }
    
    public List<SavedRequestExportDto> getSavedRequests() {
        return savedRequests;
    }
    
    public void setSavedRequests(List<SavedRequestExportDto> savedRequests) {
        this.savedRequests = savedRequests;
    }
    
    public List<EnvironmentExportDto> getEnvironments() {
        return environments;
    }
    
    public void setEnvironments(List<EnvironmentExportDto> environments) {
        this.environments = environments;
    }
    
    public static class CollectionExportDto {
        private String name;
        private String description;
        private List<SavedRequestExportDto> requests;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public List<SavedRequestExportDto> getRequests() {
            return requests;
        }
        
        public void setRequests(List<SavedRequestExportDto> requests) {
            this.requests = requests;
        }
    }
    
    public static class SavedRequestExportDto {
        private String name;
        private String method;
        private String url;
        private String headers;
        private String body;
        private String description;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getMethod() {
            return method;
        }
        
        public void setMethod(String method) {
            this.method = method;
        }
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
        
        public String getHeaders() {
            return headers;
        }
        
        public void setHeaders(String headers) {
            this.headers = headers;
        }
        
        public String getBody() {
            return body;
        }
        
        public void setBody(String body) {
            this.body = body;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
    }
    
    public static class EnvironmentExportDto {
        private String name;
        private String description;
        private String variables;
        private Boolean isDefault;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public String getVariables() {
            return variables;
        }
        
        public void setVariables(String variables) {
            this.variables = variables;
        }
        
        public Boolean getIsDefault() {
            return isDefault;
        }
        
        public void setIsDefault(Boolean isDefault) {
            this.isDefault = isDefault;
        }
    }
}

