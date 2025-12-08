# Building BISEN Distribution Package

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Build Commands

### Build JAR and Distribution ZIP

```bash
mvn clean package
```

This will:
1. Compile the source code
2. Create the executable JAR file: `target/rest-api-tester-1.0.0.jar`
3. Create the distribution ZIP: `target/bisen-api-tester-1.0.0.zip`

### Build Only JAR (without ZIP)

```bash
mvn clean package -DskipAssembly
```

### Build Only Distribution ZIP (if JAR already exists)

```bash
mvn assembly:single
```

## Distribution Package Contents

The ZIP file (`bisen-api-tester-1.0.0.zip`) contains:

```
bisen-api-tester-1.0.0/
├── rest-api-tester-1.0.0.jar    # Executable JAR file
├── start-windows.bat             # Windows startup script
├── start-mac.sh                  # macOS/Linux startup script
├── start-mac.command             # macOS double-click launcher
├── DISTRIBUTION_README.md        # User instructions
└── docs/                         # Documentation
    ├── README.md
    ├── QUICK_START.md
    ├── FEATURES.md
    ├── INSTALLATION.md
    └── ...
```

## Output Location

- **JAR File**: `target/rest-api-tester-1.0.0.jar`
- **Distribution ZIP**: `target/bisen-api-tester-1.0.0.zip`

## Testing the Distribution

1. Extract the ZIP file
2. Run the appropriate startup script:
   - Windows: `start-windows.bat`
   - macOS/Linux: `./start-mac.sh`
3. Open browser to http://localhost:2000

## Version Update

To update the version, change the `<version>` in `pom.xml`:

```xml
<version>1.0.0</version>
```

The distribution ZIP will automatically use this version in its name.

