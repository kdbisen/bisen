# BISEN API Tester - Technical Architecture Documentation

**Version:** 1.2 BETA  
**Author:** Kuldeep Bisen  
**Last Updated:** December 2024

---

## Table of Contents

1. [System Overview](#system-overview)
2. [High-Level Architecture](#high-level-architecture)
3. [Technology Stack](#technology-stack)
4. [Component Architecture](#component-architecture)
5. [Data Flow Diagrams](#data-flow-diagrams)
6. [Database Schema](#database-schema)
7. [Request Processing Flow](#request-processing-flow)
8. [Security Architecture](#security-architecture)
9. [Deployment Architecture](#deployment-architecture)

---

## System Overview

BISEN (BISEN - Powerful, Elegant & Simple) is a REST API testing tool built as a Spring Boot application. It provides a web-based interface for testing APIs, managing projects, applications, and API requests with environment variable support.

### Key Features
- REST API request execution (GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS)
- Project > Application > API hierarchy
- Environment variable management
- Request history tracking
- Swagger/OpenAPI import
- API monitoring and health checks
- Request scripting (pre/post-request scripts)
- Export/Import functionality

---

## High-Level Architecture

```mermaid
graph TB
    subgraph "Client Layer"
        Browser[Web Browser]
        UI[Thymeleaf Templates]
        JS[JavaScript/Alpine.js]
        CSS[Tailwind CSS]
    end
    
    subgraph "Presentation Layer"
        Controller[ApiTesterController]
        Security[Security Filters]
        RateLimit[Rate Limiting Filter]
    end
    
    subgraph "Business Logic Layer"
        SavedRequestSvc[SavedRequestService]
        ProjectSvc[ProjectService]
        ApplicationSvc[ApplicationService]
        EnvironmentSvc[EnvironmentService]
        ApiRequestSvc[ApiRequestService]
        ApiMonitorSvc[ApiMonitorService]
        SwaggerSvc[SwaggerImportService]
        ExportSvc[ExportImportService]
    end
    
    subgraph "Data Access Layer"
        SavedRequestRepo[SavedRequestRepository]
        ProjectRepo[ProjectRepository]
        ApplicationRepo[ApplicationRepository]
        EnvironmentRepo[EnvironmentRepository]
        ApiRequestRepo[ApiRequestRepository]
        ApiMonitorRepo[ApiMonitorRepository]
    end
    
    subgraph "Data Layer"
        SQLite[(SQLite Database)]
        Cache[In-Memory Cache]
    end
    
    subgraph "External Services"
        RestTemplate[RestTemplate]
        ExternalAPI[External APIs]
    end
    
    Browser -->|HTTP Requests| Controller
    Controller -->|Process| Security
    Security -->|Validate| RateLimit
    RateLimit -->|Route| SavedRequestSvc
    RateLimit -->|Route| ProjectSvc
    RateLimit -->|Route| ApplicationSvc
    RateLimit -->|Route| EnvironmentSvc
    RateLimit -->|Route| ApiRequestSvc
    RateLimit -->|Route| ApiMonitorSvc
    RateLimit -->|Route| SwaggerSvc
    RateLimit -->|Route| ExportSvc
    
    SavedRequestSvc --> SavedRequestRepo
    ProjectSvc --> ProjectRepo
    ApplicationSvc --> ApplicationRepo
    EnvironmentSvc --> EnvironmentRepo
    ApiRequestSvc --> ApiRequestRepo
    ApiMonitorSvc --> ApiMonitorRepo
    
    SavedRequestRepo --> SQLite
    ProjectRepo --> SQLite
    ApplicationRepo --> SQLite
    EnvironmentRepo --> SQLite
    ApiRequestRepo --> SQLite
    ApiMonitorRepo --> SQLite
    
    ApiRequestSvc --> Cache
    ProjectSvc --> Cache
    
    ApiRequestSvc --> RestTemplate
    RestTemplate --> ExternalAPI
    
    UI -->|Render| Browser
    JS -->|Interact| Browser
    CSS -->|Style| Browser
```

---

## Technology Stack

### Backend Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17 | Core programming language |
| **Spring Boot** | 3.2.0 | Application framework and dependency injection |
| **Spring Web** | 3.2.0 | REST API and web MVC support |
| **Spring Security** | 3.2.0 | Authentication and authorization |
| **Spring Data JPA** | 3.2.0 | Database abstraction and ORM |
| **Hibernate** | 6.x | JPA implementation and ORM |
| **SQLite** | 3.44.1.0 | Embedded database for data persistence |
| **HikariCP** | Built-in | Connection pooling for database |
| **Jackson** | Built-in | JSON/YAML serialization and deserialization |
| **Thymeleaf** | Built-in | Server-side template engine for HTML |
| **Swagger Parser** | 2.1.16 | Parse Swagger/OpenAPI specifications |
| **SnakeYAML** | Built-in | YAML parsing for Swagger imports |
| **Spring Cache** | Built-in | In-memory caching layer |

### Frontend Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| **HTML5** | - | Markup structure |
| **CSS3** | - | Custom styling |
| **Tailwind CSS** | CDN | Utility-first CSS framework |
| **JavaScript (ES6+)** | - | Client-side interactivity |
| **Alpine.js** | 3.13.3 | Lightweight reactive framework |
| **Thymeleaf** | - | Server-side templating |

### Testing Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| **JUnit 5** | Built-in | Unit testing framework |
| **Playwright** | 1.40.0 | End-to-end testing |
| **Spring Boot Test** | Built-in | Integration testing support |

### Build & Deployment

| Technology | Version | Purpose |
|------------|---------|---------|
| **Maven** | 3.x | Build automation and dependency management |
| **Spring Boot Maven Plugin** | Built-in | Package executable JAR |
| **Maven Assembly Plugin** | 3.6.0 | Create distribution ZIP |

---

## Component Architecture

### Layer-by-Layer Breakdown

```mermaid
graph LR
    subgraph "Presentation Layer"
        A1[ApiTesterController]
        A2[Thymeleaf Templates]
        A3[Static Resources]
    end
    
    subgraph "Service Layer"
        B1[SavedRequestService]
        B2[ProjectService]
        B3[ApplicationService]
        B4[EnvironmentService]
        B5[ApiRequestService]
        B6[ApiMonitorService]
        B7[SwaggerImportService]
        B8[ExportImportService]
    end
    
    subgraph "Repository Layer"
        C1[SavedRequestRepository]
        C2[ProjectRepository]
        C3[ApplicationRepository]
        C4[EnvironmentRepository]
        C5[ApiRequestRepository]
        C6[ApiMonitorRepository]
    end
    
    subgraph "Model Layer"
        D1[Project Entity]
        D2[Application Entity]
        D3[SavedRequest Entity]
        D4[Environment Entity]
        D5[ApiRequest Entity]
        D6[ApiMonitor Entity]
    end
    
    subgraph "Configuration Layer"
        E1[DatabaseConfig]
        E2[CacheConfig]
        E3[SecurityConfig]
        E4[HibernateConfig]
        E5[RestTemplateConfig]
        E6[JacksonConfig]
    end
    
    A1 --> B1
    A1 --> B2
    A1 --> B3
    A1 --> B4
    A1 --> B5
    A1 --> B6
    A1 --> B7
    A1 --> B8
    
    B1 --> C1
    B2 --> C2
    B3 --> C3
    B4 --> C4
    B5 --> C5
    B6 --> C6
    
    C1 --> D3
    C2 --> D1
    C3 --> D2
    C4 --> D4
    C5 --> D5
    C6 --> D6
    
    E1 --> C1
    E2 --> B1
    E3 --> A1
```

### Component Responsibilities

#### 1. Controller Layer (`ApiTesterController`)

**Responsibilities:**
- Handle HTTP requests and responses
- Route requests to appropriate services
- Prepare model data for Thymeleaf templates
- Validate input data
- Handle exceptions and return appropriate responses

**Key Endpoints:**
- `GET /` - Home page with request builder
- `GET /projects` - Projects management page
- `GET /history` - Request history page
- `POST /api/execute` - Execute API request
- `POST /api/saved` - Save API request
- `GET /api/projects` - Get all projects
- `POST /api/projects` - Create project
- `GET /api/applications/{projectId}` - Get applications by project
- `POST /api/environments` - Create/update environment

#### 2. Service Layer

**SavedRequestService**
- Manage CRUD operations for saved API requests
- Handle request assignment to applications
- Support request duplication
- Manage request deletion (single and bulk)

**ProjectService**
- Manage project lifecycle (create, read, update, delete)
- Provide project listing with applications
- Cache project data for performance

**ApplicationService**
- Manage application lifecycle within projects
- Handle application-project relationships
- Provide application listing with APIs

**EnvironmentService**
- Manage environment variables per application
- Handle variable substitution in requests
- Support environment switching

**ApiRequestService**
- Execute HTTP requests to external APIs
- Handle request/response transformation
- Support variable substitution
- Store request history
- Execute pre/post-request scripts

**ApiMonitorService**
- Schedule and execute API health checks
- Track API status and response times
- Alert on API failures
- Store monitoring history

**SwaggerImportService**
- Parse Swagger/OpenAPI specifications
- Extract API endpoints and schemas
- Create saved requests from Swagger definitions

**ExportImportService**
- Export projects/applications/APIs to JSON
- Import exported data
- Support data migration

#### 3. Repository Layer

**Responsibilities:**
- Abstract database operations
- Provide custom query methods
- Handle entity relationships
- Optimize queries with `@EntityGraph`

**Key Repositories:**
- `SavedRequestRepository` - CRUD for saved requests
- `ProjectRepository` - CRUD for projects
- `ApplicationRepository` - CRUD for applications with `@EntityGraph`
- `EnvironmentRepository` - CRUD for environments
- `ApiRequestRepository` - CRUD for request history
- `ApiMonitorRepository` - CRUD for API monitors

#### 4. Model Layer (Entities)

**Entity Relationships:**

```mermaid
erDiagram
    Project ||--o{ Application : "has"
    Application ||--o{ SavedRequest : "contains"
    Application ||--o{ Environment : "has"
    SavedRequest }o--|| Application : "belongs to"
    Environment }o--|| Application : "belongs to"
    ApiRequest }o--|| SavedRequest : "references"
    ApiMonitor }o--|| SavedRequest : "monitors"
    
    Project {
        Long id PK
        String name
        String description
        String url
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }
    
    Application {
        Long id PK
        String name
        String description
        Long projectId FK
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }
    
    SavedRequest {
        Long id PK
        String name
        String method
        String url
        String headers
        String body
        String preRequestScript
        String postRequestScript
        Long applicationId FK
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }
    
    Environment {
        Long id PK
        String name
        String description
        String variables
        Long applicationId FK
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }
    
    ApiRequest {
        Long id PK
        String method
        String url
        Integer statusCode
        String responseBody
        Long executionTime
        LocalDateTime executedAt
        Long savedRequestId FK
    }
    
    ApiMonitor {
        Long id PK
        String name
        String url
        String method
        Integer intervalMinutes
        Boolean isActive
        LocalDateTime lastChecked
        String lastStatus
        Long savedRequestId FK
    }
```

---

## Data Flow Diagrams

### 1. API Request Execution Flow

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant Controller
    participant ApiRequestService
    participant EnvironmentService
    participant RestTemplate
    participant ExternalAPI
    participant ApiRequestRepository
    participant Cache

    User->>Browser: Fill request form & click Send
    Browser->>Controller: POST /api/execute
    Controller->>ApiRequestService: executeRequest(requestDto)
    
    alt Environment variables exist
        ApiRequestService->>EnvironmentService: getEnvironmentVariables(applicationId)
        EnvironmentService-->>ApiRequestService: variables map
        ApiRequestService->>ApiRequestService: substituteVariables(url, headers, body)
    end
    
    alt Pre-request script exists
        ApiRequestService->>ApiRequestService: executePreRequestScript(script)
    end
    
    ApiRequestService->>RestTemplate: execute HTTP request
    RestTemplate->>ExternalAPI: HTTP Request
    ExternalAPI-->>RestTemplate: HTTP Response
    RestTemplate-->>ApiRequestService: ResponseDto
    
    alt Post-request script exists
        ApiRequestService->>ApiRequestService: executePostRequestScript(script, response)
    end
    
    ApiRequestService->>ApiRequestRepository: save(ApiRequest)
    ApiRequestRepository-->>ApiRequestService: saved entity
    ApiRequestService->>Cache: evict cache entries
    ApiRequestService-->>Controller: ResponseDto
    Controller-->>Browser: JSON response
    Browser-->>User: Display response
```

### 2. Save Request Flow

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant Controller
    participant SavedRequestService
    participant ApplicationRepository
    participant SavedRequestRepository
    participant Cache

    User->>Browser: Fill form & click Save
    Browser->>Controller: POST /api/saved
    Controller->>SavedRequestService: saveRequest(savedRequestDto)
    
    alt Update existing request
        SavedRequestService->>SavedRequestRepository: findById(id)
        SavedRequestRepository-->>SavedRequestService: existing entity
    else Create new request
        SavedRequestService->>SavedRequestService: new SavedRequest()
    end
    
    alt Application ID provided
        SavedRequestService->>ApplicationRepository: findById(applicationId)
        ApplicationRepository-->>SavedRequestService: Application entity
        SavedRequestService->>SavedRequestService: setApplication(application)
    end
    
    SavedRequestService->>SavedRequestRepository: save(savedRequest)
    SavedRequestRepository-->>SavedRequestService: saved entity
    SavedRequestService->>Cache: evict cache entries
    SavedRequestService-->>Controller: SavedRequest entity
    Controller-->>Browser: Success response
    Browser-->>User: Show success message
```

### 3. Project Management Flow

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant Controller
    participant ProjectService
    participant ProjectRepository
    participant Cache

    User->>Browser: Create/Update Project
    Browser->>Controller: POST /api/projects
    Controller->>ProjectService: saveProject(projectDto)
    
    alt Update existing
        ProjectService->>ProjectRepository: findById(id)
        ProjectRepository-->>ProjectService: existing entity
    else Create new
        ProjectService->>ProjectService: new Project()
    end
    
    ProjectService->>ProjectService: setFields(name, description, url)
    ProjectService->>ProjectRepository: save(project)
    ProjectRepository-->>ProjectService: saved entity
    ProjectService->>Cache: evict("projects")
    ProjectService-->>Controller: Project entity
    Controller-->>Browser: Success response
    Browser-->>User: Refresh project list
```

### 4. Environment Variable Substitution Flow

```mermaid
sequenceDiagram
    participant ApiRequestService
    participant EnvironmentService
    participant EnvironmentRepository
    participant SavedRequest

    ApiRequestService->>EnvironmentService: getEnvironmentVariables(applicationId, envName)
    EnvironmentService->>EnvironmentRepository: findByApplicationIdAndName(applicationId, envName)
    EnvironmentRepository-->>EnvironmentService: Environment entity
    EnvironmentService->>EnvironmentService: parseVariablesJSON(environment.variables)
    EnvironmentService-->>ApiRequestService: Map<String, String> variables
    
    ApiRequestService->>ApiRequestService: substituteInURL(url, variables)
    ApiRequestService->>ApiRequestService: substituteInHeaders(headers, variables)
    ApiRequestService->>ApiRequestService: substituteInBody(body, variables)
    
    Note over ApiRequestService: Variables replaced: {{variableName}} → value
```

---

## Database Schema

### Complete Schema Diagram

```mermaid
erDiagram
    PROJECTS {
        BIGINT id PK
        VARCHAR name
        TEXT description
        VARCHAR url
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }
    
    APPLICATIONS {
        BIGINT id PK
        VARCHAR name
        TEXT description
        BIGINT project_id FK
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }
    
    SAVED_REQUESTS {
        BIGINT id PK
        VARCHAR name
        VARCHAR method
        VARCHAR url
        TEXT headers
        TEXT body
        TEXT pre_request_script
        TEXT post_request_script
        BIGINT collection_id FK "deprecated"
        BIGINT application_id FK
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }
    
    ENVIRONMENTS {
        BIGINT id PK
        VARCHAR name
        TEXT description
        TEXT variables "JSON format"
        BIGINT application_id FK
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }
    
    API_REQUESTS {
        BIGINT id PK
        VARCHAR method
        VARCHAR url
        TEXT request_headers
        TEXT request_body
        INTEGER status_code
        TEXT response_headers
        TEXT response_body
        BIGINT execution_time_ms
        TIMESTAMP executed_at
        BIGINT saved_request_id FK
    }
    
    API_MONITORS {
        BIGINT id PK
        VARCHAR name
        VARCHAR url
        VARCHAR method
        INTEGER interval_minutes
        BOOLEAN is_active
        TIMESTAMP last_checked
        VARCHAR last_status
        BIGINT saved_request_id FK
    }
    
    COLLECTIONS {
        BIGINT id PK
        VARCHAR name
        TEXT description
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }
    
    PROJECTS ||--o{ APPLICATIONS : "has"
    APPLICATIONS ||--o{ SAVED_REQUESTS : "contains"
    APPLICATIONS ||--o{ ENVIRONMENTS : "has"
    SAVED_REQUESTS }o--|| APPLICATIONS : "belongs_to"
    ENVIRONMENTS }o--|| APPLICATIONS : "belongs_to"
    API_REQUESTS }o--|| SAVED_REQUESTS : "references"
    API_MONITORS }o--|| SAVED_REQUESTS : "monitors"
```

### Indexes

| Table | Index Name | Columns | Purpose |
|-------|------------|---------|---------|
| `projects` | `idx_project_created_at` | `created_at` | Fast sorting by creation date |
| `saved_requests` | `idx_saved_request_application_id` | `application_id` | Fast lookup by application |
| `saved_requests` | `idx_saved_request_created_at` | `created_at` | Fast sorting by creation date |
| `api_requests` | `idx_api_request_status_code` | `status_code` | Fast filtering by status |
| `api_requests` | `idx_api_request_executed_at` | `executed_at` | Fast sorting by execution time |

---

## Request Processing Flow

### Detailed Request Execution

```mermaid
flowchart TD
    Start([User Clicks Send]) --> Validate[Validate Request Data]
    Validate -->|Invalid| Error[Return Error]
    Validate -->|Valid| CheckEnv{Environment<br/>Selected?}
    
    CheckEnv -->|Yes| GetEnv[Get Environment Variables]
    CheckEnv -->|No| CheckPreScript{Pre-Request<br/>Script?}
    
    GetEnv --> ParseEnv[Parse Variables JSON]
    ParseEnv --> SubstituteEnv[Substitute Variables<br/>in URL/Headers/Body]
    SubstituteEnv --> CheckPreScript
    
    CheckPreScript -->|Yes| ExecPreScript[Execute Pre-Request Script]
    CheckPreScript -->|No| BuildRequest[Build HTTP Request]
    ExecPreScript --> BuildRequest
    
    BuildRequest --> SetHeaders[Set Request Headers]
    SetHeaders --> SetBody[Set Request Body]
    SetBody --> Execute[Execute HTTP Request<br/>via RestTemplate]
    
    Execute -->|Success| GetResponse[Get Response]
    Execute -->|Error| HandleError[Handle Error]
    
    GetResponse --> CheckPostScript{Post-Request<br/>Script?}
    CheckPostScript -->|Yes| ExecPostScript[Execute Post-Request Script]
    CheckPostScript -->|No| SaveHistory[Save to History]
    ExecPostScript --> SaveHistory
    
    SaveHistory --> CacheEvict[Evict Cache]
    CacheEvict --> ReturnResponse[Return Response to Client]
    
    HandleError --> SaveError[Save Error to History]
    SaveError --> ReturnError[Return Error Response]
    
    ReturnResponse --> End([End])
    ReturnError --> End
    Error --> End
```

---

## Security Architecture

### Security Layers

```mermaid
graph TB
    subgraph "Security Filters"
        RateLimit[Rate Limiting Filter]
        SecurityFilter[Spring Security Filter]
        CORSFilter[CORS Filter]
        XSSFilter[XSS Protection]
    end
    
    subgraph "Validation Layer"
        URLValidator[URL Validation]
        CSRFValidator[CSRF Protection]
        InputValidator[Input Sanitization]
    end
    
    subgraph "Request Processing"
        Controller[Controller]
        Service[Service Layer]
    end
    
    Request[HTTP Request] --> RateLimit
    RateLimit --> SecurityFilter
    SecurityFilter --> CORSFilter
    CORSFilter --> XSSFilter
    XSSFilter --> URLValidator
    URLValidator --> CSRFValidator
    CSRFValidator --> InputValidator
    InputValidator --> Controller
    Controller --> Service
```

### Security Features

1. **Rate Limiting**
   - Prevents abuse with request rate limits
   - Configurable per endpoint
   - In-memory tracking

2. **Input Validation**
   - URL validation (whitelist/blacklist)
   - XSS prevention
   - SQL injection prevention (via JPA)
   - Request size limits

3. **CORS Configuration**
   - Configurable CORS policies
   - Origin validation

4. **CSRF Protection**
   - Spring Security CSRF tokens
   - Form submission validation

---

## Deployment Architecture

### Single-JAR Deployment

```mermaid
graph TB
    subgraph "Deployment Package"
        JAR[rest-api-tester-1.0.0.jar]
        Scripts[Start Scripts]
        README[Documentation]
    end
    
    subgraph "Runtime Environment"
        JVM[Java 17 JVM]
        OS[Operating System<br/>Windows/macOS/Linux]
    end
    
    subgraph "Data Storage"
        DBFile[SQLite Database File<br/>~/.bisen-api-tester/]
        LogFile[Log Files<br/>~/.bisen-api-tester/]
    end
    
    subgraph "Network"
        Port[Port 2000]
        HTTP[HTTP Server<br/>Embedded Tomcat]
    end
    
    JAR --> JVM
    JVM --> OS
    JAR --> HTTP
    HTTP --> Port
    JAR --> DBFile
    JAR --> LogFile
    Scripts --> JAR
```

### Deployment Characteristics

- **Self-contained**: Single JAR file with all dependencies
- **Embedded Server**: Tomcat embedded in Spring Boot
- **File-based Database**: SQLite database in user directory
- **No External Dependencies**: No need for separate database server
- **Cross-platform**: Works on Windows, macOS, and Linux
- **Portable**: Can run from any directory

---

## Low-Level Design Details

### 1. Caching Strategy

```mermaid
graph LR
    Request[Service Request] --> CheckCache{Cache Hit?}
    CheckCache -->|Yes| ReturnCache[Return Cached Data]
    CheckCache -->|No| QueryDB[Query Database]
    QueryDB --> StoreCache[Store in Cache]
    StoreCache --> ReturnData[Return Data]
    
    Update[Data Update] --> InvalidateCache[Invalidate Cache]
    InvalidateCache --> UpdateDB[Update Database]
```

**Cache Configuration:**
- **Type**: In-memory (ConcurrentMapCacheManager)
- **Cached Entities**: Projects, Applications (read-heavy)
- **TTL**: No expiration (manual eviction on updates)
- **Eviction Strategy**: Manual eviction on create/update/delete

### 2. Connection Pooling

**HikariCP Configuration:**
- **Maximum Pool Size**: 10 connections
- **Minimum Idle**: 2 connections
- **Connection Timeout**: 30 seconds
- **Idle Timeout**: 10 minutes
- **Max Lifetime**: 30 minutes
- **Leak Detection**: 60 seconds

### 3. Transaction Management

- **Default**: Spring's `@Transactional` with read-only optimization
- **Isolation Level**: Default (READ_COMMITTED for SQLite)
- **Propagation**: REQUIRED (default)

### 4. Exception Handling

```mermaid
graph TD
    Exception[Exception Thrown] --> ControllerException[Controller Level]
    ControllerException --> ServiceException[Service Level]
    ServiceException --> RepositoryException[Repository Level]
    
    RepositoryException -->|Data Access| DataException[Data Access Exception]
    RepositoryException -->|Not Found| NotFoundException[Entity Not Found]
    
    ServiceException -->|Business Logic| BusinessException[Business Logic Exception]
    ServiceException -->|Validation| ValidationException[Validation Exception]
    
    ControllerException -->|HTTP Error| HTTPException[HTTP Error Response]
    ControllerException -->|Success| SuccessResponse[Success Response]
```

---

## Performance Optimizations

### 1. Database Optimizations
- **Indexes**: On foreign keys and frequently queried columns
- **Lazy Loading**: For `@OneToMany` relationships
- **@EntityGraph**: Eager loading when needed to avoid N+1 queries
- **Batch Operations**: For bulk inserts/updates

### 2. Caching
- **Read-heavy operations**: Projects and Applications cached
- **Cache eviction**: On data modifications
- **In-memory cache**: Fast access for frequently accessed data

### 3. Query Optimization
- **Custom queries**: Optimized with `@Query` annotations
- **Pagination**: For large result sets (future enhancement)
- **Projection**: DTOs for minimal data transfer

### 4. Connection Pooling
- **HikariCP**: High-performance connection pool
- **Connection reuse**: Reduces connection overhead
- **Leak detection**: Prevents connection leaks

---

## API Endpoints Summary

### REST API Endpoints

| Method | Endpoint | Purpose | Service |
|--------|----------|---------|---------|
| GET | `/` | Home page | Controller |
| GET | `/projects` | Projects page | Controller |
| GET | `/history` | History page | Controller |
| POST | `/api/execute` | Execute API request | ApiRequestService |
| POST | `/api/saved` | Save request | SavedRequestService |
| GET | `/api/saved` | Get all saved requests | SavedRequestService |
| DELETE | `/api/saved/{id}` | Delete request | SavedRequestService |
| GET | `/api/projects` | Get all projects | ProjectService |
| POST | `/api/projects` | Create/update project | ProjectService |
| DELETE | `/api/projects/{id}` | Delete project | ProjectService |
| GET | `/api/applications/{projectId}` | Get applications | ApplicationService |
| POST | `/api/applications` | Create application | ApplicationService |
| POST | `/api/environments` | Create/update environment | EnvironmentService |
| GET | `/api/history` | Get request history | ApiRequestService |
| POST | `/api/import/swagger` | Import Swagger | SwaggerImportService |

---

## Future Enhancements

### Planned Improvements
1. **Multi-user support**: User authentication and authorization
2. **Team collaboration**: Shared projects and APIs
3. **API collections**: Group related APIs
4. **GraphQL support**: Test GraphQL APIs
5. **WebSocket testing**: Real-time API testing
6. **Performance testing**: Load testing capabilities
7. **API documentation**: Auto-generate API docs
8. **CI/CD integration**: Integrate with Jenkins/GitHub Actions

---

## Conclusion

BISEN API Tester is built on a modern, scalable architecture using Spring Boot and follows best practices for:
- **Separation of Concerns**: Clear layer separation
- **Dependency Injection**: Spring's IoC container
- **Database Abstraction**: JPA/Hibernate ORM
- **Security**: Multiple security layers
- **Performance**: Caching and connection pooling
- **Maintainability**: Clean code structure and documentation

The architecture supports future enhancements while maintaining simplicity and performance.

---

**Document Version:** 1.0  
**Last Updated:** December 2024  
**Maintained By:** Development Team

