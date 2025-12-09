# Troubleshooting: Saving API to Application

## Common Issues and Solutions

### Issue 1: Application Dropdown is Empty

**Symptoms:**
- The "Assign to Application" dropdown shows only "-- No Application --"
- No projects/applications appear in the dropdown

**Solutions:**

1. **Check if you have projects and applications:**
   - Go to **Projects** page
   - Verify you have at least one project
   - Verify that project has at least one application

2. **Check browser console:**
   - Open browser Developer Tools (F12)
   - Go to Console tab
   - Look for errors like:
     - "Error loading projects and applications"
     - "Failed to load applications"
   - Check Network tab to see if API calls are failing

3. **Refresh the page:**
   - Sometimes the applications don't load on first page load
   - Press F5 to refresh
   - Or close and reopen the browser tab

4. **Check API endpoints:**
   - Open browser Developer Tools → Network tab
   - Refresh the page
   - Look for:
     - `/api/projects` - Should return list of projects
     - `/api/projects/{id}/applications` - Should return applications for each project
   - If these return 404 or 500 errors, there's a backend issue

### Issue 2: Application Selected But Not Saving

**Symptoms:**
- You select an application from dropdown
- Click Save
- Request saves but application assignment is lost

**Solutions:**

1. **Check browser console:**
   - Open Developer Tools (F12) → Console
   - Before clicking Save, check:
     - "Saving request with data:" - Should show `applicationId` in the data
   - After clicking Save, check:
     - "Saved request response:" - Should show the saved request
     - Look for any error messages

2. **Verify applicationId is being sent:**
   - In Console, before saving, you should see:
     ```javascript
     Saving request with data: {
       name: "...",
       method: "...",
       url: "...",
       applicationId: 123  // <-- This should be a number, not null
     }
     ```

3. **Check Network tab:**
   - Open Developer Tools → Network tab
   - Click Save
   - Find the POST request to `/api/saved`
   - Click on it → Go to "Payload" or "Request" tab
   - Verify `applicationId` is in the request body

4. **Check server logs:**
   - Look at the application console/logs
   - You should see:
     ```
     Saving request: name=..., method=..., applicationId=123
     Assigned to application: ... (Project: ...)
     Saved request successfully with ID: ...
     ```
   - If you see "Application with ID X not found", the application doesn't exist

### Issue 3: Database Schema Issue

**Symptoms:**
- Error messages about missing column `application_id`
- SQL errors when saving

**Solutions:**

1. **Restart the application:**
   - The database schema should auto-create on startup
   - Stop the application
   - Delete the database file: `~/.bisen-api-tester/bisen-api-tester.db`
   - Start the application again
   - The schema will be recreated with the new `application_id` column

2. **Check database manually (if needed):**
   - The `saved_requests` table should have an `application_id` column
   - If using SQLite, you can check with:
     ```sql
     .schema saved_requests
     ```

### Issue 4: Application Not Showing in Projects Page

**Symptoms:**
- You saved an API to an application
- But when you go to Projects page, the API doesn't appear under the application

**Solutions:**

1. **Refresh the Projects page:**
   - The page might need to reload data
   - Press F5 or refresh the page

2. **Check if the API was actually saved:**
   - Go to **Saved Requests** page
   - Find your saved request
   - Check if it shows the project/application tags

3. **Verify the save was successful:**
   - Check browser console for "Saved request successfully" message
   - Check server logs for confirmation

### Issue 5: "Application with ID X not found" Error

**Symptoms:**
- Error in server logs: "Application with ID X not found"
- Request saves but without application assignment

**Solutions:**

1. **The application might have been deleted:**
   - Go to Projects page
   - Check if the application still exists
   - If not, create it again

2. **Application ID mismatch:**
   - The frontend might have a stale application ID
   - Refresh the page to reload applications
   - Select the application again

## Debugging Steps

### Step 1: Verify Projects and Applications Exist

1. Go to **Projects** page
2. Verify you see at least one project
3. Click on a project to expand
4. Verify you see at least one application
5. Note the application name

### Step 2: Check Application Dropdown

1. Go to **Home** page
2. Click **Settings** tab
3. Scroll to "Assign to Application"
4. Click the dropdown
5. You should see: `Project Name > Application Name`
6. If empty, check browser console for errors

### Step 3: Test Save with Console Open

1. Open browser Developer Tools (F12)
2. Go to **Console** tab
3. Fill in a test API request:
   - Method: GET
   - URL: https://jsonplaceholder.typicode.com/posts/1
4. Go to **Settings** tab
5. Select an application from dropdown
6. Click **Save**
7. Watch the console for:
   - "Saving request with data:" - Check if `applicationId` is present
   - "Saved request response:" - Check the response
   - Any error messages

### Step 4: Check Network Requests

1. Open Developer Tools → **Network** tab
2. Clear the network log
3. Click **Save**
4. Find the POST request to `/api/saved`
5. Click on it
6. Check:
   - **Status**: Should be 200 OK
   - **Request Payload**: Should include `applicationId`
   - **Response**: Should show the saved request with application info

### Step 5: Check Server Logs

Look for these log messages:
```
INFO  - Saving request: name=..., method=..., applicationId=123
INFO  - Assigned to application: ApplicationName (Project: ProjectName)
INFO  - Saved request successfully with ID: ...
```

If you see:
```
WARN  - Application with ID 123 not found
```
Then the application doesn't exist in the database.

## Quick Test

Run this in browser console (F12 → Console) to test:

```javascript
// Test 1: Check if applications are loaded
console.log('Applications:', applications);
console.log('Projects:', projects);

// Test 2: Check application select element
const select = document.getElementById('applicationId');
console.log('Application select:', select);
console.log('Options:', select ? select.options.length : 'not found');

// Test 3: Manually load applications
loadProjectsAndApplications().then(() => {
    console.log('Applications loaded:', applications.length);
});
```

## Still Not Working?

1. **Clear browser cache:**
   - Press Ctrl+Shift+Delete (or Cmd+Shift+Delete on Mac)
   - Clear cache and cookies
   - Refresh the page

2. **Check application is running:**
   - Make sure the Spring Boot application is running
   - Check the port (default: 2000)
   - Verify no errors in application startup logs

3. **Restart the application:**
   - Stop the application
   - Start it again
   - This will ensure all services are properly initialized

4. **Check database:**
   - Verify the database file exists: `~/.bisen-api-tester/bisen-api-tester.db`
   - Check if it's not corrupted
   - If needed, delete it and let the app recreate it

## Expected Behavior

When everything works correctly:

1. ✅ Applications load automatically when you open Settings tab
2. ✅ Dropdown shows: `Project Name > Application Name (Version)`
3. ✅ Selecting an application shows: `Project Name > Application Name`
4. ✅ Clicking Save shows: "Request saved successfully and assigned to Project > Application!"
5. ✅ On Projects page, the API appears under the application
6. ✅ Loading the saved request shows the application selected in Settings

---

**Need More Help?** Check the server logs and browser console for detailed error messages.

