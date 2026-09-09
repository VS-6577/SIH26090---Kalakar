// KALAKRITI — AI Studio Express Backend Server
// Proxies Android mobile application & frontend requests to AI APIs securely.
// API keys remain strictly server-side.

import 'dotenv/config';
import express from 'express';
import cors from 'cors';
import os from 'os';
import { analyzeProductRouter } from './routes/analyze.js';
import { contentRouter } from './routes/content.js';
import { photoshootRouter } from './routes/photoshoot.js';
import { imageGenRouter } from './routes/imageGen.js';
import { transcribeRouter } from './routes/transcribe.js';

const app = express();
const PORT = parseInt(process.env.PORT || '3001', 10);

// Detect LAN IP addresses for easy mobile client configuration
function getLanIps() {
  const interfaces = os.networkInterfaces();
  const ips = [];
  for (const name of Object.keys(interfaces)) {
    for (const iface of interfaces[name] || []) {
      if (iface.family === 'IPv4' && !iface.internal) {
        ips.push(iface.address);
      }
    }
  }
  return ips;
}

// --- Middleware ---
app.use(cors({
  origin: true,
  methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization', 'Accept']
}));

// Increase payload limit for base64 image data (5 images × ~1-2MB each)
app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ extended: true, limit: '50mb' }));

// Request logger
app.use((req, _res, next) => {
  const timestamp = new Date().toISOString().split('T')[1].slice(0, 8);
  console.log(`[${timestamp}] ${req.method} ${req.originalUrl}`);
  next();
});

// --- Root Status & Diagnostic Landing Page ---
app.get('/', (req, res) => {
  const lanIps = getLanIps();
  const primaryLan = lanIps[0] || 'localhost';
  const hasGemini = !!process.env.GEMINI_API_KEY && process.env.GEMINI_API_KEY !== 'your_gemini_api_key_here';
  const hasHf = !!process.env.HF_TOKEN && process.env.HF_TOKEN !== 'hf_your_token_here';

  res.send(`
    <!DOCTYPE html>
    <html lang="en">
      <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>KALAKRITI AI Studio Backend</title>
        <style>
          body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #faf8f5; color: #2d2926; padding: 32px 16px; margin: 0; }
          .card { background: white; max-width: 580px; margin: 0 auto; padding: 28px; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.06); border: 1px solid #ede8e1; }
          h2 { color: #8a4b12; margin-top: 0; font-size: 24px; }
          .badge { display: inline-block; padding: 4px 10px; border-radius: 12px; font-size: 12px; font-weight: 600; margin-right: 6px; }
          .badge-green { background: #e6f4ea; color: #137333; }
          .badge-amber { background: #fef7e0; color: #b06000; }
          .endpoint-box { background: #f5f2eb; padding: 12px 16px; border-radius: 8px; font-family: monospace; font-size: 13px; margin: 12px 0; word-break: break-all; }
          ul { padding-left: 20px; line-height: 1.6; }
        </style>
      </head>
      <body>
        <div class="card">
          <h2>KALAKRITI AI Studio Backend</h2>
          <p>The Express REST API server is running and ready for Android and Web requests.</p>
          <div>
            <span class="badge ${hasGemini ? 'badge-green' : 'badge-amber'}">Gemini: ${hasGemini ? 'Configured' : 'Placeholder'}</span>
            <span class="badge ${hasHf ? 'badge-green' : 'badge-amber'}">Hugging Face: ${hasHf ? 'Configured' : 'Placeholder'}</span>
          </div>
          <h3>Android Connection Endpoints:</h3>
          <div class="endpoint-box">
            <div><strong>Physical Mobile (Wi-Fi):</strong> http://${primaryLan}:${PORT}/</div>
            <div><strong>Android Emulator:</strong> http://10.0.2.2:${PORT}/</div>
            <div><strong>ADB Reverse USB:</strong> http://localhost:${PORT}/</div>
          </div>
          <h3>Active Routes:</h3>
          <ul>
            <li><code>GET  /api/health</code> — Health & service status</li>
            <li><code>POST /api/product-sessions/:id/analyze</code> — 5-photo visual analysis</li>
            <li><code>POST /api/product-sessions/:id/generate-content</code> — Multilingual catalog descriptions</li>
            <li><code>POST /api/product-sessions/:id/photoshoot</code> — AI photoshoot shot prompts</li>
            <li><code>POST /api/product-sessions/:id/generate-single-image</code> — Qwen image edit mockup</li>
            <li><code>POST /api/product-sessions/:id/transcribe-voice</code> — Multilingual voice transcription</li>
          </ul>
        </div>
      </body>
    </html>
  `);
});

