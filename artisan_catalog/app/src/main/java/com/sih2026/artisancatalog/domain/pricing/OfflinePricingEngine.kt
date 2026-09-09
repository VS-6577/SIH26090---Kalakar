package com.sih2026.artisancatalog.domain.pricing

import com.sih2026.artisancatalog.domain.model.CraftCategory
import com.sih2026.artisancatalog.domain.model.PricingBreakdown
import kotlin.math.roundToLong

/**
 * Offline fair-wage pricing engine compliant with SIH26090.
 * Computes transparent, living-wage based prices using local input parameters:
 * Material Cost + (Labor Hours * Hourly Living Wage) + Artisan Fair Margin = Suggested Retail Price.
 */
class OfflinePricingEngine : PricingEngine {

    // Default benchmarks based on Indian artisan living wage research
    companion object {
        const val DEFAULT_HOURLY_RATE = 68.0 // INR per hour for skilled handicraft
        const val DEFAULT_LABOR_HOURS = 2.5
        const val DEFAULT_MATERIAL_COST = 180.0
        const val DEFAULT_MARGIN = 100.0
        const val BENCHMARK_SUGGESTED_PRICE = 450.0
    }

    override fun calculateSuggestedPrice(
        materialCost: Double?,
        laborHours: Double?,
        laborHourlyRate: Double?,
        targetMarginPercent: Double?,
        category: CraftCategory
    ): PricingBreakdown {
        val matCost = materialCost ?: categoryDefaultMaterial(category)
        val hours = laborHours ?: categoryDefaultHours(category)
        val rate = laborHourlyRate ?: categoryHourlyRate(category)

        val computedLaborCost = (hours * rate).roundTo2Decimals()
        val baseCost = matCost + computedLaborCost

        val margin = if (targetMarginPercent != null) {
            (baseCost * (targetMarginPercent / 100.0)).roundTo2Decimals()
        } else {
            // Default to ₹100 or ~28% fair artisan profit margin
            DEFAULT_MARGIN
        }

        val totalSuggested = (matCost + computedLaborCost + margin).roundTo2Decimals()

        return PricingBreakdown(
            materialCost = matCost,
            laborCost = computedLaborCost,
            margin = margin,
            suggestedPrice = totalSuggested,
            laborHours = hours,
            hourlyLivingRate = rate,
            notes = "Fair Wage Model: 100% transparent rural artisan compensation"
        )
    }

    private fun categoryDefaultMaterial(category: CraftCategory): Double {
        return when (category) {
            CraftCategory.POTTERY -> 120.0
            CraftCategory.EMBROIDERY -> 180.0
            CraftCategory.WOODWORK -> 220.0
            CraftCategory.METALWORK -> 260.0
            CraftCategory.JEWELRY -> 150.0
            CraftCategory.LEATHER -> 240.0
            CraftCategory.OTHER -> DEFAULT_MATERIAL_COST
        }
    }

    private fun categoryDefaultHours(category: CraftCategory): Double {
        return when (category) {
            CraftCategory.POTTERY -> 2.0
            CraftCategory.EMBROIDERY -> 2.5
            CraftCategory.WOODWORK -> 3.5
            CraftCategory.METALWORK -> 4.0
            CraftCategory.JEWELRY -> 2.0
            CraftCategory.LEATHER -> 3.0
            CraftCategory.OTHER -> DEFAULT_LABOR_HOURS
        }
    }

    private fun categoryHourlyRate(category: CraftCategory): Double {
        return when (category) {
            CraftCategory.WOODWORK, CraftCategory.METALWORK -> 75.0
            CraftCategory.EMBROIDERY -> 68.0
            else -> DEFAULT_HOURLY_RATE
        }
    }

    private fun Double.roundTo2Decimals(): Double {
        return (this * 100.0).roundToLong() / 100.0
    }
}
