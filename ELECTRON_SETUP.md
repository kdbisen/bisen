# BISEN API Tester - Desktop Application Setup

This guide will help you create a desktop application version of BISEN API Tester using Electron.

## What is This?

Electron allows you to wrap the BISEN API Tester web application into a native desktop application that:
- Runs in its own window (like a system app)
- Automatically starts the Spring Boot server
- Opens the app in a browser-like window
- Closes the server when you exit the app
- Works on Windows, macOS, and Linux

## Prerequisites

1. **Node.js** (v16 or higher)
   - Download: https://nodejs.org/
   - Verify: `node --version`

2. **Java 17+**
   - Download: https://adoptium.net/
   - Verify: `java -version`

3. **Maven** (to build the JAR)
   - Usually comes with the project
   - Verify: `mvn --version`

## Quick Start

### Option 1: Use the Quick Start Script

**macOS/Linux:**
```bash
cd rest-api-tester/electron
./quick-start.sh
```

**Windows:**
```cmd
cd rest-api-tester\electron
quick-start.bat
```

The script will:
- Check all prerequisites
- Build the JAR if needed
- Install Electron dependencies
- Start the desktop app

### Option 2: Manual Setup

1. **Build the JAR file:**
```bash
cd rest-api-tester
mvn clean package
```

2. **Install Electron dependencies:**
```bash
cd electron
npm install
```

3. **Start the app:**
```bash
npm start
```

## Building Distributable Apps

After testing, you can build platform-specific installers:

### Build for Your Current Platform
```bash
cd electron
npm run build
```

### Build for Specific Platforms

**macOS:**
```bash
npm run build:mac
```
Creates: `.dmg` installer and `.app` bundle

**Windows:**
```bash
npm run build:win
```
Creates: `.exe` installer (NSIS)

**Linux:**
```bash
npm run build:linux
```
Creates: `.AppImage` or `.deb` package

Built applications will be in `electron/dist/`

## How It Works

1. **main.js**: 
   - Starts the Spring Boot server as a background process
   - Creates an Electron window
   - Waits for server to be ready (checks `http://localhost:2000`)
   - Loads the app in the window
   - Stops the server when the app closes

2. **preload.js**: 
   - Secure bridge between Electron and web content
   - Can expose safe APIs if needed

3. **package.json**: 
   - Electron configuration
   - Build settings for different platforms

## File Structure

```
rest-api-tester/
├── electron/
│   ├── main.js          # Electron main process
│   ├── preload.js       # Preload script
│   ├── package.json     # Electron config
│   ├── quick-start.sh   # Quick start (macOS/Linux)
│   ├── quick-start.bat  # Quick start (Windows)
│   └── README.md        # Detailed docs
├── target/
│   └── rest-api-tester-1.0.0.jar  # Built JAR file
└── ...
```

## Troubleshooting

### "JAR file not found"
- Build the JAR: `mvn clean package`
- Ensure it's at: `target/rest-api-tester-1.0.0.jar`

### "Java not found"
- Install Java 17+ from https://adoptium.net/
- Verify: `java -version`
- On macOS, you may need to set `JAVA_HOME`

### "Port 2000 already in use"
- Close other instances of the API tester
- Or change port in `application.yml` and update `main.js`

### "npm install fails"
- Check Node.js version: `node --version` (should be 16+)
- Try: `npm cache clean --force`
- Delete `node_modules` and try again

### "Build fails"
- Ensure all dependencies installed: `npm install`
- Check electron-builder: `npm list electron-builder`
- Try: `rm -rf node_modules && npm install`

## Distribution

After building, distribute the files from `electron/dist/`:

- **macOS**: Share the `.dmg` file or `.app` bundle
- **Windows**: Share the `.exe` installer
- **Linux**: Share the `.AppImage` or `.deb` package

Users only need:
- The installer/package
- Java 17+ installed on their system

## Development Tips

- **DevTools**: Opens automatically in development mode
- **Server Logs**: Check the terminal where you ran `npm start`
- **Hot Reload**: Not available - restart the app to see changes
- **Debugging**: Use Chrome DevTools (View → Developer → Developer Tools)

## Next Steps

1. Test the app: `npm start`
2. Build for your platform: `npm run build`
3. Test the built app from `dist/`
4. Distribute to users!

## Notes

- The server runs on `localhost:2000` (not accessible from other machines)
- External links automatically open in the default browser
- The app window is resizable and remembers its size
- Server automatically stops when you close the app

Enjoy your desktop API tester! 🚀

