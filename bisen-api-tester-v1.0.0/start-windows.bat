@echo off
title BISEN API Tester
echo.
echo ========================================
echo   BISEN API Tester - Starting...
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo.
    echo Please install Java 17 or higher from:
    echo https://www.oracle.com/java/technologies/downloads/
    echo.
    pause
    exit /b 1
)

REM Get the directory where the batch file is located
cd /d "%~dp0"

REM Check if JAR file exists
if not exist "rest-api-tester-1.0.0.jar" (
    echo ERROR: rest-api-tester-1.0.0.jar not found!
    echo Please make sure the JAR file is in the same folder as this script.
    echo.
    pause
    exit /b 1
)

echo Starting BISEN API Tester...
echo.
echo The application will open in your browser at: http://localhost:8080
echo.
echo Press Ctrl+C to stop the application.
echo.

REM Start the application
java -jar rest-api-tester-1.0.0.jar

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Application failed to start
    echo.
    pause
)

