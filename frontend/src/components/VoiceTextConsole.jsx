import React, { useState, useEffect, useRef } from 'react';
import { Mic, Send, Square, Edit2, Check, RotateCcw, Sparkles, Globe, AlertCircle, Loader2 } from 'lucide-react';
import { transcribeVoiceAudio } from '../services/aiPipeline';

const LANGUAGES = [
  { code: 'hi-IN', label: 'हिंदी', sublabel: 'Hindi' },
  { code: 'en-IN', label: 'Hinglish', sublabel: 'India' },
  { code: 'en-US', label: 'English', sublabel: 'Global' }
];

const SAMPLE_PROMPTS = [
  'Ye basket natural bamboo se bana hai aur isme 2 sturdy handles hain. Banane me 2 din lagte hain.',
  'Handcrafted terracotta clay pot with traditional etched motifs, suitable for organic cooking.',
  'Hand-carved rosewood elephant figurine with polished beeswax finish and floral detailing.'
];

export default function VoiceTextConsole({
  defaultTranscript = '',
  onConfirmVoiceText,
  isDisabled = false,
  isGenerating = false
}) {
  const [textInput, setTextInput] = useState('');
  const [isRecording, setIsRecording] = useState(false);
  const [isAiTranscribing, setIsAiTranscribing] = useState(false);
  const [recordSeconds, setRecordSeconds] = useState(0);
  const [transcript, setTranscript] = useState(defaultTranscript || '');
  const [interimText, setInterimText] = useState('');
  const [isEditingTranscript, setIsEditingTranscript] = useState(false);
  const [selectedLang, setSelectedLang] = useState('hi-IN');
  const [errorMessage, setErrorMessage] = useState(null);
  const [recordedAudioData, setRecordedAudioData] = useState(null);

  const recognitionRef = useRef(null);
  const mediaRecorderRef = useRef(null);
  const audioChunksRef = useRef([]);
  const timerRef = useRef(null);
  const isRecordingRef = useRef(false);
  const transcriptRef = useRef('');

  // Synchronize transcriptRef when transcript state changes externally
  useEffect(() => {
    transcriptRef.current = transcript;
  }, [transcript]);

  // Keep isRecordingRef in sync for SpeechRecognition callbacks
  useEffect(() => {
    isRecordingRef.current = isRecording;
  }, [isRecording]);

  // Cleanup on unmount
  useEffect(() => {
    return () => {
      if (timerRef.current) clearInterval(timerRef.current);
      if (recognitionRef.current) {
        try { recognitionRef.current.abort(); } catch (e) {}
      }
      if (mediaRecorderRef.current && mediaRecorderRef.current.state !== 'inactive') {
        try { mediaRecorderRef.current.stop(); } catch (e) {}
      }
    };
  }, []);

  // Timer effect during recording
  useEffect(() => {
    if (isRecording) {
      setRecordSeconds(0);
      timerRef.current = setInterval(() => {
        setRecordSeconds(prev => prev + 1);
      }, 1000);
    } else {
      if (timerRef.current) clearInterval(timerRef.current);
    }
    return () => {
      if (timerRef.current) clearInterval(timerRef.current);
    };
  }, [isRecording]);

  const formatTimer = (sec) => {
    const mins = Math.floor(sec / 60).toString().padStart(2, '0');
    const secs = (sec % 60).toString().padStart(2, '0');
    return `${mins}:${secs}`;
  };

  /**
   * Start dual-engine recording:
   * 1. Browser SpeechRecognition for instantaneous live text streaming.
   * 2. MediaRecorder for full-fidelity audio capture (with Gemini 3 AI transcription fallback).
   */
  const startRecording = async () => {
    if (isDisabled || isGenerating || isRecording || isAiTranscribing) return;

    setErrorMessage(null);
    setTranscript('');
    transcriptRef.current = '';
    setInterimText('');
    setRecordedAudioData(null);
    audioChunksRef.current = [];

    // --- 1. Initialize & Start MediaRecorder for audio capture ---
    let audioStream = null;
    try {
      if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
        audioStream = await navigator.mediaDevices.getUserMedia({ audio: true });
        const mediaRecorder = new MediaRecorder(audioStream);
        mediaRecorderRef.current = mediaRecorder;

        mediaRecorder.ondataavailable = (e) => {
          if (e.data && e.data.size > 0) {
            audioChunksRef.current.push(e.data);
          }
        };

        mediaRecorder.start(250); // Capture in 250ms time slices
      }
    } catch (micErr) {
      console.warn('Microphone stream access error:', micErr);
      if (micErr.name === 'NotAllowedError' || micErr.name === 'PermissionDeniedError') {
        setErrorMessage('Microphone access blocked. Please allow microphone permissions in your browser.');
        return;
      }
    }

    // --- 2. Initialize & Start Web Speech Recognition ---
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (SpeechRecognition) {
      try {
        const recognition = new SpeechRecognition();
        recognition.continuous = true;
        recognition.interimResults = true;
        recognition.lang = selectedLang;

        recognition.onresult = (event) => {
          let finalAccumulated = '';
          let currentInterim = '';

          for (let i = 0; i < event.results.length; i++) {
            const res = event.results[i];
            if (res.isFinal) {
              finalAccumulated += res[0].transcript + ' ';
            } else {
              currentInterim += res[0].transcript;
            }
          }

          const trimmedFinal = finalAccumulated.trim();
          const trimmedInterim = currentInterim.trim();

          setInterimText(trimmedInterim);

          if (trimmedFinal) {
            transcriptRef.current = trimmedFinal;
            setTranscript(trimmedFinal);
          } else if (trimmedInterim) {
            transcriptRef.current = trimmedInterim;
          }
        };

        recognition.onerror = (event) => {
          console.warn('Speech recognition event error:', event.error);
          if (event.error === 'not-allowed') {
            setErrorMessage('Microphone permission was denied.');
          } else if (event.error === 'network') {
            console.log('Web Speech network issue; Gemini AI audio fallback will handle transcription.');
          }
        };

        recognition.onend = () => {
          // If the browser prematurely ends continuous recognition while we are still recording, restart it
          if (isRecordingRef.current) {
            try {
              recognition.start();
            } catch (err) {
              // Ignore restart collision
            }
          }
        };

        recognitionRef.current = recognition;
        recognition.start();
      } catch (recErr) {
        console.warn('Web Speech API initialization warning:', recErr);
      }
    }

    setIsRecording(true);
    isRecordingRef.current = true;
  };

  /**
   * Stop recording:
   * Stops both engines. If speech recognition captured text, use it.
   * If not, automatically invoke Gemini 3 AI voice transcription on the captured audio.
   */
  const stopRecording = () => {
    if (!isRecording) return;
    setIsRecording(false);
    isRecordingRef.current = false;
    setInterimText('');

    // Stop SpeechRecognition
    if (recognitionRef.current) {
      try {
        recognitionRef.current.stop();
      } catch (e) {}
    }

    // Stop MediaRecorder & process audio blob
    const mediaRecorder = mediaRecorderRef.current;
    if (mediaRecorder && mediaRecorder.state !== 'inactive') {
      mediaRecorder.onstop = async () => {
        try {
          const mimeType = mediaRecorder.mimeType || 'audio/webm';
          const audioBlob = new Blob(audioChunksRef.current, { type: mimeType });

          // Convert blob to base64
          const reader = new FileReader();
          reader.onloadend = async () => {
            const base64DataUrl = reader.result;
            setRecordedAudioData({ base64: base64DataUrl, mimeType });

            // If browser recognition did not capture anything, run Gemini 3 transcription automatically!
            if (!transcriptRef.current.trim() && audioBlob.size > 500) {
              await runGeminiTranscription(base64DataUrl, mimeType);
            } else if (!transcriptRef.current.trim()) {
              setErrorMessage('No audible speech was detected. Please speak closer to your microphone or type below.');
            }
          };
          reader.readAsDataURL(audioBlob);

          // Stop all audio stream tracks
          if (mediaRecorder.stream) {
            mediaRecorder.stream.getTracks().forEach(track => track.stop());
          }
        } catch (procErr) {
          console.error('Audio processing error on stop:', procErr);
        }
      };

      try {
        mediaRecorder.stop();
      } catch (e) {}
    } else {
      // Fallback check if mediaRecorder was not active
      if (!transcriptRef.current.trim()) {
        setErrorMessage('No speech detected. Please speak into your microphone or type below.');
      }
    }
  };

  /**
   * Transcribes recorded audio via Gemini 3 AI backend endpoint
   */
  const runGeminiTranscription = async (audioDataUrl, mimeType) => {
    setIsAiTranscribing(true);
    setErrorMessage(null);

    try {
      console.log('Transcribing voice audio via Gemini 3 AI service...');
      const result = await transcribeVoiceAudio({
        audioData: audioDataUrl,
        mimeType: mimeType || 'audio/webm',
        language: selectedLang
      });

      if (result && result.transcript && result.transcript.trim()) {
        const aiText = result.transcript.trim();
        setTranscript(aiText);
        transcriptRef.current = aiText;
        console.log('Gemini 3 voice transcription succeeded:', aiText);
      } else {
        if (!transcriptRef.current.trim()) {
          setErrorMessage('No clear speech was heard in the recording. Try speaking clearly or type your description.');
        }
      }
    } catch (err) {
      console.warn('Gemini AI transcription error:', err.message);
      if (!transcriptRef.current.trim()) {
        setErrorMessage('Could not transcribe audio. Please type your product details below.');
      }
    } finally {
      setIsAiTranscribing(false);
    }
  };

  const handleSendText = () => {
    if (!textInput.trim() || isDisabled || isGenerating) return;
    const val = textInput.trim();
    setTranscript(val);
    transcriptRef.current = val;
    setTextInput('');
    setErrorMessage(null);
  };

  const handleConfirmTranscript = () => {
    const textToConfirm = transcript.trim() || transcriptRef.current.trim();
    if (!textToConfirm || isDisabled || isGenerating) return;
    onConfirmVoiceText(textToConfirm);
  };

  const handleResetInput = () => {
    setTranscript('');
    transcriptRef.current = '';
    setInterimText('');
    setIsEditingTranscript(false);
    setRecordedAudioData(null);
    setErrorMessage(null);
  };

  const handleUseSamplePrompt = (sample) => {
    setTranscript(sample);
    transcriptRef.current = sample;
    setTextInput('');
    setErrorMessage(null);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
      
      {/* Section Header with Language Selector */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '8px' }}>
        <div className="heading-section" style={{ margin: 0 }}>
          TELL US ABOUT YOUR PRODUCT
        </div>

        {/* Language Selection Pills */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
          <Globe size={13} style={{ color: 'var(--accent-gold)', marginRight: '2px' }} />
          {LANGUAGES.map((lang) => {
            const isActive = selectedLang === lang.code;
            return (
              <button
                key={lang.code}
                onClick={() => setSelectedLang(lang.code)}
                disabled={isRecording || isGenerating}
                style={{
                  fontSize: '0.75rem',
                  padding: '3px 8px',
                  borderRadius: '12px',
                  border: isActive ? '1px solid var(--accent-gold)' : '1px solid var(--border-subtle)',
                  backgroundColor: isActive ? 'var(--accent-gold-soft, #fdf8eb)' : 'transparent',
                  color: isActive ? 'var(--accent-gold)' : 'var(--text-secondary)',
                  fontWeight: isActive ? 700 : 500,
                  cursor: isRecording || isGenerating ? 'not-allowed' : 'pointer',
                  transition: 'all 0.15s ease'
                }}
                title={`Speech Language: ${lang.sublabel}`}
              >
                {lang.label}
              </button>
            );
          })}
        </div>
      </div>

      {/* Error / Warning Alert Banner */}
      {errorMessage && (
        <div style={{
          backgroundColor: '#fef2f2',
          border: '1px solid #fecaca',
          color: '#991b1b',
          borderRadius: 'var(--radius-md)',
          padding: '10px 14px',
          fontSize: '0.82rem',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          gap: '8px'
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <AlertCircle size={15} style={{ flexShrink: 0 }} />
            <span>{errorMessage}</span>
          </div>
          <button
            onClick={() => setErrorMessage(null)}
            style={{ background: 'none', border: 'none', color: '#991b1b', cursor: 'pointer', fontSize: '0.8rem', fontWeight: 600 }}
          >
            ✕
          </button>
        </div>
      )}

      {/* 1. RECORDING IN PROGRESS STATE */}
      {isRecording ? (
        <div style={{
          background: 'var(--bg-app)',
          border: '1.5px solid var(--accent-gold)',
          borderRadius: 'var(--radius-lg)',
          padding: '18px 20px',
          display: 'flex',
          flexDirection: 'column',
          gap: '12px',
          boxShadow: '0 4px 16px rgba(150, 112, 56, 0.1)'
        }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              <div style={{
                width: '12px',
                height: '12px',
                borderRadius: '50%',
                backgroundColor: '#dc2626',
                animation: 'pulse 1s infinite'
              }} />
              <span style={{ fontSize: '0.9rem', fontWeight: 700, color: 'var(--accent-gold)' }}>
                Listening ({LANGUAGES.find(l => l.code === selectedLang)?.label})...
              </span>
              <span style={{ fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-muted)', fontFamily: 'monospace' }}>
                {formatTimer(recordSeconds)}
              </span>
            </div>

            <button
              onClick={stopRecording}
              className="btn btn-primary btn-sm"
              style={{ padding: '6px 14px', backgroundColor: '#dc2626', borderColor: '#b91c1c' }}
            >
              <Square size={12} style={{ fill: 'currentColor' }} />
              <span>Done Speaking</span>
            </button>
          </div>

          {/* Live speech transcription stream */}
          <div style={{
            backgroundColor: '#ffffff',
            border: '1px dashed var(--border-strong)',
            borderRadius: 'var(--radius-md)',
            padding: '12px 14px',
            minHeight: '48px',
            maxHeight: '120px',
            overflowY: 'auto',
            fontSize: '0.9rem',
            lineHeight: '1.5'
          }}>
            {transcript ? (
              <span style={{ color: 'var(--text-primary)', fontWeight: 500 }}>{transcript} </span>
            ) : null}
            {interimText ? (
              <span style={{ color: 'var(--accent-gold)', fontStyle: 'italic' }}>{interimText}</span>
            ) : null}
            {!transcript && !interimText && (
              <span style={{ color: 'var(--text-muted)', fontStyle: 'italic', fontSize: '0.85rem' }}>
                Speak clearly into your microphone... (e.g. material, craft technique, size)
              </span>
            )}
          </div>
        </div>
      ) : isAiTranscribing ? (
        /* 2. AI TRANSCRIPTION LOADING STATE */
        <div style={{
          background: 'var(--bg-app)',
          border: '1px solid var(--accent-gold)',
          borderRadius: 'var(--radius-lg)',
          padding: '24px 20px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          gap: '12px',
          textAlign: 'center'
        }}>
          <Loader2 size={20} className="spin" style={{ color: 'var(--accent-gold)' }} />
          <span style={{ fontSize: '0.9rem', fontWeight: 600, color: 'var(--accent-gold)' }}>
            Gemini 3 is transcribing and understanding your voice...
          </span>
        </div>
      ) : transcript ? (
        /* 3. TRANSCRIPT REVIEW & CONFIRMATION STATE */
        <div style={{
          background: 'var(--bg-app)',
          border: '1px solid var(--border-subtle)',
          borderRadius: 'var(--radius-lg)',
          padding: '18px 20px'
        }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <span style={{ fontSize: '0.7rem', fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
                ARTISAN STATEMENT
              </span>
              <span style={{ fontSize: '0.7rem', backgroundColor: 'var(--accent-gold-soft, #fef9ee)', color: 'var(--accent-gold)', padding: '2px 6px', borderRadius: '8px', fontWeight: 600 }}>
                {LANGUAGES.find(l => l.code === selectedLang)?.label}
              </span>
            </div>

            <div style={{ display: 'flex', gap: '6px' }}>
              {/* Gemini 3 AI Re-Transcribe / Enhance Button */}
              {recordedAudioData && !isEditingTranscript && (
                <button
                  onClick={() => runGeminiTranscription(recordedAudioData.base64, recordedAudioData.mimeType)}
                  disabled={isGenerating || isAiTranscribing}
                  className="btn btn-secondary btn-sm"
                  style={{ fontSize: '0.75rem', padding: '3px 8px', color: 'var(--accent-gold)' }}
                  title="Enhance accuracy with Gemini 3 AI"
                >
                  <Sparkles size={12} />
                  <span>Gemini 3 Enhance</span>
                </button>
              )}

              <button
                onClick={handleResetInput}
                disabled={isGenerating}
                className="btn btn-secondary btn-sm"
                style={{ fontSize: '0.75rem', padding: '3px 8px' }}
                title="Clear & restart"
              >
                <RotateCcw size={11} />
                <span>Re-record</span>
              </button>
            </div>
          </div>

          {!isEditingTranscript ? (
            <p style={{
              fontSize: '0.92rem',
              color: 'var(--text-primary)',
              fontStyle: 'italic',
              marginBottom: '16px',
              lineHeight: '1.55',
              backgroundColor: '#ffffff',
              padding: '12px 16px',
              borderRadius: 'var(--radius-md)',
              border: '1px solid var(--border-subtle)'
            }}>
              "{transcript}"
            </p>
          ) : (
            <textarea
              value={transcript}
              onChange={(e) => {
                setTranscript(e.target.value);
                transcriptRef.current = e.target.value;
              }}
              rows={3}
              placeholder="Edit your artisan statement..."
              style={{
                width: '100%',
                padding: '12px',
                fontSize: '0.9rem',
                marginBottom: '14px',
                borderRadius: 'var(--radius-sm)',
                border: '1.5px solid var(--accent-gold)',
                fontFamily: 'inherit',
                outline: 'none',
                resize: 'vertical'
              }}
            />
          )}

          <div style={{ display: 'flex', gap: '8px', justifyContent: 'flex-end', alignItems: 'center' }}>
            {!isEditingTranscript ? (
              <button
                onClick={() => setIsEditingTranscript(true)}
                disabled={isGenerating}
                className="btn btn-secondary btn-sm"
              >
                <Edit2 size={12} />
                <span>Edit Text</span>
              </button>
            ) : (
              <button
                onClick={() => setIsEditingTranscript(false)}
                className="btn btn-secondary btn-sm"
              >
                <Check size={12} />
                <span>Done Editing</span>
              </button>
            )}

            <button
              onClick={handleConfirmTranscript}
              disabled={!transcript.trim() || isDisabled || isGenerating}
              className="btn btn-primary btn-sm"
              style={{ padding: '8px 16px' }}
            >
              {isGenerating ? (
                <>
                  <Loader2 size={13} className="spin" />
                  <span>Generating Content...</span>
                </>
              ) : (
                <>
                  <Check size={13} />
                  <span>Confirm & Generate Content</span>
                </>
              )}
            </button>
          </div>
        </div>
      ) : (
        /* 4. DEFAULT UNIFIED INPUT STATE (Text input + inner Mic + Quick Prompts) */
        <div>
          <div style={{ display: 'flex', gap: '8px', alignItems: 'center', marginBottom: '8px' }}>
            <div style={{ position: 'relative', flex: 1 }}>
              <input
                type="text"
                placeholder="Type or click the mic to speak..."
                value={textInput}
                onChange={(e) => setTextInput(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleSendText()}
                disabled={isDisabled || isGenerating}
                style={{
                  width: '100%',
                  padding: '12px 16px',
                  paddingRight: '44px',
                  borderRadius: 'var(--radius-md)',
                  border: '1px solid var(--border-strong)',
                  fontSize: '0.9rem',
                  backgroundColor: '#FFFFFF',
                  outline: 'none'
                }}
              />

              {/* Mic Button inside input */}
              <button
                type="button"
                onClick={startRecording}
                disabled={isDisabled || isGenerating}
                title={`Click to speak in ${LANGUAGES.find(l => l.code === selectedLang)?.label}`}
                style={{
                  position: 'absolute',
                  right: '8px',
                  top: '50%',
                  transform: 'translateY(-50%)',
                  color: 'var(--accent-gold)',
                  backgroundColor: 'var(--accent-gold-soft, #fdf8eb)',
                  border: '1px solid var(--accent-gold)',
                  borderRadius: '50%',
                  cursor: isDisabled || isGenerating ? 'not-allowed' : 'pointer',
                  width: '32px',
                  height: '32px',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  transition: 'transform 0.15s ease'
                }}
              >
                <Mic size={17} />
              </button>
            </div>

            {/* Send Button */}
            <button
              onClick={handleSendText}
              disabled={!textInput.trim() || isDisabled || isGenerating}
              className="btn btn-primary"
              style={{ padding: '12px 16px' }}
              title="Use written text"
            >
              <Send size={16} />
            </button>
          </div>

          {/* Sample prompts for quick artisan input */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '4px', marginTop: '4px' }}>
            <span style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>
              Or click a sample description to test:
            </span>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
              {SAMPLE_PROMPTS.map((sample, idx) => (
                <button
                  key={idx}
                  onClick={() => handleUseSamplePrompt(sample)}
                  disabled={isDisabled || isGenerating}
                  style={{
                    fontSize: '0.72rem',
                    padding: '3px 8px',
                    borderRadius: 'var(--radius-sm)',
                    border: '1px solid var(--border-subtle)',
                    background: '#ffffff',
                    color: 'var(--text-secondary)',
                    cursor: 'pointer',
                    textAlign: 'left',
                    maxWidth: '100%',
                    whiteSpace: 'nowrap',
                    overflow: 'hidden',
                    textOverflow: 'ellipsis'
                  }}
                  title={sample}
                >
                  "{sample.substring(0, 45)}..."
                </button>
              ))}
            </div>
          </div>
        </div>
      )}

    </div>
  );
}
