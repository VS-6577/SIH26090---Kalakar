package com.sih2026.artisancatalog.data.remote.dto

import com.google.gson.annotations.SerializedName

// --- HEALTH CHECK ---
data class HealthResponse(
    @SerializedName("status") val status: String,
    @SerializedName("service") val service: String?,
    @SerializedName("geminiKeyConfigured") val geminiKeyConfigured: Boolean?
)

// --- STAGE 1: PRODUCT UNDERSTANDING (/analyze) ---
data class ProductImagePayload(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("label") val label: String,
    @SerializedName("dataUrl") val dataUrl: String
)

data class AnalyzeRequest(
    @SerializedName("images") val images: List<ProductImagePayload>
)

data class AnalyzeResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("profile") val profile: VisualProductProfile?,
    @SerializedName("error") val error: String?,
    @SerializedName("detail") val detail: String?
)

data class VisualProductProfile(
    @SerializedName("product_type") val productType: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("materials_observed") val materialsObserved: List<MaterialObserved>? = null,
    @SerializedName("colors_observed") val colorsObserved: List<String>? = null,
    @SerializedName("shape") val shape: String? = null,
    @SerializedName("construction") val construction: String? = null,
    @SerializedName("craft_style") val craftStyle: String? = null,
    @SerializedName("visible_features") val visibleFeatures: List<String>? = null,
    @SerializedName("texture") val texture: String? = null,
    @SerializedName("patterns") val patterns: List<String>? = null,
    @SerializedName("distinctive_features") val distinctiveFeatures: List<String>? = null,
    @SerializedName("visible_imperfections") val visibleImperfections: List<String>? = null,
    @SerializedName("visual_summary") val visualSummary: String? = null,
    @SerializedName("uncertainties") val uncertainties: List<String>? = null
)

data class MaterialObserved(
    @SerializedName("value") val value: String? = null,
    @SerializedName("confidence") val confidence: String? = null
)

// --- STAGE 2: PRODUCT CONTENT GENERATION (/generate-content) ---
data class ContentRequest(
    @SerializedName("profile") val profile: VisualProductProfile?,
    @SerializedName("artisanInput") val artisanInput: String
)

data class ContentResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("content") val content: GeneratedContent?,
    @SerializedName("error") val error: String?,
    @SerializedName("detail") val detail: String?
)

data class GeneratedContent(
    @SerializedName("extracted_facts") val extractedFacts: ExtractedFacts? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("short_description") val shortDescription: String? = null,
    @SerializedName("description_en") val descriptionEn: String? = null,
    @SerializedName("description_hi") val descriptionHi: String? = null,
    @SerializedName("seo_description") val seoDescription: String? = null,
    @SerializedName("tags") val tags: List<String>? = null
)

data class ExtractedFacts(
    @SerializedName("materials") val materials: List<String>? = null,
    @SerializedName("craft_technique") val craftTechnique: String? = null,
    @SerializedName("production_time") val productionTime: String? = null,
    @SerializedName("dimensions") val dimensions: String? = null,
    @SerializedName("weight") val weight: String? = null,
    @SerializedName("artisan_highlights") val artisanHighlights: List<String>? = null
)

// --- STAGE 5: PHOTOSHOOT PROMPT SPECIFICATION (/photoshoot) ---
data class PhotoshootRequest(
    @SerializedName("profile") val profile: VisualProductProfile?,
    @SerializedName("artisanInput") val artisanInput: String,
    @SerializedName("content") val content: GeneratedContent?
)

data class PhotoshootResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("photoshoot") val photoshoot: Map<String, ShotPromptSpec>?,
    @SerializedName("error") val error: String?
)

data class ShotPromptSpec(
    @SerializedName("prompt") val prompt: String?,
    @SerializedName("purpose") val purpose: String? = null
)

// --- STAGE 6: QWEN IMAGE EDIT MOCKUP (/generate-single-image) ---
data class ImageGenRequest(
    @SerializedName("imageType") val imageType: String,
    @SerializedName("originalImages") val originalImages: List<String>? = null,
    @SerializedName("originalImage") val originalImage: String? = null
)

data class ImageGenResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("image") val image: GeneratedImageData?,
    @SerializedName("error") val error: String?,
    @SerializedName("detail") val detail: String?
)

data class GeneratedImageData(
    @SerializedName("id") val id: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("dataUrl") val dataUrl: String? = null,
    @SerializedName("promptVersion") val promptVersion: Int? = null,
    @SerializedName("generatedAt") val generatedAt: String? = null,
    @SerializedName("source") val source: String? = null
)

// --- VOICE AUDIO TRANSCRIPTION (/transcribe-voice) ---
data class TranscribeRequest(
    @SerializedName("audioData") val audioData: String,
    @SerializedName("mimeType") val mimeType: String = "audio/webm",
    @SerializedName("language") val language: String = "auto"
)

data class TranscribeResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("transcript") val transcript: String?,
    @SerializedName("error") val error: String?
)
