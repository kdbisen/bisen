@echo off
REM Quick Start Script for BISEN API Tester Electron App (Windows)
REM This script helps you get started quickly

echo ==========================================
echo BISEN API Tester - Electron Desktop App
echo Quick Start Script
echo ==========================================
echo.

REM Check if Node.js is installed
where node >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Node.js is not installed!
    echo Please install Node.js from https://nodejs.org/
    exit /b 1
)

echo [OK] Node.js found
node --version

REM Check if npm is installed
where npm >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] npm is not installed!
    exit /b 1
)

echo [OK] npm found
npm --version

REM Check if Java is installed
where java >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java is not installed!
    echo Please install Java 17+ from https://adoptium.net/
    exit /b 1
)

echo [OK] Java found
java -version

REM Check if JAR file exists
set JAR_PATH=..\target\rest-api-tester-1.0.0.jar
if not exist "%JAR_PATH%" (
    echo [WARNING] JAR file not found at %JAR_PATH%
    echo Building JAR file...
    cd ..
    call mvn clean package -q
    cd electron
    if not exist "%JAR_PATH%" (
        echo [ERROR] Failed to build JAR file!
        exit /b 1
    )
    echo [OK] JAR file built successfully
) else (
    echo [OK] JAR file found
)

REM Check if node_modules exists
if not exist "node_modules" (
    echo [INFO] Installing Electron dependencies...
    call npm install
    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Failed to install dependencies!
        exit /b 1
    )
    echo [OK] Dependencies installed
) else (
    echo [OK] Dependencies already installed
)

echo.
echo ==========================================
echo Starting BISEN API Tester...
echo ==========================================
echo.

REM Start the Electron app
call npm start

