// Preload script for Electron
// This runs in a context that has access to Node.js APIs
// but is isolated from the web page

const { contextBridge } = require('electron');

// Expose protected methods that allow the renderer process
// to use functionality from the main process
contextBridge.exposeInMainWorld('electronAPI', {
    platform: process.platform,
    version: process.versions.electron
});

