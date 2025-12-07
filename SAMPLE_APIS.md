# Sample APIs Collection

## Overview

A sample collection **"Sample APIs - All Methods"** is automatically created when you first start the application. This collection contains working examples of all HTTP methods using public test APIs.

## What's Included

### GET Requests (8 examples)
- ✅ Get All Posts - `jsonplaceholder.typicode.com/posts`
- ✅ Get Single Post - `jsonplaceholder.typicode.com/posts/1`
- ✅ Get Users - `reqres.in/api/users?page=1`
- ✅ Get Single User - `reqres.in/api/users/2`
- ✅ HTTPBin Get - `httpbin.org/get?name=BISEN&type=API`
- ✅ Delayed Response - `httpbin.org/delay/2` (tests timeout handling)
- ✅ Status Code 200 - `httpbin.org/status/200`
- ✅ Status Code 404 - `httpbin.org/status/404` (error handling)
- ✅ JSON Response - `httpbin.org/json`

### POST Requests (4 examples)
- ✅ Create Post - Creates a new post with JSON body
- ✅ Create User - Creates a new user
- ✅ HTTPBin Post - Generic POST with JSON
- ✅ Login - Authentication example with credentials

### PUT Requests (3 examples)
- ✅ Update Post - Updates an existing post
- ✅ Update User - Updates user information
- ✅ HTTPBin Put - Generic PUT request

### PATCH Requests (3 examples)
- ✅ Patch Post - Partial update of a post
- ✅ Patch User - Partial update of a user
- ✅ HTTPBin Patch - Generic PATCH request

### DELETE Requests (3 examples)
- ✅ Delete Post - Deletes a post
- ✅ Delete User - Deletes a user
- ✅ HTTPBin Delete - Generic DELETE request

### Other Methods
- ✅ HEAD Request - HTTPBin Head
- ✅ OPTIONS Request - HTTPBin Options
- ✅ POST Form Data - Form-encoded POST example

## Total: 25+ Sample Requests

## API Sources Used

1. **JSONPlaceholder** (https://jsonplaceholder.typicode.com)
   - Fake REST API for testing
   - Supports GET, POST, PUT, PATCH, DELETE
   - No authentication required

2. **ReqRes** (https://reqres.in)
   - REST API for testing
   - User management endpoints
   - Login/authentication examples

3. **HTTPBin** (https://httpbin.org)
   - HTTP testing service
   - Supports all HTTP methods
   - Various testing endpoints (status codes, delays, etc.)

## How to Use

1. **Start the application**: `mvn spring-boot:run`
2. **View the collection**: 
   - Go to "Collections" page
   - Click on "Sample APIs - All Methods"
   - Or see all requests in the left sidebar
3. **Test an API**:
   - Click on any saved request in the sidebar
   - It will load into the main form
   - Click "Send Request" to execute
   - View the response

## Features Demonstrated

- ✅ All HTTP methods (GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS)
- ✅ JSON request bodies
- ✅ Form-encoded data
- ✅ Query parameters
- ✅ Status code handling (200, 404, etc.)
- ✅ Error responses
- ✅ Delayed responses
- ✅ Authentication examples

## Notes

- The sample collection is created automatically on first startup
- If the collection already exists, it won't be recreated (prevents duplicates)
- All APIs are publicly accessible test APIs
- No authentication required for most endpoints
- These are real working APIs you can test immediately

## Customization

You can:
- Edit any request
- Add more requests to the collection
- Create your own collections
- Delete the sample collection if not needed
- Use these as templates for your own APIs

