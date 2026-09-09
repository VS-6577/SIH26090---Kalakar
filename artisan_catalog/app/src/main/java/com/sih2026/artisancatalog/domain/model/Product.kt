package com.sih2026.artisancatalog.domain.model

data class Product(
    val id: String,
    val title: String,
    val description: String,
    val craftHistory: String,
    val imageUris: List<String>,
    val voiceTranscription: String,
    val materialCost: Double,
    val laborCost: Double,
    val margin: Double,
    val suggestedPrice: Double,
    val category: CraftCategory = CraftCategory.OTHER,
    val language: String = "en",
    val createdAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "LOCAL_ONLY"
)
