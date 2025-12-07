# BISEN Features Documentation

**Author:** Kuldeep Bisen  
**Version:** 1.2 BETA

---

## Complete Feature List

### 1. HTTP Request Builder

#### Supported HTTP Methods
- **GET** - Retrieve data
- **POST** - Create new resources
- **PUT** - Update entire resource
- **DELETE** - Remove resource
- **PATCH** - Partial update
- **HEAD** - Get headers only
- **OPTIONS** - Get allowed methods

#### Request Components
- **URL** - Full endpoint URL with query parameters
- **Headers** - Custom HTTP headers with key-value pairs
- **Body Types**:
  - JSON
  - XML
  - Form Data
  - Form URL Encoded
  - Text/Plain
  - HTML
  - None

#### Advanced Features
- **Request Timeout** - Configurable timeout (1s - 5min)
- **SSL Certificate Support** - Upload client certificates (P12, PEM, JKS)
- **Ignore SSL Errors** - For self-signed certificates
- **Authorization Types**:
  - Basic Auth
  - Bearer Token
  - API Key
  - Digest Auth

---

### 2. Environment Management

#### Environment Features
- Create unlimited environments (Dev, Staging, Prod, etc.)
- Set default environment
- Environment-specific variables
- Variable inheritance and override

#### Variable Substitution
- Use `{{variableName}}` syntax
- Works in URL, headers, and body
- Real-time preview
- JSON validation

#### Example
```json
{
  "baseUrl": "https://api.example.com",
  "apiKey": "secret-key-123",
  "version": "v1"
}
```

Usage:
- URL: `{{baseUrl}}/{{version}}/users`
- Header: `X-API-Key: {{apiKey}}`
- Body: `{"apiKey": "{{apiKey}}"}`

---

### 3. Collections

#### Collection Management
- Create collections to organize requests
- Hierarchical sidebar navigation
- Nested request display
- Collection descriptions

#### Request Organization
- Add requests to collections
- Remove from collections
- Unassigned requests section
- Search and filter

---

### 4. Request History

#### History Features
- Automatic history tracking
- View all past requests
- Response details
- Status codes
- Response times
- Delete history entries

---

### 5. Saved Requests

#### Save & Load
- Save request templates
- Load saved requests
- Edit saved requests
- Duplicate requests
- Delete saved requests

#### Request Details
- Request name
- Method and URL
- Headers and body
- Collection assignment

---

### 6. Export/Import

#### Export Format (BISEN)
```json
{
  "version": "1.0",
  "exportedAt": "2024-01-01T00:00:00",
  "collections": [...],
  "savedRequests": [...],
  "environments": [...]
}
```

#### Export Includes
- All collections with requests
- Unassigned saved requests
- All environments with variables

#### Import Features
- Import from BISEN format
- Merge with existing data
- Create new collections/environments
- Preserve relationships

---

### 7. Swagger/OpenAPI Import

#### Import Sources
- URL - Import from Swagger/OpenAPI URL
- File - Upload JSON/YAML file

#### Import Features
- Automatic collection creation by tags
- Request generation from paths
- Method support (GET, POST, PUT, DELETE, etc.)
- Parameter handling

---

### 8. Request Validation

#### JSON Validation
- Real-time JSON syntax checking
- Error messages with line numbers
- Auto-format on blur
- Format button

#### XML Validation
- XML syntax validation
- Format button
- Error detection

#### Variable Validation
- JSON format validation
- Format button
- Error messages

---

### 9. Security Features

#### SSRF Protection
- Blocks internal IP addresses
- Blocks localhost
- Blocks private IP ranges
- URL validation

#### Input Validation
- URL length limits
- Header size limits
- Body size limits
- Method validation

#### Rate Limiting
- 100 requests per minute per IP
- HTTP 429 response on limit
- Automatic reset

#### Security Headers
- Content-Security-Policy
- X-Frame-Options
- X-Content-Type-Options
- X-XSS-Protection
- HSTS

#### Sensitive Data Protection
- Password masking in logs
- Token masking
- API key masking
- Secure storage

---

### 10. UI Features

#### User Interface
- Clean, minimalist design
- Responsive layout
- Soft color palette
- Smooth animations

#### Navigation
- Top navigation bar
- Sidebar with collections
- Breadcrumb navigation
- Quick actions

#### Search & Filter
- Search saved requests
- Filter by collection
- Real-time filtering

---

## Keyboard Shortcuts

*Coming soon in future versions*

---

## Best Practices

1. **Use Environments**
   - Create separate environments for Dev, Staging, Prod
   - Use variables for base URLs and API keys

2. **Organize with Collections**
   - Group related requests
   - Use descriptive names

3. **Save Requests**
   - Save frequently used requests
   - Use descriptive names

4. **Validate Before Sending**
   - Always validate JSON/XML
   - Check variable substitution

5. **Export Regularly**
   - Export collections for backup
   - Share with team members

---

*Documentation by Kuldeep Bisen*

