package com.sih2026.artisancatalog.domain.model

data class ProductAiResult(
    val title: String,
    val description: String,
    val craftHistory: String,
    val suggestedCategory: CraftCategory,
    val detectedKeywords: List<String>,
    val language: String = "en"
)
