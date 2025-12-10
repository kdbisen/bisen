# Packaging Instructions for BISEN API Tester

## Creating Distribution Package

### Automatic Packaging (Recommended)

Run the packaging script:
```bash
./package-distribution.sh
```

This will:
1. Build the executable JAR file
2. Create a distribution folder
3. Copy all necessary files
4. Create a ZIP file ready for distribution

### Manual Packaging

1. **Build the JAR:**
   ```bash
   mvn clean package -DskipTests
   ```

2. **Create distribution folder:**
   ```bash
   mkdir bisen-api-tester-dist
   ```

3. **Copy files:**
   ```bash
   cp target/rest-api-tester-1.0.0.jar bisen-api-tester-dist/
   cp start-windows.bat bisen-api-tester-dist/
   cp start-mac.sh bisen-api-tester-dist/
   cp DISTRIBUTION_README.md bisen-api-tester-dist/README.md
   ```

4. **Create ZIP:**
   ```bash
   cd bisen-api-tester-dist
   zip -r ../bisen-api-tester-v1.0.0.zip .
   ```

## Distribution Package Contents

The final ZIP file should contain:
- `rest-api-tester-1.0.0.jar` - Main application (executable JAR with all dependencies)
- `start-windows.bat` - Windows startup script
- `start-mac.sh` - macOS/Linux startup script  
- `README.md` - User instructions
- `create-shortcut-windows.vbs` - Optional: Creates desktop shortcut on Windows

## File Sizes

- JAR file: ~60MB (includes all dependencies)
- Complete ZIP: ~58MB (compressed)

## Testing the Distribution

1. Extract the ZIP to a test folder
2. Run the appropriate startup script
3. Verify the application starts and opens at http://localhost:8080
4. Test basic functionality (create request, save, etc.)

## Notes

- The JAR file is a "fat JAR" containing all dependencies
- No installation required - just extract and run
- Java 17+ must be installed on the user's system
- All data is stored locally in `~/.bisen-api-tester/`


