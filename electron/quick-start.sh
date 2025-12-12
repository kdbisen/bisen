#!/bin/bash

# Quick Start Script for BISEN API Tester Electron App
# This script helps you get started quickly

echo "=========================================="
echo "BISEN API Tester - Electron Desktop App"
echo "Quick Start Script"
echo "=========================================="
echo ""

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed!"
    echo "Please install Node.js from https://nodejs.org/"
    exit 1
fi

echo "✅ Node.js found: $(node --version)"

# Check if npm is installed
if ! command -v npm &> /dev/null; then
    echo "❌ npm is not installed!"
    exit 1
fi

echo "✅ npm found: $(npm --version)"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed!"
    echo "Please install Java 17+ from https://adoptium.net/"
    exit 1
fi

echo "✅ Java found: $(java -version 2>&1 | head -n 1)"

# Check if JAR file exists
JAR_PATH="../target/rest-api-tester-1.0.0.jar"
if [ ! -f "$JAR_PATH" ]; then
    echo "⚠️  JAR file not found at $JAR_PATH"
    echo "Building JAR file..."
    cd ..
    mvn clean package -q
    cd electron
    if [ ! -f "$JAR_PATH" ]; then
        echo "❌ Failed to build JAR file!"
        exit 1
    fi
    echo "✅ JAR file built successfully"
else
    echo "✅ JAR file found"
fi

# Check if node_modules exists
if [ ! -d "node_modules" ]; then
    echo "📦 Installing Electron dependencies..."
    npm install
    if [ $? -ne 0 ]; then
        echo "❌ Failed to install dependencies!"
        exit 1
    fi
    echo "✅ Dependencies installed"
else
    echo "✅ Dependencies already installed"
fi

echo ""
echo "=========================================="
echo "Starting BISEN API Tester..."
echo "=========================================="
echo ""

# Start the Electron app
npm start

