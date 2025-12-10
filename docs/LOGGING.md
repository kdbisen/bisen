# BISEN Logging Configuration

**Author:** Kuldeep Bisen  
**Version:** 1.2 BETA

---

## Logging Overview

BISEN uses SLF4J with Logback for comprehensive logging. All logging is configured in `application.yml`.

---

## Log Levels

### Application Logs
- **Root:** INFO
- **com.resttester:** INFO
- **com.resttester.controller:** DEBUG (for API endpoint debugging)
- **com.resttester.service:** INFO
- **com.resttester.security:** WARN

### Framework Logs
- **org.springframework:** WARN
- **org.springframework.web:** INFO
- **org.hibernate:** WARN
- **org.hibernate.SQL:** DEBUG (SQL queries)
- **org.hibernate.type.descriptor.sql.BasicBinder:** TRACE (SQL parameters)

---

## Log Output

### Console Logging
- **Format:** `yyyy-MM-dd HH:mm:ss.SSS [thread] LEVEL logger - message`
- **Example:** `2024-12-07 17:30:45.123 [main] INFO  c.r.RestApiTesterApplication - Started application`

### File Logging
- **Location:** `~/.bisen-api-tester/bisen.log`
- **Max Size:** 10MB per file
- **Max History:** 30 files (keeps 30 days of logs)
- **Total Size Cap:** 300MB
- **Rotation:** Automatic when file reaches max size

---

## Log Categories

### Request Execution Logs
- **DEBUG:** Request details (method, URL)
- **INFO:** Successful requests (method, URL, status, time)
- **WARN:** HTTP errors (4xx, 5xx)
- **ERROR:** Exceptions and failures

### Security Logs
- **WARN:** Security validation failures
- **ERROR:** Security violations

### Database Logs
- **INFO:** Database directory creation
- **ERROR:** Database errors

### Import/Export Logs
- **INFO:** Successful imports/exports
- **ERROR:** Import/export failures

---

## Log Format

```
2024-12-07 17:30:45.123 [http-nio-8080-exec-1] INFO  c.r.s.ApiRequestService - Request completed: GET https://api.example.com/users - Status: 200 - Time: 245ms
```

**Components:**
- **Timestamp:** Date and time with milliseconds
- **Thread:** Thread name executing the code
- **Level:** Log level (DEBUG, INFO, WARN, ERROR)
- **Logger:** Abbreviated logger name
- **Message:** Log message

---

## Viewing Logs

### Console
Logs are displayed in the console where the application is running.

### Log File
View log file:
```bash
# macOS/Linux
cat ~/.bisen-api-tester/bisen.log

# Windows
type %USERPROFILE%\.bisen-api-tester\bisen.log
```

### Tail Logs (Real-time)
```bash
# macOS/Linux
tail -f ~/.bisen-api-tester/bisen.log

# Windows (PowerShell)
Get-Content ~/.bisen-api-tester/bisen.log -Wait -Tail 50
```

---

## Customizing Log Levels

Edit `application.yml`:

```yaml
logging:
  level:
    com.resttester: DEBUG  # Change to DEBUG for more verbose logs
    com.resttester.service: DEBUG
```

---

## Log Rotation

Logs are automatically rotated:
- When file reaches 10MB
- Old logs are compressed
- Maximum 30 files kept
- Total size limited to 300MB

---

## Sensitive Data Masking

All sensitive data is automatically masked in logs:
- Passwords
- API Keys
- Tokens
- Authorization headers

Example:
```
Before: Authorization: Bearer secret-token-123
After:  Authorization: Bearer ********
```

---

## Startup Logs

On application startup, you'll see:
```
========================================
BISEN API Testing Tool
Version: 1.2 BETA
Author: Kuldeep Bisen
========================================
Application started successfully!
Database location: /Users/username/.bisen-api-tester/bisen-api-tester.db
Log file location: /Users/username/.bisen-api-tester/bisen.log
Access application at: http://localhost:8080
========================================
```

---

## Troubleshooting

### Logs Not Appearing
- Check log file permissions
- Verify log directory exists: `~/.bisen-api-tester/`
- Check disk space

### Too Many Logs
- Increase log levels to WARN or ERROR
- Reduce `max-history` in `application.yml`

### Log File Too Large
- Reduce `max-size` in `application.yml`
- Reduce `max-history` to keep fewer files

---

*Logging Documentation by Kuldeep Bisen*


