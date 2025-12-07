# BISEN Quick Start Guide

**Author:** Kuldeep Bisen  
**Version:** 1.2 BETA

---

## 5-Minute Quick Start

### Step 1: Start BISEN

```bash
cd rest-api-tester
mvn spring-boot:run
```

Open browser: `http://localhost:8080`

---

### Step 2: Make Your First Request

1. **Select Method:** Choose `GET` from dropdown
2. **Enter URL:** `https://jsonplaceholder.typicode.com/posts/1`
3. **Click:** "Send Request"
4. **View Response:** See the JSON response below

---

### Step 3: Create an Environment

1. Click **"Environments"** in navigation
2. Click **"+ New Environment"**
3. Enter:
   - Name: `Development`
   - Variables: `{"baseUrl": "https://api.example.com"}`
4. Click **"OK"**

---

### Step 4: Use Variables

1. Select **"Development"** from environment dropdown
2. Enter URL: `{{baseUrl}}/users`
3. Click **"Preview"** to see substitution
4. Send request

---

### Step 5: Save Request

1. Fill in request details
2. Click **"Save Request"**
3. Enter name: `Get Users`
4. Request saved in sidebar

---

### Step 6: Create Collection

1. Go to **"Collections"** page
2. Click **"+ New Collection"**
3. Enter name: `My API Tests`
4. Add saved requests to collection

---

## Common Tasks

### Testing POST Request

1. Method: `POST`
2. URL: `https://jsonplaceholder.typicode.com/posts`
3. Body Type: `JSON`
4. Body:
   ```json
   {
     "title": "Test Post",
     "body": "This is a test",
     "userId": 1
   }
   ```
5. Click "Send Request"

### Using Authorization

1. Select Auth Type: `Bearer Token`
2. Enter Token: `your-token-here`
3. Send request

### Importing Swagger

1. Go to **"Import Swagger"**
2. Enter URL: `https://petstore.swagger.io/v2/swagger.json`
3. Click **"Import from URL"**
4. Collections created automatically

---

## Tips

- ✅ Use environments for different API endpoints
- ✅ Save frequently used requests
- ✅ Organize requests in collections
- ✅ Validate JSON before sending
- ✅ Export collections for backup

---

*Quick Start Guide by Kuldeep Bisen*

