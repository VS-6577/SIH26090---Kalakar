package com.sih2026.artisancatalog

import android.app.Application
import com.sih2026.artisancatalog.data.local.AppDatabase
import com.sih2026.artisancatalog.data.local.PreferenceManager
import com.sih2026.artisancatalog.data.remote.ApiClient
import com.sih2026.artisancatalog.data.repository.CatalogRepository
import com.sih2026.artisancatalog.data.repository.ProductRepositoryImpl
import com.sih2026.artisancatalog.domain.ai.AIService
import com.sih2026.artisancatalog.domain.ai.OfflineAIService
import com.sih2026.artisancatalog.domain.ai.RemoteAIService
import com.sih2026.artisancatalog.domain.image.ImageEnhancementService
import com.sih2026.artisancatalog.domain.image.PhotoProcessor
import com.sih2026.artisancatalog.domain.pricing.OfflinePricingEngine
import com.sih2026.artisancatalog.domain.pricing.PricingEngine
import com.sih2026.artisancatalog.domain.tts.TextToSpeechHelper
import com.sih2026.artisancatalog.domain.voice.AndroidVoiceTranscriber
import com.sih2026.artisancatalog.domain.voice.VoiceTranscriber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ArtisanApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val productRepository: CatalogRepository by lazy { ProductRepositoryImpl(database.productDao()) }
    val preferenceManager: PreferenceManager by lazy { PreferenceManager(this) }

    val apiService by lazy { ApiClient.instance }
    val remoteAIService: RemoteAIService by lazy { RemoteAIService(apiService) }
    val aiService: AIService by lazy { remoteAIService }
    val pricingEngine: PricingEngine by lazy { OfflinePricingEngine() }
    val photoProcessor: PhotoProcessor by lazy { ImageEnhancementService(this) }
    val voiceTranscriber: VoiceTranscriber by lazy { AndroidVoiceTranscriber(this) }
    val ttsHelper: TextToSpeechHelper by lazy { TextToSpeechHelper(this) }

    override fun onTerminate() {
        super.onTerminate()
        voiceTranscriber.destroy()
        ttsHelper.shutdown()
    }
}
