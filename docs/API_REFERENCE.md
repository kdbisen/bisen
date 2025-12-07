# BISEN API Reference

**Author:** Kuldeep Bisen  
**Version:** 1.2 BETA

---

## REST API Endpoints

### Execute Request

Execute an API request and get the response.

**Endpoint:** `POST /api/execute`

**Request Body:**
```json
{
  "method": "GET",
  "url": "https://api.example.com/users",
  "headers": "Content-Type: application/json\nAuthorization: Bearer token",
  "body": "{\"key\": \"value\"}",
  "name": "Get Users",
  "collectionId": 1,
  "authType": "bearer",
  "token": "your-token",
  "variables": "{\"baseUrl\": \"https://api.example.com\"}",
  "environmentId": 1,
  "timeout": 30000,
  "ignoreSslErrors": false
}
```

**Response:**
```json
{
  "statusCode": 200,
  "response": "{\"data\": [...]}",
  "responseTime": 245,
  "headers": "Content-Type: application/json\n..."
}
```

---

### Collections

#### Get All Collections
**Endpoint:** `GET /api/collections`

**Response:**
```json
[
  {
    "id": 1,
    "name": "My Collection",
    "description": "Collection description",
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

#### Create Collection
**Endpoint:** `POST /api/collections`

**Request Body:**
```json
{
  "name": "New Collection",
  "description": "Description"
}
```

#### Update Collection
**Endpoint:** `PUT /api/collections/{id}`

**Request Body:**
```json
{
  "name": "Updated Name",
  "description": "Updated Description"
}
```

#### Delete Collection
**Endpoint:** `DELETE /api/collections/{id}`

---

### Saved Requests

#### Get All Saved Requests
**Endpoint:** `GET /api/saved`

#### Get Saved Request by ID
**Endpoint:** `GET /api/saved/{id}`

#### Save Request
**Endpoint:** `POST /api/saved`

**Request Body:**
```json
{
  "id": null,
  "name": "Get User",
  "method": "GET",
  "url": "https://api.example.com/users/1",
  "headers": "Content-Type: application/json",
  "body": "",
  "collectionId": 1
}
```

#### Delete Saved Request
**Endpoint:** `DELETE /api/saved/{id}`

#### Update Request Collection
**Endpoint:** `PUT /api/saved/{id}/collection`

**Request Body:**
```json
{
  "collectionId": 2
}
```

---

### Environments

#### Get All Environments
**Endpoint:** `GET /api/environments`

#### Get Environment by ID
**Endpoint:** `GET /api/environments/{id}`

#### Create Environment
**Endpoint:** `POST /api/environments`

**Request Body:**
```json
{
  "name": "Development",
  "description": "Dev environment",
  "variables": "{\"baseUrl\": \"https://dev.api.com\"}",
  "isDefault": false
}
```

#### Update Environment
**Endpoint:** `PUT /api/environments/{id}`

#### Delete Environment
**Endpoint:** `DELETE /api/environments/{id}`

#### Set Default Environment
**Endpoint:** `POST /api/environments/{id}/set-default`

---

### Export/Import

#### Export All Data
**Endpoint:** `GET /api/export`

**Response:**
```json
{
  "version": "1.0",
  "exportedAt": "2024-01-01T00:00:00",
  "collections": [...],
  "savedRequests": [...],
  "environments": [...]
}
```

#### Import BISEN Format
**Endpoint:** `POST /api/import/bisen`

**Request Body:**
```json
{
  "version": "1.0",
  "exportedAt": "2024-01-01T00:00:00",
  "collections": [...],
  "savedRequests": [...],
  "environments": [...]
}
```

**Response:**
```json
{
  "collectionsCreated": 5,
  "requestsCreated": 20,
  "environmentsCreated": 3
}
```

---

### Swagger Import

#### Import from URL
**Endpoint:** `POST /api/import/url`

**Request Body:**
```json
{
  "url": "https://api.example.com/swagger.json"
}
```

#### Import from File
**Endpoint:** `POST /api/import/file`

**Content-Type:** `multipart/form-data`

**Form Data:**
- `file`: Swagger/OpenAPI JSON or YAML file

---

### Request History

#### Get All History
**Endpoint:** `GET /history`

#### Get History Item
**Endpoint:** `GET /history/{id}`

#### Delete History Item
**Endpoint:** `DELETE /history/{id}`

---

## Data Models

### RequestDto
```java
{
  "method": String,
  "url": String,
  "headers": String,
  "body": String,
  "name": String,
  "collectionId": Long,
  "authType": String,
  "username": String,
  "password": String,
  "token": String,
  "apiKey": String,
  "apiKeyHeader": String,
  "certificateFile": String,
  "certificatePassword": String,
  "certificateType": String,
  "ignoreSslErrors": Boolean,
  "variables": String,
  "environmentId": Long,
  "timeout": Integer
}
```

### ResponseDto
```java
{
  "statusCode": Integer,
  "response": String,
  "responseTime": Long,
  "headers": String
}
```

### CollectionDto
```java
{
  "name": String,
  "description": String
}
```

### EnvironmentDto
```java
{
  "name": String,
  "description": String,
  "variables": String,
  "isDefault": Boolean
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "error": "Invalid request data"
}
```

### 404 Not Found
```json
{
  "error": "Resource not found"
}
```

### 429 Too Many Requests
```json
{
  "error": "Too Many Requests"
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal server error"
}
```

---

*API Reference by Kuldeep Bisen*

