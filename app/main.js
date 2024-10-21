
// main.js
const { app, BrowserWindow } = require('electron');
const path = require('path');
const { exec } = require('child_process');

function createWindow() {
  const win = new BrowserWindow({
    width: 1200,
    height: 900,
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: false,
    },
  });

  // Load the local server
  win.loadURL('http://localhost:30031');
  // 打开开发者工具
  win.webContents.openDevTools();
}

app.whenReady().then(() => {
  // Start the server
  exec('node server.js', (err, stdout, stderr) => {
    if (err) {
      console.error(`Error starting server: ${stderr}`);
      return;
    }
    console.log(stdout);
  });

  createWindow();
});

// Quit when all windows are closed
app.on('window-all-closed', () => {
  app.quit();
});
