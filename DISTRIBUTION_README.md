# BISEN API Tester - Distribution Package

## Quick Start

### For Windows Users:
1. Double-click `start-windows.bat` to start the application
2. The application will open automatically at http://localhost:2000

### For macOS Users:
1. Double-click `start-mac.command` (or `start-mac.sh` in terminal)
2. The application will open automatically at http://localhost:2000

### For Linux Users:
1. Run in terminal: `./start-mac.sh`
2. The application will open automatically at http://localhost:2000

## Requirements

- **Java 17 or higher** must be installed on your system
- No other software installation required!

### Check Java Installation

**Windows:**
```cmd
java -version
```

**macOS/Linux:**
```bash
java -version
```

### Install Java (if not installed)

**Windows:**
- Download from: https://www.oracle.com/java/technologies/downloads/
- Or use: https://adoptium.net/

**macOS:**
```bash
brew install openjdk@17
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk
```

## Package Contents

```
bisen-api-tester-1.0.0/
├── rest-api-tester-1.0.0.jar    # Main application JAR file
├── start-windows.bat            # Windows startup script
├── start-mac.sh                 # macOS/Linux startup script (terminal)
├── start-mac.command            # macOS startup script (double-click)
├── DISTRIBUTION_README.md       # This file
└── docs/                        # Documentation folder
    ├── README.md
    ├── QUICK_START.md
    ├── FEATURES.md
    └── ...
```

## Usage

1. **Extract the ZIP file** to any folder on your computer
2. **Run the appropriate startup script** for your operating system:
   - Windows: Double-click `start-windows.bat`
   - macOS/Linux: Double-click `start-mac.sh` or run `./start-mac.sh` in terminal
3. **Open your browser** and go to: http://localhost:2000
4. **Start testing APIs!**

## Data Storage

All your data (collections, saved requests, history) is stored locally on your machine:
- **Location**: `~/.bisen-api-tester/bisen-api-tester.db`
- **Backup**: Simply copy the database file to backup all your data
- **Portable**: The database file can be moved between machines

## Stopping the Application

- Press `Ctrl+C` in the terminal/command prompt
- Or close the terminal window

## Troubleshooting

### "Java is not recognized"
- Java is not installed or not in your PATH
- Install Java 17+ and try again

### "Port 2000 is already in use"
- Another application is using port 2000
- Stop that application or change the port in the application configuration

### Application won't start
- Check that Java 17+ is installed: `java -version`
- Make sure the JAR file is in the same folder as the startup script
- Check the error message in the terminal for details

## Features

- ✅ Test all HTTP methods (GET, POST, PUT, DELETE, PATCH, etc.)
- ✅ Multiple authorization types (Basic, Bearer, API Key, Digest)
- ✅ SSL Certificate support
- ✅ Save requests to collections
- ✅ Import Swagger/OpenAPI specifications
- ✅ Request history
- ✅ Clean, modern UI

## Support

For issues or questions, please check the main README.md file.

---

**BISEN API Tester** - Powerful, Elegant & Simple API Testing Tool

