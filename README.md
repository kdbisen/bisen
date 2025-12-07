# REST API Tester

A simple, clean, and powerful Postman alternative built with Spring Boot, H2 Database, and Thymeleaf.

## Features

- ✅ Support for all HTTP methods (GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS)
- ✅ Custom headers support
- ✅ Request body support (JSON, XML, etc.)
- ✅ **Collections** - Organize API requests into collections (like Postman)
- ✅ Request history with H2 database
- ✅ Clean and modern UI
- ✅ Response time tracking
- ✅ Status code visualization
- ✅ Request/Response viewing

## Tech Stack

- **Spring Boot 3.2.0**
- **H2 Database** (in-memory/file-based)
- **Thymeleaf** (templating engine)
- **Spring Data JPA**
- **Java 17**

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Running the Application

1. Navigate to the project directory:
```bash
cd rest-api-tester
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

4. Open your browser and navigate to:
```
http://localhost:8080
```

### H2 Console

Access the H2 database console at:
```
http://localhost:8080/h2-console
```

- JDBC URL: `jdbc:h2:file:./data/resttester`
- Username: `sa`
- Password: (leave empty)

## Usage

1. **Make a Request:**
   - Select HTTP method (GET, POST, PUT, DELETE, etc.)
   - Enter the URL
   - Add headers (one per line, format: `Key: Value`)
   - Add request body (for POST, PUT, PATCH)
   - Optionally select a collection to save the request to
   - Click "Send Request"

2. **Collections:**
   - Click "Collections" to manage your API collections
   - Create new collections to organize your requests
   - View all requests in a collection
   - Edit or delete collections

3. **View History:**
   - Click "View History" to see all previous requests
   - Click on any request to view details
   - Delete requests you no longer need

## Project Structure

```
rest-api-tester/
├── src/
│   ├── main/
│   │   ├── java/com/resttester/
│   │   │   ├── RestApiTesterApplication.java
│   │   │   ├── controller/
│   │   │   │   └── ApiTesterController.java
│   │   │   ├── service/
│   │   │   │   └── ApiRequestService.java
│   │   │   ├── repository/
│   │   │   │   └── ApiRequestRepository.java
│   │   │   ├── model/
│   │   │   │   └── ApiRequest.java
│   │   │   └── dto/
│   │   │       ├── RequestDto.java
│   │   │       └── ResponseDto.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── templates/
│   │           ├── index.html
│   │           └── history.html
│   └── test/
└── pom.xml
```

## Database & Data Persistence

The application uses **H2 file-based database** to store all your data:
- ✅ **Request History** - All executed API requests
- ✅ **Saved Requests** - Your request templates
- ✅ **Collections** - Your API collections

### Data Location

All data is stored in the `./data/` directory:
- Database file: `./data/resttester.mv.db`
- Trace file: `./data/resttester.trace.db` (if enabled)

**Important:** Your data **persists across application restarts**. The database file is automatically created on first run and all your data will be available when you restart the application.

### Backup Your Data

To backup your data, simply copy the `./data/` directory:
```bash
# Backup
cp -r data/ data-backup/

# Restore (if needed)
cp -r data-backup/ data/
```

### H2 Console Access

You can access the H2 database console to view/manage data directly:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/resttester`
- Username: `sa`
- Password: (leave empty)

**Note:** The `data/` directory is in `.gitignore` to prevent committing database files to version control.

## License

This project is open source and available for personal and commercial use.

