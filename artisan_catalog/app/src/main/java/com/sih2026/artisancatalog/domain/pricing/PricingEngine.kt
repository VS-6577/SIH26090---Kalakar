package com.sih2026.artisancatalog.domain.pricing

import com.sih2026.artisancatalog.domain.model.CraftCategory
import com.sih2026.artisancatalog.domain.model.PricingBreakdown

interface PricingEngine {
    fun calculateSuggestedPrice(
        materialCost: Double? = null,
        laborHours: Double? = null,
        laborHourlyRate: Double? = null,
        targetMarginPercent: Double? = null,
        category: CraftCategory = CraftCategory.OTHER
    ): PricingBreakdown
}
