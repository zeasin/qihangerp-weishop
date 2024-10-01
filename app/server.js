// server.js
const express = require('express');
const path = require('path');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();
const PORT = 30031;

// Serve static files from the Vue app
app.use(express.static(path.join(__dirname, 'dist')));

// Proxy configuration
app.use('/prod-api/', createProxyMiddleware({
  target: 'http://121.229.189.144:8088/api/auth-api/', // 替换为您的后端 API 地址
  changeOrigin: true,
  pathRewrite: { '^/prod-api': '' }, // 如果需要，可以重写路径
}));

// Handle all requests to serve the index.html file
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'dist', 'index.html'));
});

app.listen(PORT, () => {
  console.log(`Server is running at http://localhost:${PORT}`);
});
