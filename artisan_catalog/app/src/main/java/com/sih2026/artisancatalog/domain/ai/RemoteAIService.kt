package com.sih2026.artisancatalog.domain.ai

import com.sih2026.artisancatalog.data.remote.api.KalakritiApiService
import com.sih2026.artisancatalog.data.remote.dto.AnalyzeRequest
import com.sih2026.artisancatalog.data.remote.dto.ContentRequest
import com.sih2026.artisancatalog.data.remote.dto.GeneratedContent
import com.sih2026.artisancatalog.data.remote.dto.GeneratedImageData
import com.sih2026.artisancatalog.data.remote.dto.ImageGenRequest
import com.sih2026.artisancatalog.data.remote.dto.ProductImagePayload
import com.sih2026.artisancatalog.data.remote.dto.VisualProductProfile
import com.sih2026.artisancatalog.domain.model.CraftCategory
import com.sih2026.artisancatalog.domain.model.ProductAiResult

/**
 * Production Remote AI service connecting to the Node/Express backend REST API.
 * Gracefully falls back to OfflineAIService whenever network is unavailable or server is unreachable.
 */
class RemoteAIService(
    private val apiService: KalakritiApiService,
    private val fallbackOfflineService: OfflineAIService = OfflineAIService()
) : AIService {

    override val isOfflineOnly: Boolean = false

    suspend fun checkHealth(): Boolean {
        return try {
            val response = apiService.checkHealth()
            response.isSuccessful && response.body()?.status == "ok"
        } catch (_: Exception) {
            false
        }
    }

    suspend fun analyzeProductImages(
        sessionId: String,
        images: List<ProductImagePayload>
    ): VisualProductProfile? {
        return try {
            val response = apiService.analyzeProduct(sessionId, AnalyzeRequest(images))
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.profile
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun generateListingContent(
        sessionId: String,
        profile: VisualProductProfile?,
        artisanInput: String
    ): GeneratedContent? {
        return try {
            val response = apiService.generateContent(
                sessionId,
                ContentRequest(profile = profile, artisanInput = artisanInput)
            )
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.content
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun generateStudioMockup(
        sessionId: String,
        imageType: String,
        originalImages: List<String>
    ): GeneratedImageData? {
        return try {
            val response = apiService.generateSingleImage(
                sessionId,
                ImageGenRequest(imageType = imageType, originalImages = originalImages)
            )
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.image
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun generateProductDetails(
        voiceTranscription: String,
        targetLanguage: String
    ): ProductAiResult {
        return try {
            val response = apiService.generateContent(
                sessionId = "session_${System.currentTimeMillis()}",
                request = ContentRequest(profile = null, artisanInput = voiceTranscription)
            )
            val content = response.body()?.content
            if (response.isSuccessful && content != null) {
                val isHindi = targetLanguage.startsWith("hi", ignoreCase = true)
                ProductAiResult(
                    title = content.title ?: "Handcrafted Artisan Product",
                    description = if (isHindi) (content.descriptionHi ?: content.shortDescription ?: "") else (content.descriptionEn ?: content.shortDescription ?: ""),
                    craftHistory = content.extractedFacts?.craftTechnique ?: "Handcrafted by local artisans using traditional Indian craft techniques.",
                    suggestedCategory = CraftCategory.OTHER,
                    detectedKeywords = content.tags ?: listOf("Handmade"),
                    language = targetLanguage
                )
            } else {
                fallbackOfflineService.generateProductDetails(voiceTranscription, targetLanguage)
            }
        } catch (_: Exception) {
            fallbackOfflineService.generateProductDetails(voiceTranscription, targetLanguage)
        }
    }

    override suspend fun translateText(text: String, targetLanguage: String): String {
        return fallbackOfflineService.translateText(text, targetLanguage)
    }
}