app.get('/api/health', (_req, res) => {
  const lanIps = getLanIps();
  const hasGemini = !!process.env.GEMINI_API_KEY && process.env.GEMINI_API_KEY !== 'your_gemini_api_key_here';
  const hasHf = !!process.env.HF_TOKEN && process.env.HF_TOKEN !== 'hf_your_token_here';
  const hasOpenRouter = !!process.env.OPENROUTER_API_KEY && process.env.OPENROUTER_API_KEY !== 'your_openrouter_api_key_here';

  res.json({
    status: 'ok',
    service: 'kalakriti-backend',
    version: '1.0.0',
    port: PORT,
    uptimeSeconds: Math.floor(process.uptime()),
    lanIps,
    suggestedMobileUrl: lanIps[0] ? `http://${lanIps[0]}:${PORT}/` : `http://localhost:${PORT}/`,
    geminiKeyConfigured: hasGemini,
    hfTokenConfigured: hasHf,
    openRouterKeyConfigured: hasOpenRouter
  });
});

// --- API Routes ---
app.use('/api/product-sessions', analyzeProductRouter);
app.use('/api/product-sessions', contentRouter);
app.use('/api/product-sessions', photoshootRouter);
app.use('/api/product-sessions', imageGenRouter);
app.use('/api/product-sessions', transcribeRouter);

// --- 404 Handler ---
app.use((req, res) => {
  res.status(404).json({
    success: false,
    error: `Route not found: ${req.method} ${req.originalUrl}`
  });
});

// --- Global Error Handler ---
app.use((err, req, res, _next) => {
  console.error(`[ERROR] Unhandled exception on ${req.method} ${req.url}:`, err);
  res.status(err.status || 500).json({
    success: false,
    error: err.message || 'Internal server error'
  });
});

// --- Server Startup ---
const server = app.listen(PORT, '0.0.0.0', () => {
  const lanIps = getLanIps();
  const hasGemini = !!process.env.GEMINI_API_KEY && process.env.GEMINI_API_KEY !== 'your_gemini_api_key_here';
  const hasHf = !!process.env.HF_TOKEN && process.env.HF_TOKEN !== 'hf_your_token_here';

  console.log(`\n  KALAKRITI AI Studio Backend`);
  console.log(`  ──────────────────────────────────────────────────────────`);
  console.log(`  Local:           http://localhost:${PORT}`);
  lanIps.forEach(ip => {
    console.log(`  Mobile (Wi-Fi):  http://${ip}:${PORT}`);
  });
  console.log(`  Emulator:        http://10.0.2.2:${PORT}`);
  console.log(`  Health API:      http://localhost:${PORT}/api/health`);
  console.log(`  ──────────────────────────────────────────────────────────`);
  console.log(`  Gemini:          ${hasGemini ? '✓ Key configured' : '⚠ Placeholder or missing in .env'}`);
  console.log(`  Hugging Face:    ${hasHf ? '✓ Token configured' : '⚠ Placeholder or missing in .env'}`);
  console.log(`  Ready for Android & Web client connections.\n`);
});

// Graceful shutdown
const shutdown = () => {
  console.log('\nShutting down server gracefully...');
  server.close(() => {
    console.log('Backend server stopped.');
    process.exit(0);
  });
};

process.on('SIGTERM', shutdown);
process.on('SIGINT', shutdown);


