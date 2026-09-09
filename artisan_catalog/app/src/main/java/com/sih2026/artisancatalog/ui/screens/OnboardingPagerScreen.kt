package com.sih2026.artisancatalog.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih2026.artisancatalog.R
import com.sih2026.artisancatalog.ui.theme.PureWhite
import com.sih2026.artisancatalog.ui.theme.TerracottaPrimary
import com.sih2026.artisancatalog.ui.theme.TextBrownSecondary
import com.sih2026.artisancatalog.ui.theme.TextDarkBrown
import com.sih2026.artisancatalog.ui.theme.WarmBackground

import com.sih2026.artisancatalog.ui.components.KalakritiLogo

data class OnboardingPageData(
    val imageRes: Int,
    val title: String,
    val description: String,
    val isLastPage: Boolean = false
)

@Composable
fun OnboardingPagerScreen(
    onNavigateToLanguageSelection: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = listOf(
        OnboardingPageData(
            imageRes = R.drawable.art_smart_catalog,
            title = "Smart Catalog",
            description = "Create product names, descriptions\nand tags with AI in seconds."
        ),
        OnboardingPageData(
            imageRes = R.drawable.art_fair_price,
            title = "Fair Price Suggestions",
            description = "Get transparent price suggestions\nthat value your craft and effort."
        ),
        OnboardingPageData(
            imageRes = R.drawable.art_language,
            title = "In Your Own Language",
            description = "Create and manage your product listings\nin the language you are most comfortable with."
        ),
        OnboardingPageData(
            imageRes = R.drawable.art_reach_customers,
            title = "Reach More Customers",
            description = "Showcase your crafted products and\nconnect directly with buyers on ONDC.",
            isLastPage = true
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            val page = pages[pageIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 18.dp, bottom = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Brand Header (matches reference screens 2-5)
                KalakritiLogo(
                    size = 72.dp,
                    showWordmark = true,
                    tagline = "ARTISANS EMPOWER TOMORROW",
                    animateEntrance = false
                )

                // Central Artwork Illustration
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = page.imageRes),
                        contentDescription = page.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Bottom Content Area (Title, Underline, Description, Dots, CTA)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = page.title,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDarkBrown,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Decorative underline
                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(1.5.dp))
                            .background(TerracottaPrimary)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = page.description,
                        fontSize = 14.sp,
                        color = TextBrownSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4-Dot Page Indicators
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(pages.size) { dotIndex ->
                            val isSelected = pagerState.currentPage == dotIndex
                            val width = if (isSelected) 22.dp else 8.dp
                            val color = if (isSelected) TerracottaPrimary else TerracottaPrimary.copy(alpha = 0.25f)

                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(width = width, height = 8.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // CTA: Fixed height container to prevent layout jump across pages
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (page.isLastPage) {
                            Button(
                                onClick = onNavigateToLanguageSelection,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(26.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                            ) {
                                Text(
                                    text = "Get Started →",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite
                                )
                            }
                        } else {
                            Text(
                                text = "swipe to explore →",
                                fontSize = 13.sp,
                                color = TextBrownSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
