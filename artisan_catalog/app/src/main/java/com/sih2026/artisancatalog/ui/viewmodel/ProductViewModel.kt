package com.sih2026.artisancatalog.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sih2026.artisancatalog.ArtisanApplication
import com.sih2026.artisancatalog.data.remote.ApiClient
import com.sih2026.artisancatalog.data.remote.dto.ProductImagePayload
import com.sih2026.artisancatalog.data.remote.dto.VisualProductProfile
import com.sih2026.artisancatalog.domain.image.ImageEnhancementService
import com.sih2026.artisancatalog.domain.model.CraftCategory
import com.sih2026.artisancatalog.domain.model.PricingBreakdown
import com.sih2026.artisancatalog.domain.model.Product
import com.sih2026.artisancatalog.util.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

enum class ProductOrientation(val displayName: String, val instruction: String) {
    FRONT("Front View", "Capture the front of your product"),
    RIGHT("Right View", "Rotate your product to show the right side"),
    BACK("Back View", "Rotate to show the back of your product"),
    LEFT("Left View", "Rotate your product to show the left side"),
    TOP("Top View", "Capture the top view of your product"),
    BOTTOM("Bottom View", "Capture the bottom view of your product")
}

data class ProductDraftState(
    val id: String = UUID.randomUUID().toString(),
    
    // Six-View Product Photography
    val frontPhoto: String? = null,
    val rightPhoto: String? = null,
    val backPhoto: String? = null,
    val leftPhoto: String? = null,
    val topPhoto: String? = null,
    val bottomPhoto: String? = null,
    val currentOrientation: ProductOrientation = ProductOrientation.FRONT,
    
    // Voice Description & Content
    val voiceTranscription: String = "",
    val productTitle: String = "Handmade Traditional Embroidered Cotton Bag",
    val productDescription: String = "A handcrafted cotton bag featuring intricate traditional embroidery. Lightweight, durable, reusable and suitable for everyday use.",
    val craftHistory: String = "Created using centuries-old needlework techniques passed down through generations of rural artisan families.",
    val englishTitle: String = "Handmade Traditional Embroidered Cotton Bag",
    val hindiTitle: String = "हस्तनिर्मित पारंपरिक कढ़ाई वाला सूती बैग",
    val englishDescription: String = "A handcrafted cotton bag featuring intricate traditional embroidery. Lightweight, durable, reusable and suitable for everyday use.",
    val hindiDescription: String = "पारंपरिक हस्त-कढ़ाई से सुसज्जित शुद्ध सूती थैला। यह मजबूत, पर्यावरण-अनुकूल और रोजमर्रा के उपयोग के लिए उपयुक्त है।",
    val englishCraftHistory: String = "Created using centuries-old needlework techniques passed down through generations of rural artisan families.",
    val hindiCraftHistory: String = "यह उत्पाद ग्रामीण महिला बुनकरों द्वारा पीढ़ियों पुरानी पारंपरिक सुई-धागे की कला से तैयार किया गया है।",
    val tags: List<String> = listOf("Handmade", "GI Tagged", "Eco-Friendly"),
    val category: CraftCategory = CraftCategory.EMBROIDERY,

    // Backend AI & Network State
    val serverUrl: String = ApiClient.DEFAULT_BASE_URL,
    val isBackendOnline: Boolean = false,
    val isAnalyzingWithAi: Boolean = false,
    val isGeneratingContent: Boolean = false,
    val isGeneratingMockups: Boolean = false,
    val aiAnalysisStatus: String? = null,
    val visualProfile: VisualProductProfile? = null,
    val generatedMockupImages: Map<String, String> = emptyMap(),
    
    // Q1: Material
    val materialCost: Double = 250.0,
    val materialCategory: String = "Textile",
    
    // Q2: Labor
    val laborMode: String = "Days", // "Days" or "Hours"
    val timeSpent: Double = 2.0,
    val dailyWage: Double = 350.0,
    
    // Q3: Logistics
    val packagingCost: Double = 30.0,
    val transportCost: Double = 20.0,
    val packagingType: String = "Eco Jute Box",
    
    // Q4: Margin
    val marginPercent: Double = 35.0,
    val isCustomMargin: Boolean = false,
    val customMarginInput: String = "35",
    
    // Dynamic Pricing Calculations
    val calculatedLaborCost: Double = 700.0,
    val calculatedLogisticsCost: Double = 50.0,
    val calculatedTrueCost: Double = 1420.0,
    val calculatedArtisanProfit: Double = 350.0,
    val calculatedFinalPrice: Double = 1350.0,
    val aiMarketEstimate: Double = 1350.0,
    val sweetSpotPrice: Double = 1450.0,
    val minSellingPrice: Double = 1150.0,
    val maxSellingPrice: Double = 1750.0,
    
    // Application & Auth State
    val selectedLanguage: String = "hi", // Hindi is default on first opening
    val userMobileNumber: String = "98765 43210",
    val otpInput: String = "",
    val isLoggedIn: Boolean = true,
    val artisanName: String = "Ram Kumar",
    val artisanCluster: String = "Gorakhpur Cluster",
    val artisanId: String = "ART-8831",
    val isVerified: Boolean = true,
    
    // UI Flow Flags
    val reviewLanguageTab: String = "EN", // "EN" or "HI"
    val isSaving: Boolean = false,
    val isSavedSuccess: Boolean = false
) {
    val allPhotosList: List<String>
        get() = listOfNotNull(frontPhoto, rightPhoto, backPhoto, leftPhoto, topPhoto, bottomPhoto)

    val studioMockupList: List<String>
        get() {
            val mockups = listOfNotNull(
                generatedMockupImages["hero"],
                generatedMockupImages["lifestyle"],
                generatedMockupImages["detail"],
                generatedMockupImages["context"]
            )
            return mockups.ifEmpty { allPhotosList }
        }
}

class ProductViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val app = application as ArtisanApplication
    private val repository = app.productRepository
    private val aiService = app.aiService
    private val remoteAIService = app.remoteAIService
    private val photoProcessor = app.photoProcessor
    private val voiceTranscriber = app.voiceTranscriber
    private val ttsHelper = app.ttsHelper
    private val preferenceManager = app.preferenceManager

    private val _draftState = MutableStateFlow(ProductDraftState())
    val draftState: StateFlow<ProductDraftState> = _draftState.asStateFlow()

    val isListening: StateFlow<Boolean> = voiceTranscriber.isListening
    val isSpeaking: StateFlow<Boolean> = ttsHelper.isSpeaking

    val allProducts: StateFlow<List<Product>> = repository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Recalculate default pricing based on initial state
        recalculatePricing()

        // Observe Server URL preference
        viewModelScope.launch {
            preferenceManager.serverUrl.collect { url ->
                ApiClient.currentBaseUrl = url
                _draftState.value = _draftState.value.copy(serverUrl = url)
                checkBackendHealth()
            }
        }

        viewModelScope.launch {
            voiceTranscriber.transcribedText.collect { text ->
                if (text.isNotBlank()) {
                    _draftState.value = _draftState.value.copy(voiceTranscription = text)
                }
            }
        }
    }

    // -------------------------------------------------------------
    // BACKEND INTEGRATION & HEALTH
    // -------------------------------------------------------------
    fun checkBackendHealth() {
        viewModelScope.launch {
            val isOnline = remoteAIService.checkHealth()
            _draftState.value = _draftState.value.copy(isBackendOnline = isOnline)
        }
    }

    fun updateServerUrl(url: String) {
        viewModelScope.launch {
            val sanitized = if (url.endsWith("/")) url else "$url/"
            preferenceManager.setServerUrl(sanitized)
            ApiClient.currentBaseUrl = sanitized
            _draftState.value = _draftState.value.copy(serverUrl = sanitized)
            checkBackendHealth()
        }
    }

    fun analyzeCapturedProduct() {
        viewModelScope.launch {
            val state = _draftState.value
            val photos = state.allPhotosList
            if (photos.isEmpty()) return@launch

            _draftState.value = _draftState.value.copy(
                isAnalyzingWithAi = true,
                aiAnalysisStatus = "Analyzing product photos with Gemini Vision..."
            )

            val orientationList = listOf(
                Pair(ProductOrientation.FRONT, state.frontPhoto),
                Pair(ProductOrientation.RIGHT, state.rightPhoto),
                Pair(ProductOrientation.BACK, state.backPhoto),
                Pair(ProductOrientation.LEFT, state.leftPhoto),
                Pair(ProductOrientation.TOP, state.topPhoto),
                Pair(ProductOrientation.BOTTOM, state.bottomPhoto)
            )

            val payloads = mutableListOf<ProductImagePayload>()
            withContext(Dispatchers.IO) {
                orientationList.forEach { (orientation, path) ->
                    if (path != null) {
                        val base64Url = ImageUtils.pathToBase64DataUrl(path, maxDimension = 1024)
                        if (base64Url != null) {
                            payloads.add(
                                ProductImagePayload(
                                    id = orientation.name.lowercase(),
                                    type = orientation.name.lowercase(),
                                    label = orientation.displayName,
                                    dataUrl = base64Url
                                )
                            )
                        }
                    }
                }
            }

            if (payloads.isNotEmpty()) {
                val profile = remoteAIService.analyzeProductImages(state.id, payloads)
                if (profile != null) {
                    val detectedCategory = when {
                        profile.category?.contains("pottery", ignoreCase = true) == true ||
                        profile.productType?.contains("pot", ignoreCase = true) == true -> CraftCategory.POTTERY
                        profile.category?.contains("wood", ignoreCase = true) == true -> CraftCategory.WOODWORK
                        profile.category?.contains("metal", ignoreCase = true) == true ||
                        profile.category?.contains("brass", ignoreCase = true) == true -> CraftCategory.METALWORK
                        profile.category?.contains("jewelry", ignoreCase = true) == true -> CraftCategory.JEWELRY
                        profile.category?.contains("leather", ignoreCase = true) == true -> CraftCategory.LEATHER
                        else -> CraftCategory.EMBROIDERY
                    }

                    val materialName = profile.materialsObserved?.firstOrNull()?.value ?: "Textile"

                    _draftState.value = _draftState.value.copy(
                        visualProfile = profile,
                        category = detectedCategory,
                        materialCategory = materialName,
                        isAnalyzingWithAi = false,
                        aiAnalysisStatus = "Product analyzed: ${profile.productType ?: profile.category}"
                    )
                } else {
                    _draftState.value = _draftState.value.copy(
                        isAnalyzingWithAi = false,
                        aiAnalysisStatus = "Offline: on-device heuristics active"
                    )
                }
            } else {
                _draftState.value = _draftState.value.copy(
                    isAnalyzingWithAi = false,
                    aiAnalysisStatus = null
                )
            }
        }
    }

    fun generateCatalogContent(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            val state = _draftState.value
            _draftState.value = _draftState.value.copy(isGeneratingContent = true)

            val content = remoteAIService.generateListingContent(
                sessionId = state.id,
                profile = state.visualProfile,
                artisanInput = state.voiceTranscription.ifBlank { "Handmade authentic craft by rural artisan." }
            )

            if (content != null) {
                val enTitle = content.title ?: state.productTitle
                val hiTitle = content.title ?: state.hindiTitle
                val enDesc = content.descriptionEn ?: content.shortDescription ?: state.productDescription
                val hiDesc = content.descriptionHi ?: state.hindiDescription
                val enHistory = content.extractedFacts?.craftTechnique ?: state.craftHistory
                val hiHistory = content.extractedFacts?.craftTechnique ?: state.hindiCraftHistory
                val tags = content.tags ?: state.tags

                val isHindi = state.reviewLanguageTab == "HI"

                _draftState.value = _draftState.value.copy(
                    isGeneratingContent = false,
                    englishTitle = enTitle,
                    hindiTitle = hiTitle,
                    englishDescription = enDesc,
                    hindiDescription = hiDesc,
                    englishCraftHistory = enHistory,
                    hindiCraftHistory = hiHistory,
                    productTitle = if (isHindi) hiTitle else enTitle,
                    productDescription = if (isHindi) hiDesc else enDesc,
                    craftHistory = if (isHindi) hiHistory else enHistory,
                    tags = tags
                )
            } else {
                // Heuristic offline fallback
                val offlineResult = aiService.generateProductDetails(
                    state.voiceTranscription,
                    if (state.reviewLanguageTab == "HI") "hi" else "en"
                )
                _draftState.value = _draftState.value.copy(
                    isGeneratingContent = false,
                    productTitle = offlineResult.title,
                    productDescription = offlineResult.description,
                    craftHistory = offlineResult.craftHistory,
                    tags = offlineResult.detectedKeywords.ifEmpty { state.tags }
                )
            }
            onComplete?.invoke()
        }
    }

    fun generateStudioMockup(imageType: String = "hero") {
        viewModelScope.launch {
            val state = _draftState.value
            val photos = state.allPhotosList
            if (photos.isEmpty()) return@launch

            _draftState.value = _draftState.value.copy(isGeneratingMockups = true)

            val base64Urls = withContext(Dispatchers.IO) {
                photos.mapNotNull { ImageUtils.pathToBase64DataUrl(it, maxDimension = 1024) }
            }

            if (base64Urls.isNotEmpty()) {
                val result = remoteAIService.generateStudioMockup(state.id, imageType, base64Urls)
                if (result?.dataUrl != null) {
                    val updatedMockups = state.generatedMockupImages.toMutableMap().apply {
                        put(imageType, result.dataUrl)
                    }
                    _draftState.value = _draftState.value.copy(
                        isGeneratingMockups = false,
                        generatedMockupImages = updatedMockups
                    )
                } else {
                    _draftState.value = _draftState.value.copy(isGeneratingMockups = false)
                }
            } else {
                _draftState.value = _draftState.value.copy(isGeneratingMockups = false)
            }
        }
    }

    // -------------------------------------------------------------
    // LANGUAGE STATE
    // -------------------------------------------------------------
    fun selectLanguage(langCode: String) {
        viewModelScope.launch {
            preferenceManager.setLanguage(langCode)
            _draftState.value = _draftState.value.copy(selectedLanguage = langCode)
        }
    }

    fun setReviewLanguageTab(tab: String) {
        val s = _draftState.value
        _draftState.value = s.copy(
            reviewLanguageTab = tab,
            productTitle = if (tab == "HI") s.hindiTitle else s.englishTitle,
            productDescription = if (tab == "HI") s.hindiDescription else s.englishDescription,
            craftHistory = if (tab == "HI") s.hindiCraftHistory else s.englishCraftHistory
        )
    }

    // -------------------------------------------------------------
    // AUTHENTICATION
    // -------------------------------------------------------------
    fun setMobileNumber(phone: String) {
        _draftState.value = _draftState.value.copy(userMobileNumber = phone)
    }

    fun setOtpInput(otp: String) {
        _draftState.value = _draftState.value.copy(otpInput = otp)
    }

    fun loginSuccess() {
        _draftState.value = _draftState.value.copy(isLoggedIn = true)
    }

    // -------------------------------------------------------------
    // GUIDED SIX-VIEW PRODUCT PHOTOGRAPHY
    // -------------------------------------------------------------
    fun saveOrientationPhoto(orientation: ProductOrientation, photoPath: String) {
        val current = _draftState.value
        val updated = when (orientation) {
            ProductOrientation.FRONT -> current.copy(frontPhoto = photoPath)
            ProductOrientation.RIGHT -> current.copy(rightPhoto = photoPath)
            ProductOrientation.BACK -> current.copy(backPhoto = photoPath)
            ProductOrientation.LEFT -> current.copy(leftPhoto = photoPath)
            ProductOrientation.TOP -> current.copy(topPhoto = photoPath)
            ProductOrientation.BOTTOM -> current.copy(bottomPhoto = photoPath)
        }
        _draftState.value = updated
    }

    fun nextOrientation(): ProductOrientation? {
        val values = ProductOrientation.entries.toTypedArray()
        val currentIndex = values.indexOf(_draftState.value.currentOrientation)
        return if (currentIndex < values.size - 1) {
            val next = values[currentIndex + 1]
            _draftState.value = _draftState.value.copy(currentOrientation = next)
            next
        } else {
            null
        }
    }

    fun setOrientation(orientation: ProductOrientation) {
        _draftState.value = _draftState.value.copy(currentOrientation = orientation)
    }

    fun useSampleCraftPhotos() {
        viewModelScope.launch {
            val samplesDir = File(app.filesDir, "sample_photos").apply { if (!exists()) mkdirs() }
            val frontFile = File(samplesDir, "sample_front.jpg")
            val rightFile = File(samplesDir, "sample_right.jpg")
            val backFile = File(samplesDir, "sample_back.jpg")
            val leftFile = File(samplesDir, "sample_left.jpg")
            val topFile = File(samplesDir, "sample_top.jpg")
            val bottomFile = File(samplesDir, "sample_bottom.jpg")

            if (photoProcessor is ImageEnhancementService) {
                photoProcessor.createSampleCraftPhoto("Handmade Bag (Front)", frontFile)
                photoProcessor.createSampleCraftPhoto("Handmade Bag (Right)", rightFile)
                photoProcessor.createSampleCraftPhoto("Handmade Bag (Back)", backFile)
                photoProcessor.createSampleCraftPhoto("Handmade Bag (Left)", leftFile)
                photoProcessor.createSampleCraftPhoto("Handmade Bag (Top)", topFile)
                photoProcessor.createSampleCraftPhoto("Handmade Bag (Bottom)", bottomFile)
            }

            _draftState.value = _draftState.value.copy(
                frontPhoto = frontFile.absolutePath,
                rightPhoto = rightFile.absolutePath,
                backPhoto = backFile.absolutePath,
                leftPhoto = leftFile.absolutePath,
                topPhoto = topFile.absolutePath,
                bottomPhoto = bottomFile.absolutePath
            )

            // Trigger AI analysis with sample photos
            analyzeCapturedProduct()
        }
    }

    // -------------------------------------------------------------
    // VOICE INPUT
    // -------------------------------------------------------------
    fun startVoiceRecognition() {
        val langCode = if (_draftState.value.selectedLanguage == "hi") "hi-IN" else "en-IN"
        voiceTranscriber.startListening(langCode)
    }

    fun stopVoiceRecognition() {
        voiceTranscriber.stopListening()
    }

    fun updateVoiceTranscription(text: String) {
        _draftState.value = _draftState.value.copy(voiceTranscription = text)
        voiceTranscriber.setManualText(text)
    }

    fun simulateArtisanVoice() {
        if (_draftState.value.selectedLanguage == "hi") {
            _draftState.value = _draftState.value.copy(
                voiceTranscription = "यह एक हस्तनिर्मित पारंपरिक कढ़ाई वाला सूती थैला है। इसे बनाने में दो दिन लगे।"
            )
        } else {
            _draftState.value = _draftState.value.copy(
                voiceTranscription = "This is a handmade cotton bag with traditional embroidery. Took two days to craft."
            )
        }
    }

    // -------------------------------------------------------------
    // ESTIMATED PRICING STATE & CALCULATIONS
    // -------------------------------------------------------------
    fun updateMaterialCost(cost: Double) {
        _draftState.value = _draftState.value.copy(materialCost = cost)
        recalculatePricing()
    }

    fun updateMaterialCategory(category: String) {
        _draftState.value = _draftState.value.copy(materialCategory = category)
    }

    fun updateLaborMode(mode: String) {
        _draftState.value = _draftState.value.copy(laborMode = mode)
        recalculatePricing()
    }

    fun updateTimeSpent(time: Double) {
        _draftState.value = _draftState.value.copy(timeSpent = time)
        recalculatePricing()
    }

    fun updateDailyWage(wage: Double) {
        _draftState.value = _draftState.value.copy(dailyWage = wage)
        recalculatePricing()
    }

    fun updatePackagingCost(cost: Double) {
        _draftState.value = _draftState.value.copy(packagingCost = cost)
        recalculatePricing()
    }

    fun updateTransportCost(cost: Double) {
        _draftState.value = _draftState.value.copy(transportCost = cost)
        recalculatePricing()
    }

    fun updatePackagingType(type: String) {
        _draftState.value = _draftState.value.copy(packagingType = type)
    }

    fun updateMarginPercent(margin: Double, isCustom: Boolean = false, customText: String = "35") {
        _draftState.value = _draftState.value.copy(
            marginPercent = margin,
            isCustomMargin = isCustom,
            customMarginInput = customText
        )
        recalculatePricing()
    }

    private fun recalculatePricing() {
        val s = _draftState.value
        val labor = if (s.laborMode == "Days") s.timeSpent * s.dailyWage else s.timeSpent * (s.dailyWage / 8.0)
        val logistics = s.packagingCost + s.transportCost
        val baseCost = s.materialCost + labor + logistics
        
        // Margin calculation
        val profit = baseCost * (s.marginPercent / 100.0)
        val finalPrice = baseCost + profit

        // Benchmarks & Sweet Spot
        val trueCost = baseCost + 420.0 // True cost including artisan living overhead
        val sweetSpot = finalPrice + 100.0
        val minPrice = baseCost * 1.15
        val maxPrice = baseCost * 1.75

        _draftState.value = s.copy(
            calculatedLaborCost = labor,
            calculatedLogisticsCost = logistics,
            calculatedTrueCost = trueCost,
            calculatedArtisanProfit = profit,
            calculatedFinalPrice = finalPrice,
            sweetSpotPrice = sweetSpot,
            minSellingPrice = minPrice,
            maxSellingPrice = maxPrice
        )
    }

    // -------------------------------------------------------------
    // REVIEW & TTS
    // -------------------------------------------------------------
    fun listenToDetails() {
        val s = _draftState.value
        val textToSpeak = buildString {
            append("${s.productTitle}. ")
            append("${s.productDescription}. ")
            append("Suggested selling price is rupees ${s.calculatedFinalPrice.toInt()}. ")
            append("Artisan profit is rupees ${s.calculatedArtisanProfit.toInt()}.")
        }
        ttsHelper.speak(textToSpeak, if (s.reviewLanguageTab == "HI") "hi" else "en")
    }

    fun stopListeningTts() {
        ttsHelper.stop()
    }

    fun saveProduct(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _draftState.value = _draftState.value.copy(isSaving = true)
            val s = _draftState.value

            val product = Product(
                id = s.id,
                title = s.productTitle,
                description = s.productDescription,
                craftHistory = s.craftHistory,
                imageUris = s.allPhotosList.ifEmpty { listOf("sample_pottery_1") },
                voiceTranscription = s.voiceTranscription,
                materialCost = s.materialCost,
                laborCost = s.calculatedLaborCost,
                margin = s.calculatedArtisanProfit,
                suggestedPrice = s.calculatedFinalPrice,
                category = s.category,
                language = s.selectedLanguage,
                createdAt = System.currentTimeMillis(),
                syncStatus = if (s.isBackendOnline) "SYNCED" else "LOCAL_ONLY"
            )

            repository.saveProduct(product)
            _draftState.value = _draftState.value.copy(
                isSaving = false,
                isSavedSuccess = true
            )
            onSuccess()
        }
    }

    fun resetProductDraft() {
        _draftState.value = ProductDraftState(
            id = UUID.randomUUID().toString(),
            selectedLanguage = _draftState.value.selectedLanguage,
            serverUrl = _draftState.value.serverUrl,
            isBackendOnline = _draftState.value.isBackendOnline
        )
    }

    fun startNewProduct() = resetProductDraft()

    override fun onCleared() {
        super.onCleared()
        ttsHelper.stop()
    }
}
