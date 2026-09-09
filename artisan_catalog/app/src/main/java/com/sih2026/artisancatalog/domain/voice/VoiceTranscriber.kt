package com.sih2026.artisancatalog.domain.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

interface VoiceTranscriber {
    val isListening: StateFlow<Boolean>
    val transcribedText: StateFlow<String>
    val errorMessage: StateFlow<String?>
    val isSpeechAvailable: Boolean

    fun startListening(languageCode: String = "en-IN")
    fun stopListening()
    fun setManualText(text: String)
    fun simulateArtisanVoiceInput(presetIndex: Int = 0)
    fun destroy()
}

class AndroidVoiceTranscriber(
    private val context: Context
) : VoiceTranscriber {

    private val _isListening = MutableStateFlow(false)
    override val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _transcribedText = MutableStateFlow("")
    override val transcribedText: StateFlow<String> = _transcribedText.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    override val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null

    override val isSpeechAvailable: Boolean
        get() = SpeechRecognizer.isRecognitionAvailable(context)

    // Authentic artisan speech presets for offline demo mode and emulators
    private val demoSpeechPresets = listOf(
        "This is a handmade cotton bag with traditional embroidery. Made with natural dyes and sturdy straps.",
        "I crafted this four-piece terracotta chai kulhad set on my hand wheel using local riverbed clay.",
        "A hand-carved wooden spice box made from reclaimed teakwood with traditional floral engravings.",
        "यह एक हस्तनिर्मित शुद्ध सूती थैला है जिस पर हाथ से सुंदर कढ़ाई की गई है।",
        "यह लाल मिट्टी का हस्तनिर्मित कुल्हड़ सेट है जिसे भट्टी में पारंपरिक विधि से पकाया गया है।"
    )

    init {
        initRecognizer()
    }

    private fun initRecognizer() {
        if (isSpeechAvailable) {
            try {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(object : RecognitionListener {
                        override fun onReadyForSpeech(params: Bundle?) {
                            _isListening.value = true
                            _errorMessage.value = null
                        }

                        override fun onBeginningOfSpeech() {}

                        override fun onRmsChanged(rmsdB: Float) {}

                        override fun onBufferReceived(buffer: ByteArray?) {}

                        override fun onEndOfSpeech() {
                            _isListening.value = false
                        }

                        override fun onError(error: Int) {
                            _isListening.value = false
                            val errorDesc = getErrorText(error)
                            // In offline conditions, inform user gracefully
                            if (error == SpeechRecognizer.ERROR_NETWORK || error == SpeechRecognizer.ERROR_NETWORK_TIMEOUT) {
                                _errorMessage.value = "Offline speech service unavailable. You can type or use the Demo button."
                            } else if (error != SpeechRecognizer.ERROR_NO_MATCH) {
                                _errorMessage.value = errorDesc
                            }
                        }

                        override fun onResults(results: Bundle?) {
                            _isListening.value = false
                            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            if (!matches.isNullOrEmpty()) {
                                _transcribedText.value = matches[0]
                            }
                        }

                        override fun onPartialResults(partialResults: Bundle?) {
                            val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            if (!matches.isNullOrEmpty()) {
                                _transcribedText.value = matches[0]
                            }
                        }

                        override fun onEvent(eventType: Int, params: Bundle?) {}
                    })
                }
            } catch (e: Exception) {
                speechRecognizer = null
            }
        }
    }

    override fun startListening(languageCode: String) {
        _errorMessage.value = null
        if (!isSpeechAvailable || speechRecognizer == null) {
            // Emulators or devices without Speech Recognizer fall back to instant demo simulation
            simulateArtisanVoiceInput()
            return
        }

        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languageCode)
                putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, languageCode)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            }
            speechRecognizer?.startListening(intent)
            _isListening.value = true
        } catch (e: Exception) {
            _isListening.value = false
            simulateArtisanVoiceInput()
        }
    }

    override fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (_: Exception) {}
        _isListening.value = false
    }

    override fun setManualText(text: String) {
        _transcribedText.value = text
    }

    override fun simulateArtisanVoiceInput(presetIndex: Int) {
        val index = if (presetIndex in demoSpeechPresets.indices) presetIndex else 0
        _transcribedText.value = demoSpeechPresets[index]
        _errorMessage.value = null
    }

    override fun destroy() {
        try {
            speechRecognizer?.destroy()
        } catch (_: Exception) {}
        speechRecognizer = null
    }

    private fun getErrorText(errorCode: Int): String {
        return when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission required"
            SpeechRecognizer.ERROR_NETWORK -> "Network required for cloud voice (use Demo mode offline)"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout (use Demo mode offline)"
            SpeechRecognizer.ERROR_NO_MATCH -> "No speech match recognized"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
            SpeechRecognizer.ERROR_SERVER -> "Server error"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input detected"
            else -> "Speech recognition not available"
        }
    }
}
