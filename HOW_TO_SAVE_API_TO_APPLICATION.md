# How to Save API Requests to Applications

## Step-by-Step Guide

### Step 1: Create a Project (if you don't have one)

1. Click on **"Projects"** in the top navigation menu
2. Click the **"+ New Project"** button
3. Enter:
   - **Project Name**: e.g., "E-Commerce Backend"
   - **Description**: (Optional) e.g., "Backend APIs for e-commerce platform"
4. Click **"Save"**

### Step 2: Create an Application within the Project

1. On the Projects dashboard, find your project card
2. Click **"Create Application"** (or the "+" button if no applications exist)
3. Enter:
   - **Application Name**: e.g., "Payment Gateway Service"
   - **Version**: (Optional) e.g., "V2.1.0"
   - **Description**: (Optional) e.g., "Handles payment processing"
4. Click **"Save"**

### Step 3: Configure Your API Request

1. Go to the **Home** page (main request builder)
2. Fill in your API request:
   - Select HTTP Method (GET, POST, PUT, etc.)
   - Enter the URL
   - Add headers (if needed)
   - Add request body (if needed)
   - Configure authorization (if needed)

### Step 4: Assign API to Application

1. Click on the **"Settings"** tab in the request builder
2. Scroll down to **"Assign to Application (Optional)"** section
3. Click the dropdown to see all available applications
4. The dropdown shows: **Project Name > Application Name (Version)**
   - Example: `E-Commerce Backend > Payment Gateway Service (V2.1.0)`
5. Select the application you want to assign this API to
6. You'll see a confirmation showing: `Project Name > Application Name`

### Step 5: Save the Request

1. Optionally enter a **Request Name** in the Settings tab
2. Click the **"Save"** button (top right of the request builder)
3. The API request is now saved and assigned to the selected application!

## Visual Guide

```
┌─────────────────────────────────────────┐
│  Settings Tab                            │
├─────────────────────────────────────────┤
│  Request Name (Optional)                 │
│  [Get User Profile              ]        │
│                                          │
│  Save to Collection (Optional)          │
│  [-- No Collection --         ▼]        │
│                                          │
│  Assign to Application (Optional)        │
│  [Manage Projects]                       │
│  [E-Commerce Backend > Payment... ▼]    │
│  ┌─────────────────────────────────┐   │
│  │ E-Commerce Backend > Payment... │   │
│  └─────────────────────────────────┘   │
│                                          │
│  Request Timeout (ms)                   │
│  [30000]                                │
└─────────────────────────────────────────┘
```

## Quick Tips

### ✅ Best Practices

1. **Organize by Project**: Group related applications under the same project
   - Example: "E-Commerce Backend" project contains:
     - Payment Gateway Service
     - User Authentication
     - Inventory Management

2. **Use Versions**: Add version numbers to applications for better tracking
   - Example: "V1.0.0", "V2.1.0", "V3.0.0"

3. **Name Your Requests**: Always give meaningful names to your API requests
   - Good: "POST /v1/charge - Create Payment"
   - Bad: "Request 1"

### 🔍 Viewing Saved APIs

1. Go to **Projects** page
2. Click on a project card
3. Expand an application to see all APIs assigned to it
4. Each API shows:
   - HTTP Method (GET, POST, etc.)
   - URL
   - Status (if recently tested)

### 📝 Editing Assignment

To change which application an API belongs to:

1. Load the saved request (click it from the sidebar)
2. Go to **Settings** tab
3. Change the **"Assign to Application"** dropdown
4. Click **"Save"** again

### 🗑️ Removing Assignment

To remove an API from an application:

1. Load the saved request
2. Go to **Settings** tab
3. Select **"-- No Application --"** from the dropdown
4. Click **"Save"**

## Troubleshooting

### ❌ "No applications available"

**Solution**: 
1. Go to **Projects** page
2. Create a project first
3. Then create an application within that project
4. Refresh the Home page

### ❌ Application dropdown is empty

**Solution**:
1. Make sure you have at least one project with at least one application
2. Refresh the page (F5)
3. Check browser console for errors

### ❌ Can't see my saved API in the project

**Solution**:
1. Make sure you selected an application when saving
2. Go to Projects page
3. Expand the project and application
4. The API should appear in the endpoints list

## Example Workflow

```
1. Create Project: "E-Commerce Backend"
   ↓
2. Create Application: "Payment Gateway Service" (V2.1.0)
   ↓
3. Configure API Request:
   - Method: POST
   - URL: https://api.example.com/v1/charge
   - Body: {"amount": 100, "currency": "USD"}
   ↓
4. Go to Settings Tab
   ↓
5. Select: "E-Commerce Backend > Payment Gateway Service (V2.1.0)"
   ↓
6. Click "Save"
   ↓
✅ API is now saved and assigned to the application!
```

## Need Help?

- Check the **Guide** page for more information
- Visit **Projects** page to manage your projects and applications
- Use the **"Manage Projects"** button in Settings tab for quick access

---

**BISEN** - Powerful, Elegant & Simple API Testing Tool

