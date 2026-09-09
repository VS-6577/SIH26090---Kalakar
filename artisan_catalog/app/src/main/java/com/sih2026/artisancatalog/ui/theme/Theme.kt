package com.sih2026.artisancatalog.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = TerracottaPrimary,
    onPrimary = PureWhite,
    primaryContainer = TerracottaLight,
    onPrimaryContainer = TextDarkBrown,
    secondary = ArtisanGreen,
    onSecondary = PureWhite,
    secondaryContainer = ArtisanGreenLight,
    onSecondaryContainer = ArtisanGreenHover,
    tertiary = MicRed,
    background = WarmBackground,
    onBackground = TextDarkBrown,
    surface = BeigeCard,
    onSurface = TextDarkBrown,
    surfaceVariant = InputBeige,
    onSurfaceVariant = TextBrownSecondary
)

@Composable
fun ArtisanCatalogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // Artisan UX is optimized for high-contrast warm daylight readability
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = TerracottaPrimary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes,
        typography = Typography,
        content = content
    )
}
