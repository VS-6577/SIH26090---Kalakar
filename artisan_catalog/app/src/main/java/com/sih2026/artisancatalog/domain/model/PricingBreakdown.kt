package com.sih2026.artisancatalog.domain.model

data class PricingBreakdown(
    val materialCost: Double,
    val laborCost: Double,
    val margin: Double,
    val suggestedPrice: Double,
    val laborHours: Double = 2.5,
    val hourlyLivingRate: Double = 68.0,
    val notes: String = "Calculated using Fair Living Wage standards"
)
