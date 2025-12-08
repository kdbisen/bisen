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

REM Find JAR file (supports version numbers)
set JAR_FILE=
for %%f in (rest-api-tester-*.jar) do set JAR_FILE=%%f

if "%JAR_FILE%"=="" (
    echo ERROR: JAR file not found!
    echo Please make sure the JAR file is in the same folder as this script.
    echo.
    pause
    exit /b 1
)

echo Starting BISEN API Tester...
echo.
echo The application will open in your browser at: http://localhost:2000
echo.
echo Press Ctrl+C to stop the application.
echo.

REM Start the application
java -jar "%JAR_FILE%"

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Application failed to start
    echo.
    pause
)

