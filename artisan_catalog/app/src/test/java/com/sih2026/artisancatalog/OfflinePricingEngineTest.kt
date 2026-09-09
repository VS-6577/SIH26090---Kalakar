package com.sih2026.artisancatalog

import com.sih2026.artisancatalog.domain.model.CraftCategory
import com.sih2026.artisancatalog.domain.pricing.OfflinePricingEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OfflinePricingEngineTest {

    private lateinit var pricingEngine: OfflinePricingEngine

    @Before
    fun setup() {
        pricingEngine = OfflinePricingEngine()
    }

    @Test
    fun calculateSuggestedPrice_withBenchmarkValues_returnsExactExpectedPrice() {
        // Benchmark from SIH26090 specification:
        // materialCost = 180, laborCost = 170 (2.5 hrs * 68/hr), margin = 100 => 450
        val result = pricingEngine.calculateSuggestedPrice(
            materialCost = 180.0,
            laborHours = 2.5,
            laborHourlyRate = 68.0,
            targetMarginPercent = null,
            category = CraftCategory.EMBROIDERY
        )

        assertEquals(180.0, result.materialCost, 0.01)
        assertEquals(170.0, result.laborCost, 0.01)
        assertEquals(100.0, result.margin, 0.01)
        assertEquals(450.0, result.suggestedPrice, 0.01)
    }

    @Test
    fun calculateSuggestedPrice_withCustomHours_recalculatesAccurately() {
        val result = pricingEngine.calculateSuggestedPrice(
            materialCost = 200.0,
            laborHours = 4.0,
            laborHourlyRate = 70.0,
            targetMarginPercent = null,
            category = CraftCategory.WOODWORK
        )

        // 200 material + (4 * 70 = 280) labor + 100 margin = 580
        assertEquals(200.0, result.materialCost, 0.01)
        assertEquals(280.0, result.laborCost, 0.01)
        assertEquals(100.0, result.margin, 0.01)
        assertEquals(580.0, result.suggestedPrice, 0.01)
    }

    @Test
    fun calculateSuggestedPrice_withCustomMarginPercent_computesPercentage() {
        val result = pricingEngine.calculateSuggestedPrice(
            materialCost = 100.0,
            laborHours = 2.0,
            laborHourlyRate = 50.0,
            targetMarginPercent = 20.0, // 20% of base cost (100 + 100 = 200) -> 40
            category = CraftCategory.OTHER
        )

        assertEquals(100.0, result.materialCost, 0.01)
        assertEquals(100.0, result.laborCost, 0.01)
        assertEquals(40.0, result.margin, 0.01)
        assertEquals(240.0, result.suggestedPrice, 0.01)
    }
}
