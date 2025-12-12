const { app, BrowserWindow, shell } = require('electron');
const path = require('path');
const { spawn } = require('child_process');
const fs = require('fs');

let mainWindow = null;
let serverProcess = null;
const SERVER_PORT = 2000;
const SERVER_URL = `http://localhost:${SERVER_PORT}`;
const JAR_PATH = path.join(__dirname, '..', 'target', 'rest-api-tester-1.0.0.jar');

// Check if JAR exists, if not, use the one in the app directory
const JAR_ALTERNATIVE = path.join(__dirname, 'rest-api-tester-1.0.0.jar');

function getJarPath() {
    if (fs.existsSync(JAR_PATH)) {
        return JAR_PATH;
    } else if (fs.existsSync(JAR_ALTERNATIVE)) {
        return JAR_ALTERNATIVE;
    }
    // Try to find JAR in common locations
    const possiblePaths = [
        path.join(process.resourcesPath, 'rest-api-tester-1.0.0.jar'),
        path.join(__dirname, '..', 'rest-api-tester-1.0.0.jar'),
        path.join(app.getAppPath(), 'rest-api-tester-1.0.0.jar')
    ];
    
    for (const possiblePath of possiblePaths) {
        if (fs.existsSync(possiblePath)) {
            return possiblePath;
        }
    }
    
    return null;
}

function startServer() {
    const jarPath = getJarPath();
    
    if (!jarPath) {
        console.error('JAR file not found!');
        if (mainWindow) {
            mainWindow.loadURL(`data:text/html,<html><body style="font-family: Arial; padding: 40px; text-align: center;">
                <h1>Error: JAR file not found</h1>
                <p>Please ensure the rest-api-tester-1.0.0.jar file is available.</p>
                <p>Expected location: ${JAR_PATH}</p>
            </body></html>`);
        }
        return;
    }

    console.log('Starting server with JAR:', jarPath);
    
    // Start the Spring Boot server
    serverProcess = spawn('java', ['-jar', jarPath], {
        cwd: path.dirname(jarPath),
        stdio: 'pipe'
    });

    serverProcess.stdout.on('data', (data) => {
        console.log(`Server: ${data}`);
    });

    serverProcess.stderr.on('data', (data) => {
        console.error(`Server Error: ${data}`);
    });

    serverProcess.on('error', (error) => {
        console.error('Failed to start server:', error);
        if (mainWindow) {
            mainWindow.loadURL(`data:text/html,<html><body style="font-family: Arial; padding: 40px; text-align: center;">
                <h1>Error: Failed to start server</h1>
                <p>${error.message}</p>
                <p>Please ensure Java is installed and available in your PATH.</p>
            </body></html>`);
        }
    });

    serverProcess.on('exit', (code) => {
        console.log(`Server process exited with code ${code}`);
        if (code !== 0 && code !== null) {
            console.error('Server exited unexpectedly');
        }
    });
}

function waitForServer(callback, maxAttempts = 30) {
    let attempts = 0;
    const checkServer = () => {
        const http = require('http');
        const req = http.get(SERVER_URL, (res) => {
            if (res.statusCode === 200 || res.statusCode === 302 || res.statusCode === 404) {
                console.log('Server is ready!');
                callback();
            } else {
                attempts++;
                if (attempts < maxAttempts) {
                    setTimeout(checkServer, 1000);
                } else {
                    console.error('Server did not become ready in time');
                    callback();
                }
            }
        });
        
        req.on('error', () => {
            attempts++;
            if (attempts < maxAttempts) {
                setTimeout(checkServer, 1000);
            } else {
                console.error('Server did not become ready in time');
                callback();
            }
        });
        
        req.setTimeout(1000, () => {
            req.destroy();
            attempts++;
            if (attempts < maxAttempts) {
                setTimeout(checkServer, 1000);
            } else {
                console.error('Server did not become ready in time');
                callback();
            }
        });
    };
    
    checkServer();
}

function createWindow() {
    // Create the browser window
    mainWindow = new BrowserWindow({
        width: 1400,
        height: 900,
        minWidth: 1000,
        minHeight: 600,
        icon: path.join(__dirname, '..', 'src', 'main', 'resources', 'static', 'icon.png'),
        webPreferences: {
            nodeIntegration: false,
            contextIsolation: true,
            preload: path.join(__dirname, 'preload.js')
        },
        titleBarStyle: process.platform === 'darwin' ? 'hiddenInset' : 'default',
        show: false // Don't show until ready
    });

    // Show window when ready
    mainWindow.once('ready-to-show', () => {
        mainWindow.show();
        if (process.platform === 'darwin') {
            app.dock.show();
        }
    });

    // Start server and wait for it to be ready
    startServer();
    waitForServer(() => {
        if (mainWindow && !mainWindow.isDestroyed()) {
            mainWindow.loadURL(SERVER_URL);
        }
    });

    // Handle external links
    mainWindow.webContents.setWindowOpenHandler(({ url }) => {
        shell.openExternal(url);
        return { action: 'deny' };
    });

    // Prevent navigation to external URLs
    mainWindow.webContents.on('will-navigate', (event, navigationUrl) => {
        const parsedUrl = new URL(navigationUrl);
        if (parsedUrl.origin !== SERVER_URL) {
            event.preventDefault();
            shell.openExternal(navigationUrl);
        }
    });

    // Open DevTools in development
    if (process.env.NODE_ENV === 'development') {
        mainWindow.webContents.openDevTools();
    }

    mainWindow.on('closed', () => {
        mainWindow = null;
    });
}

// This method will be called when Electron has finished initialization
app.whenReady().then(() => {
    createWindow();

    app.on('activate', () => {
        if (BrowserWindow.getAllWindows().length === 0) {
            createWindow();
        }
    });
});

// Quit when all windows are closed
app.on('window-all-closed', () => {
    // Stop the server process
    if (serverProcess) {
        console.log('Stopping server...');
        serverProcess.kill();
        serverProcess = null;
    }
    
    // On macOS, keep app running even when all windows are closed
    if (process.platform !== 'darwin') {
        app.quit();
    }
});

// Handle app quit
app.on('before-quit', () => {
    if (serverProcess) {
        console.log('Stopping server before quit...');
        serverProcess.kill();
        serverProcess = null;
    }
});

// Handle uncaught exceptions
process.on('uncaughtException', (error) => {
    console.error('Uncaught Exception:', error);
});

