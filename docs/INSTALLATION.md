# BISEN Installation Guide

**Author:** Kuldeep Bisen  
**Version:** 1.2 BETA

---

## System Requirements

- **Java:** 17 or higher
- **Maven:** 3.6 or higher
- **Operating System:** Windows, macOS, or Linux
- **Memory:** Minimum 512MB RAM
- **Disk Space:** 50MB for application + database

---

## Installation Methods

### Method 1: From Source Code

1. **Download or Clone**
   ```bash
   git clone <repository-url>
   cd rest-api-tester
   ```

2. **Build the Project**
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access Application**
   Open browser: `http://localhost:8080`

### Method 2: Using Executable JAR

1. **Build JAR**
   ```bash
   mvn clean package
   ```

2. **Run JAR**
   ```bash
   java -jar target/rest-api-tester-1.0.0.jar
   ```

3. **Access Application**
   Open browser: `http://localhost:8080`

### Method 3: Using Startup Scripts

#### Windows
1. Double-click `start-windows.bat`
2. Application will start automatically

#### macOS/Linux
1. Make script executable:
   ```bash
   chmod +x start-mac.sh
   ```
2. Run script:
   ```bash
   ./start-mac.sh
   ```

Or double-click `start-mac.command` on macOS.

---

## Database Setup

BISEN uses SQLite database. The database is automatically created on first run.

**Default Location:**
- **Windows:** `C:\Users\<username>\.bisen-api-tester\bisen-api-tester.db`
- **macOS/Linux:** `~/.bisen-api-tester/bisen-api-tester.db`

**Custom Location:**
Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:sqlite:/path/to/your/database.db
```

---

## Configuration

### Port Configuration

Default port: `8080`

To change port, edit `src/main/resources/application.yml`:
```yaml
server:
  port: 8081
```

### Database Configuration

Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:sqlite:${user.home}/.bisen-api-tester/bisen-api-tester.db
    driver-class-name: org.sqlite.JDBC
  jpa:
    database-platform: org.hibernate.community.dialect.SQLiteDialect
    hibernate:
      ddl-auto: update
```

---

## Verification

After installation, verify:

1. **Application Starts**
   - Check console for "Started RestApiTesterApplication"
   - No error messages

2. **Web Interface**
   - Open `http://localhost:8080`
   - Banner displays "BISEN 1.2 BETA"
   - Navigation menu visible

3. **Database Created**
   - Check database file exists at default location
   - File size > 0 bytes

---

## Troubleshooting

### Java Not Found

**Error:** `java: command not found`

**Solution:**
- Install Java 17 or higher
- Add Java to PATH
- Verify: `java -version`

### Port Already in Use

**Error:** `Port 8080 is already in use`

**Solution:**
- Change port in `application.yml`
- Or stop other application using port 8080

### Database Permission Error

**Error:** `Permission denied` when creating database

**Solution:**
- Check write permissions in home directory
- Create `.bisen-api-tester` directory manually
- Set proper permissions

### Maven Build Fails

**Error:** Build errors during `mvn clean install`

**Solution:**
- Check Java version: `java -version`
- Check Maven version: `mvn -version`
- Update Maven: `mvn -U clean install`
- Check internet connection (for dependencies)

---

## Uninstallation

1. **Stop Application**
   - Close browser
   - Stop Java process

2. **Remove Database (Optional)**
   ```bash
   rm ~/.bisen-api-tester/bisen-api-tester.db
   ```

3. **Remove Application Files**
   - Delete project directory
   - Or remove JAR file

---

## Next Steps

After installation:

1. Read [User Guide](../README.md#user-guide)
2. Create your first request
3. Set up environments
4. Create collections

---

*Installation Guide by Kuldeep Bisen*


