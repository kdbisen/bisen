#!/bin/bash

# BISEN API Tester Startup Script for macOS/Linux

echo ""
echo "========================================"
echo "  BISEN API Tester - Starting..."
echo "========================================"
echo ""

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo ""
    echo "Please install Java 17 or higher:"
    echo "  macOS: brew install openjdk@17"
    echo "  Linux: sudo apt-get install openjdk-17-jdk"
    echo ""
    read -p "Press Enter to exit..."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "ERROR: Java 17 or higher is required. Found Java $JAVA_VERSION"
    echo ""
    read -p "Press Enter to exit..."
    exit 1
fi

# Find JAR file (supports version numbers)
JAR_FILE=$(ls rest-api-tester-*.jar 2>/dev/null | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "ERROR: JAR file not found!"
    echo "Please make sure the JAR file is in the same folder as this script."
    echo ""
    read -p "Press Enter to exit..."
    exit 1
fi

echo "Starting BISEN API Tester..."
echo ""
echo "The application will open in your browser at: http://localhost:2000"
echo ""
echo "Press Ctrl+C to stop the application."
echo ""

# Start the application
java -jar "$JAR_FILE"

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Application failed to start"
    echo ""
    read -p "Press Enter to exit..."
fi

