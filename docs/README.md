# BISEN - API Testing Tool

**Version:** 1.2 BETA  
**Author:** Kuldeep Bisen  
**Tagline:** Simple Postman Alternative, Clean to Test APIs

---

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Getting Started](#getting-started)
4. [User Guide](#user-guide)
5. [API Reference](#api-reference)
6. [Configuration](#configuration)
7. [Troubleshooting](#troubleshooting)
8. [Contributing](#contributing)
9. [License](#license)

---

## Overview

BISEN is a powerful, elegant, and simple API testing tool built with Spring Boot. It provides a clean interface for testing REST APIs with support for all HTTP methods, environment management, collections, and more.

### Key Highlights

- ✅ **Simple & Clean UI** - Minimalist design focused on productivity
- ✅ **All HTTP Methods** - GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS
- ✅ **Environment Management** - Dev, Staging, Prod environments with variables
- ✅ **Collections** - Organize requests into collections
- ✅ **Request History** - Track all API calls
- ✅ **Export/Import** - Share collections with team members
- ✅ **Security** - Built-in SSRF protection, input validation, rate limiting
- ✅ **SQLite Database** - Local file-based storage

---

## Features

### Core Features

- **HTTP Request Builder**
  - All HTTP methods support
  - Custom headers management
  - Multiple body types (JSON, XML, Form Data, etc.)
  - Request timeout configuration
  - SSL certificate support

- **Environment Management**
  - Create multiple environments (Dev, Staging, Prod)
  - Environment-specific variables
  - Variable substitution with `{{variableName}}` syntax
  - Preview variable substitution before sending

- **Collections & Organization**
  - Create collections to group related requests
  - Hierarchical sidebar navigation
  - Search and filter requests
  - Duplicate requests

- **Request History**
  - Automatic history tracking
  - View past requests and responses
  - Response time tracking
  - Status code visualization

- **Export/Import**
  - Export all data in BISEN format
  - Import collections and environments
  - Share with team members

- **Security Features**
  - SSRF protection (blocks internal IPs)
  - Input validation
  - Rate limiting
  - Security headers
  - Sensitive data masking

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Installation

1. **Clone or download the project**
   ```bash
   cd rest-api-tester
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   Open your browser and navigate to:
   ```
   http://localhost:8080
   ```

### Database

BISEN uses SQLite for data storage. The database file is automatically created at:
```
~/.bisen-api-tester/bisen-api-tester.db
```

---

## User Guide

### Making Your First Request

1. **Select HTTP Method**
   - Choose from GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS

2. **Enter URL**
   - Enter the API endpoint URL
   - Example: `https://api.example.com/users`

3. **Add Headers (Optional)**
   - Click "Add Header" to add custom headers
   - Content-Type is automatically set based on body type

4. **Add Body (Optional)**
   - Select body type (JSON, XML, Form Data, etc.)
   - Enter the request body

5. **Send Request**
   - Click "Send Request" button
   - View response in the response section

### Environment Management

1. **Create Environment**
   - Click "Environments" in the navigation menu
   - Click "+ New Environment"
   - Enter name, description, and variables (JSON format)

2. **Set Variables**
   ```json
   {
     "baseUrl": "https://api.example.com",
     "apiKey": "your-api-key",
     "userId": "123"
   }
   ```

3. **Use Variables**
   - Select environment from dropdown
   - Use `{{variableName}}` in URL, headers, or body
   - Example: `{{baseUrl}}/users/{{userId}}`

4. **Preview Substitution**
   - Click "Preview" button to see how variables will be replaced

### Collections

1. **Create Collection**
   - Go to "Collections" page
   - Click "+ New Collection"
   - Enter name and description

2. **Add Requests to Collection**
   - Right-click on a saved request
   - Select collection from context menu
   - Or use the "+" button on hover

3. **View Collection**
   - Click on collection name in sidebar
   - All requests in the collection will be displayed

### Saving Requests

1. **Save Request**
   - Fill in the request form
   - Click "Save Request" button
   - Enter a name for the request

2. **Load Saved Request**
   - Click on request name in sidebar
   - Request details will be loaded into the form

3. **Duplicate Request**
   - Click the duplicate button (⧉) next to saved request
   - Request will be loaded with "(Copy)" suffix

### Export/Import

1. **Export Data**
   - Click "Export" in navigation menu
   - All collections, requests, and environments will be exported
   - File will be downloaded as `bisen-export-YYYY-MM-DD.json`

2. **Import Data**
   - Click "Import" in navigation menu
   - Select BISEN export file
   - Confirm import
   - All data will be imported

### Swagger Import

1. **Import from URL**
   - Go to "Import Swagger" page
   - Enter Swagger/OpenAPI URL
   - Click "Import from URL"

2. **Import from File**
   - Go to "Import Swagger" page
   - Click "Choose File"
   - Select Swagger/OpenAPI JSON/YAML file
   - Click "Import from File"

---

## API Reference

### REST Endpoints

#### Execute Request
```
POST /api/execute
Content-Type: application/json

{
  "method": "GET",
  "url": "https://api.example.com/users",
  "headers": "Content-Type: application/json",
  "body": "",
  "variables": "{\"apiKey\": \"123\"}",
  "timeout": 30000
}
```

#### Collections
- `GET /api/collections` - Get all collections
- `POST /api/collections` - Create collection
- `PUT /api/collections/{id}` - Update collection
- `DELETE /api/collections/{id}` - Delete collection

#### Saved Requests
- `GET /api/saved` - Get all saved requests
- `GET /api/saved/{id}` - Get saved request by ID
- `POST /api/saved` - Save request
- `DELETE /api/saved/{id}` - Delete saved request

#### Environments
- `GET /api/environments` - Get all environments
- `POST /api/environments` - Create environment
- `PUT /api/environments/{id}` - Update environment
- `DELETE /api/environments/{id}` - Delete environment
- `POST /api/environments/{id}/set-default` - Set default environment

#### Export/Import
- `GET /api/export` - Export all data
- `POST /api/import/bisen` - Import BISEN format

---

## Configuration

### Application Properties

Edit `src/main/resources/application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:sqlite:${user.home}/.bisen-api-tester/bisen-api-tester.db
    driver-class-name: org.sqlite.JDBC
  jpa:
    database-platform: org.hibernate.community.dialect.SQLiteDialect
    hibernate:
      ddl-auto: update
```

### Database Location

Default database location:
```
~/.bisen-api-tester/bisen-api-tester.db
```

To change location, update `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:sqlite:/path/to/your/database.db
```

---

## Troubleshooting

### Database Issues

**Problem:** Database file not found  
**Solution:** The database directory is created automatically on first run. Ensure you have write permissions.

**Problem:** Database locked  
**Solution:** Close any other instances of BISEN that might be accessing the database.

### Port Already in Use

**Problem:** Port 8080 already in use  
**Solution:** Change port in `application.yml`:
```yaml
server:
  port: 8081
```

### Import Errors

**Problem:** Import fails with JSON parse error  
**Solution:** Ensure the file is a valid BISEN export format. Check file extension is `.json`.

---

## Contributing

BISEN is developed by Kuldeep Bisen. All code belongs to the author.

For questions or issues, please contact the author.

---

## License

Copyright © 2024 Kuldeep Bisen. All rights reserved.

This software and associated documentation files (the "Software") are proprietary and confidential. Unauthorized copying, modification, distribution, or use of this Software, via any medium, is strictly prohibited.

---

## Author

**Kuldeep Bisen**  
Developer & Creator of BISEN

---

*BISEN - Simple Postman Alternative, Clean to Test APIs*  
*Version 1.2 BETA*


