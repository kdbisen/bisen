# BISEN Project Structure

**Author:** Kuldeep Bisen  
**Version:** 1.2 BETA

---

## Project Overview

BISEN is a Spring Boot application for API testing with a clean, minimalist UI.

---

## Directory Structure

```
rest-api-tester/
├── docs/                          # Documentation folder
│   ├── INDEX.md                   # Documentation index
│   ├── README.md                  # Main documentation
│   ├── QUICK_START.md             # Quick start guide
│   ├── INSTALLATION.md            # Installation guide
│   ├── FEATURES.md                # Feature documentation
│   ├── API_REFERENCE.md           # API reference
│   ├── CHANGELOG.md               # Version history
│   └── PROJECT_STRUCTURE.md       # This file
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/resttester/
│   │   │       ├── RestApiTesterApplication.java
│   │   │       ├── controller/    # REST controllers
│   │   │       ├── service/        # Business logic
│   │   │       ├── repository/    # Data access
│   │   │       ├── model/         # Entity models
│   │   │       ├── dto/           # Data transfer objects
│   │   │       ├── config/        # Configuration classes
│   │   │       └── security/      # Security components
│   │   │
│   │   └── resources/
│   │       ├── templates/         # Thymeleaf templates
│   │       │   ├── index.html
│   │       │   ├── history.html
│   │       │   ├── collections.html
│   │       │   ├── saved-requests.html
│   │       │   └── import-swagger.html
│   │       │
│   │       ├── static/            # Static resources
│   │       │   ├── icon.svg
│   │       │   ├── dialog.js
│   │       │   ├── dialog.css
│   │       │   ├── json-formatter.js
│   │       │   └── syntax-highlight.css
│   │       │
│   │       └── application.yml   # Application configuration
│   │
│   └── test/                      # Test files
│
├── pom.xml                        # Maven configuration
├── README.md                      # Project README
└── add-author-headers.sh          # Script to add author headers

```

---

## Key Components

### Controllers
- `ApiTesterController` - Main REST API endpoints

### Services
- `ApiRequestService` - Request execution logic
- `CollectionService` - Collection management
- `SavedRequestService` - Saved request management
- `EnvironmentService` - Environment management
- `ExportImportService` - Export/import functionality
- `SwaggerImportService` - Swagger import

### Models
- `ApiRequest` - Request history entity
- `Collection` - Collection entity
- `SavedRequest` - Saved request entity
- `Environment` - Environment entity

### Security
- `SecurityConfig` - Spring Security configuration
- `SecurityValidator` - Input validation and SSRF protection
- `RateLimitingFilter` - Rate limiting

### Configuration
- `DatabaseConfig` - Database initialization
- `HibernateConfig` - Hibernate configuration
- `JacksonConfig` - JSON serialization
- `RestTemplateConfig` - HTTP client configuration

---

## Database

**Type:** SQLite  
**Location:** `~/.bisen-api-tester/bisen-api-tester.db`

**Tables:**
- `api_requests` - Request history
- `collections` - Collections
- `saved_requests` - Saved requests
- `environments` - Environments

---

## Build & Run

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Or use executable JAR
java -jar target/rest-api-tester-1.0.0.jar
```

---

## Code Attribution

All code files include author header:
```java
/**
 * BISEN - API Testing Tool
 * Simple Postman Alternative, Clean to Test APIs
 * 
 * @author Kuldeep Bisen
 * @version 1.2 BETA
 */
```

---

*Project Structure Documentation by Kuldeep Bisen*


