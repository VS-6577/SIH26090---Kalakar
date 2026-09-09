package com.sih2026.artisancatalog

import com.sih2026.artisancatalog.domain.ai.OfflineAIService
import com.sih2026.artisancatalog.domain.model.CraftCategory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OfflineAIServiceTest {

    private lateinit var aiService: OfflineAIService

    @Before
    fun setup() {
        aiService = OfflineAIService()
    }

    @Test
    fun generateProductDetails_cottonBagInput_generatesAccurateEmbroideredBagCatalog() = runBlocking {
        val input = "This is a handmade cotton bag with traditional embroidery."
        val result = aiService.generateProductDetails(input, "en")

        assertEquals(CraftCategory.EMBROIDERY, result.suggestedCategory)
        assertTrue(result.title.contains("Embroidered Cotton Bag", ignoreCase = true))
        assertTrue(result.description.contains("traditional embroidery", ignoreCase = true))
        assertTrue(result.craftHistory.isNotBlank())
    }

    @Test
    fun generateProductDetails_potteryInput_detectsPotteryCategory() = runBlocking {
        val input = "I made four terracotta chai cups on the potter wheel with clay."
        val result = aiService.generateProductDetails(input, "en")

        assertEquals(CraftCategory.POTTERY, result.suggestedCategory)
        assertTrue(result.title.contains("Terracotta", ignoreCase = true) || result.title.contains("Clay", ignoreCase = true))
    }

    @Test
    fun generateProductDetails_hindiInput_generatesHindiCatalog() = runBlocking {
        val input = "यह एक हस्तनिर्मित सूती थैला है जिस पर हाथ से कढ़ाई की गई है।"
        val result = aiService.generateProductDetails(input, "hi")

        assertEquals("hi", result.language)
        assertTrue(result.title.contains("थैला") || result.title.contains("बैग"))
        assertTrue(result.description.contains("कढ़ाई"))
    }
}
