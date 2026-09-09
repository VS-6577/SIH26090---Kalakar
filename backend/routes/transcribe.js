// SIH26090 — Voice Transcription Route
// POST /api/product-sessions/:id/transcribe-voice
// Transcribes artisan voice audio recordings via Gemini 3.

import { Router } from 'express';
import { transcribeAudio } from '../services/gemini.js';

export const transcribeRouter = Router();

transcribeRouter.post('/:id/transcribe-voice', async (req, res) => {
  const { id } = req.params;
  const { audioData, mimeType, language } = req.body;

  if (!audioData) {
    return res.status(400).json({
      success: false,
      error: 'No audio data provided.'
    });
  }

  try {
    console.log(`[${new Date().toISOString()}] Received voice audio for session ${id} (${language || 'auto'})...`);

    const transcript = await transcribeAudio({
      audioBase64: audioData,
      mimeType: mimeType || 'audio/webm',
      language: language || 'auto'
    });

    console.log(`[${new Date().toISOString()}] Voice transcription complete for session ${id}: "${transcript.substring(0, 50)}..."`);

    return res.json({
      success: true,
      sessionId: id,
      transcript,
      metadata: {
        model: process.env.GEMINI_MODEL || 'gemini-3',
        timestamp: new Date().toISOString()
      }
    });
  } catch (error) {
    console.error(`[${new Date().toISOString()}] Transcription error for session ${id}:`, error.message);
    return res.status(500).json({
      success: false,
      error: error.message || 'Failed to transcribe audio. Please try again or type manually.'
    });
  }
});
