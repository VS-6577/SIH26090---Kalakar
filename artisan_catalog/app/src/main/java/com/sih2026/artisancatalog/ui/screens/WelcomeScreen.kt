package com.sih2026.artisancatalog.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih2026.artisancatalog.R
import com.sih2026.artisancatalog.ui.components.KalakritiLogo
import com.sih2026.artisancatalog.ui.theme.TerracottaPrimary
import com.sih2026.artisancatalog.ui.theme.TextBrownSecondary
import com.sih2026.artisancatalog.ui.theme.TextDarkBrown
import com.sih2026.artisancatalog.ui.theme.WarmBackground
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    onNavigateToOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Auto proceed or tap to proceed after logo entrance
    LaunchedEffect(Unit) {
        delay(2600)
        onNavigateToOnboarding()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
            .clickable { onNavigateToOnboarding() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Central Branding & Logo with entrance animation
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                KalakritiLogo(
                    size = 130.dp,
                    showWordmark = true,
                    tagline = null,
                    animateEntrance = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "—", color = TerracottaPrimary.copy(alpha = 0.5f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "🪷", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "—", color = TerracottaPrimary.copy(alpha = 0.5f), fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Welcome to KalaKriti",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDarkBrown,
                    fontFamily = FontFamily.Serif
                )
            }

            // Lower Rural Village Artwork Illustration
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.art_welcome_village),
                    contentDescription = "Rural Indian Village Illustration",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Indicator Pill (matching reference with orange dot)
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(TerracottaPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(TerracottaPrimary)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Footer Text
                Text(
                    text = "Empowering Rural Artisans • ONDC Enabled",
                    fontSize = 12.sp,
                    color = TextBrownSecondary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
