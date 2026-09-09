package com.sih2026.artisancatalog.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih2026.artisancatalog.R
import com.sih2026.artisancatalog.ui.theme.TerracottaPrimary
import com.sih2026.artisancatalog.ui.theme.TextBrownSecondary
import com.sih2026.artisancatalog.ui.theme.TextDarkBrown

@Composable
fun KalakritiLogo(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    showWordmark: Boolean = true,
    tagline: String? = "ARTISANS EMPOWER TOMORROW",
    animateEntrance: Boolean = false
) {
    // Subtle, premium logo entrance animation
    val opacity = remember { Animatable(if (animateEntrance) 0f else 1f) }
    val scale = remember { Animatable(if (animateEntrance) 0.88f else 1f) }
    val rotation = remember { Animatable(if (animateEntrance) -5f else 0f) }

    if (animateEntrance) {
        LaunchedEffect(Unit) {
            // Settle sequence: 0.88 -> 1.0, -5 deg -> +1.5 deg -> 0 deg
            scale.animateTo(1.0f, tween(600, easing = FastOutSlowInEasing))
            opacity.animateTo(1.0f, tween(500))
            rotation.animateTo(1.5f, tween(400, easing = FastOutSlowInEasing))
            rotation.animateTo(0f, tween(300, easing = FastOutSlowInEasing))
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo Mark (Exact vector conversion from authoritative SVG)
        Image(
            painter = painterResource(id = R.drawable.ic_kalakriti_vector_logo),
            contentDescription = "KALAKRITI Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(size)
                .alpha(opacity.value)
                .scale(scale.value)
                .rotate(rotation.value)
        )

        if (showWordmark) {
            Spacer(modifier = Modifier.height(10.dp))
            // KALAKRITI wordmark
            Text(
                text = "K A L A K R I T I",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                color = TerracottaPrimary,
                fontFamily = FontFamily.Serif
            )

            if (!tagline.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "—",
                        color = TerracottaPrimary.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "🪷",
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "—",
                        color = TerracottaPrimary.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tagline,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    color = TextBrownSecondary
                )
            }
        }
    }
}
