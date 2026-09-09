package com.sih2026.artisancatalog.ui.navigation

sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object Onboarding : Screen("onboarding")
    data object LanguageSelection : Screen("language_selection")
    data object Auth : Screen("auth")
    data object Dashboard : Screen("dashboard")
    data object AddProduct : Screen("add_product")
    data object GuidedCamera : Screen("guided_camera")
    data object EstimatedPricing : Screen("estimated_pricing")
    data object Review : Screen("review")
    data object PricingSummary : Screen("pricing_summary")
    data object Settings : Screen("settings")
    data object Market : Screen("market")
}
