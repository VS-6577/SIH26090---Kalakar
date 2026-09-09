package com.sih2026.artisancatalog.domain.ai

import com.sih2026.artisancatalog.domain.model.CraftCategory
import com.sih2026.artisancatalog.domain.model.ProductAiResult

interface DescriptionGenerator {
    suspend fun generateProductDetails(
        voiceTranscription: String,
        targetLanguage: String = "en"
    ): ProductAiResult
}

interface AIService : DescriptionGenerator {
    val isOfflineOnly: Boolean
    suspend fun translateText(text: String, targetLanguage: String): String
}
