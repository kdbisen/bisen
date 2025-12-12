# BISEN API Tester - Electron Desktop App

This directory contains the Electron wrapper to run BISEN API Tester as a desktop application.

## Prerequisites

1. **Node.js and npm** (v16 or higher)
   - Download from: https://nodejs.org/

2. **Java 17 or higher**
   - Required to run the Spring Boot server
   - Download from: https://adoptium.net/

3. **Built JAR file**
   - The `rest-api-tester-1.0.0.jar` must be built first
   - Run `mvn clean package` in the project root

## Installation

1. Install Electron dependencies:
```bash
cd electron
npm install
```

## Development

Run the app in development mode:
```bash
npm start
```

This will:
- Start the Spring Boot server in the background
- Open an Electron window pointing to `http://localhost:2000`
- Show server logs in the console

## Building Desktop Applications

### Build for Current Platform
```bash
npm run build
```

### Build for Specific Platforms

**macOS:**
```bash
npm run build:mac
```

**Windows:**
```bash
npm run build:win
```

**Linux:**
```bash
npm run build:linux
```

Built applications will be in the `electron/dist` directory.

## How It Works

1. **main.js**: 
   - Starts the Spring Boot server as a child process
   - Creates an Electron BrowserWindow
   - Waits for the server to be ready
   - Loads `http://localhost:2000` in the window
   - Handles server shutdown when the app closes

2. **preload.js**: 
   - Provides a secure bridge between the main process and renderer
   - Can expose safe APIs to the web page if needed

3. **package.json**: 
   - Contains Electron and build configuration
   - Defines build scripts for different platforms

## Troubleshooting

### JAR file not found
- Ensure you've built the JAR: `mvn clean package`
- The JAR should be at: `../target/rest-api-tester-1.0.0.jar`
- Or copy it to the `electron` directory

### Java not found
- Ensure Java 17+ is installed
- Verify Java is in your PATH: `java -version`
- On macOS, you may need to set JAVA_HOME

### Port 2000 already in use
- Close any other instances of the API tester
- Or change the port in `application.yml` and update `main.js`

### Build fails
- Ensure all dependencies are installed: `npm install`
- Check that electron-builder is installed: `npm list electron-builder`
- Try cleaning: `rm -rf node_modules && npm install`

## Distribution

After building, you can distribute the app from the `dist` directory:
- **macOS**: `.dmg` or `.app` bundle
- **Windows**: `.exe` installer
- **Linux**: `.AppImage` or `.deb` package

## Notes

- The server runs on `localhost:2000` by default
- The app automatically starts the server when launched
- The server stops when the app is closed
- External links open in the default browser
- DevTools can be opened in development mode

