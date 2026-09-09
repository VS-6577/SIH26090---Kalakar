package com.sih2026.artisancatalog.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sih2026.artisancatalog.ui.components.AccessibleButton
import com.sih2026.artisancatalog.ui.components.KalakritiBottomNav
import com.sih2026.artisancatalog.ui.components.NavTab
import com.sih2026.artisancatalog.ui.theme.ArtisanGreen
import com.sih2026.artisancatalog.ui.theme.BeigeCard
import com.sih2026.artisancatalog.ui.theme.BeigeCardBorder
import com.sih2026.artisancatalog.ui.theme.PureWhite
import com.sih2026.artisancatalog.ui.theme.TerracottaPrimary
import com.sih2026.artisancatalog.ui.theme.TextBrownSecondary
import com.sih2026.artisancatalog.ui.theme.TextDarkBrown
import com.sih2026.artisancatalog.ui.theme.WarmBackground
import com.sih2026.artisancatalog.ui.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@Composable
fun ReviewScreen(
    viewModel: ProductViewModel,
    onNavigateToPricingSummary: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToMarket: () -> Unit,
    modifier: Modifier = Modifier
) {
    val draftState by viewModel.draftState.collectAsState()
    val scope = rememberCoroutineScope()

    // 6 Captured photos or AI Studio Mockups
    val photoList = draftState.studioMockupList.ifEmpty { listOf("") }
    val pagerState = rememberPagerState(pageCount = { photoList.size })

    Scaffold(
        bottomBar = {
            KalakritiBottomNav(
                activeTab = NavTab.NONE,
                onTabSelected = { tab ->
                    when (tab) {
                        NavTab.HOME -> onNavigateToHome()
                        NavTab.MARKET -> onNavigateToMarket()
                        NavTab.SETTINGS -> onNavigateToSettings()
                        NavTab.NONE -> {}
                    }
                },
                onPlusClick = {}
            )
        },
        containerColor = WarmBackground
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TOP HEADER: Terracotta Rounded Banner with Centered Title & Circular Back Arrow
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(TerracottaPrimary)
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                // Circular back button on the left
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0x33000000))
                        .clickable { onNavigateBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PureWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Centered Title
                Text(
                    text = "Review",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // SECTION 1: STUDIO PHOTOS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Studio Photos",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDarkBrown
                )

                if (draftState.isGeneratingMockups) {
                    Text(
                        text = "Generating Mockup...",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TerracottaPrimary
                    )
                } else if (draftState.isBackendOnline) {
                    Text(
                        text = "+ AI Studio Shoot",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TerracottaPrimary,
                        modifier = Modifier
                            .clickable { viewModel.generateStudioMockup("hero") }
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Carousel Card matching review.png
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BeigeCard),
                border = BorderStroke(1.2.dp, BeigeCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { pageIndex ->
                        val photoUri = photoList.getOrNull(pageIndex).orEmpty()
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (photoUri.isNotBlank()) {
                                AsyncImage(
                                    model = photoUri,
                                    contentDescription = "Studio Photo",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(24.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoCamera,
                                        contentDescription = null,
                                        tint = TerracottaPrimary.copy(alpha = 0.45f),
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Craft Photo Preview",
                                        fontSize = 14.sp,
                                        color = TextBrownSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // Left Chevron Button
                    IconButton(
                        onClick = {
                            if (pagerState.currentPage > 0) {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 12.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PureWhite.copy(alpha = 0.9f))
                    ) {
                        Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Previous Photo", tint = TextDarkBrown)
                    }

                    // Right Chevron Button
                    IconButton(
                        onClick = {
                            val maxIndex = (photoList.size.coerceAtLeast(4)) - 1
                            if (pagerState.currentPage < maxIndex) {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 12.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PureWhite.copy(alpha = 0.9f))
                    ) {
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next Photo", tint = TextDarkBrown)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Indicator Dots below Carousel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { dotIndex ->
                    val isSelected = pagerState.currentPage == dotIndex
                    val width = if (isSelected) 22.dp else 7.dp
                    val color = if (isSelected) TerracottaPrimary else Color(0xFFD6CCC2)
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(width = width, height = 7.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // SECTION 2: PRODUCT DESCRIPTION AND HISTORY
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = BorderStroke(1.dp, BeigeCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Title row with EN / हिन्दी toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Product Description and\nHistory",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDarkBrown,
                            lineHeight = 22.sp
                        )

                        // [ EN | हिन्दी ] Capsule Toggle
                        val isEn = draftState.reviewLanguageTab == "EN"
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFFEDE5DC))
                                .padding(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(if (isEn) TerracottaPrimary else Color.Transparent)
                                    .clickable { viewModel.setReviewLanguageTab("EN") }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "EN",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isEn) PureWhite else TextDarkBrown
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(if (!isEn) TerracottaPrimary else Color.Transparent)
                                    .clickable { viewModel.setReviewLanguageTab("HI") }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "हिन्दी",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!isEn) PureWhite else TextDarkBrown
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = draftState.productTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDarkBrown
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = draftState.productDescription,
                        fontSize = 13.sp,
                        color = TextBrownSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Peach Card with Star: Heritage & Craft Story
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7F2)),
                        border = BorderStroke(1.dp, Color(0xFFFFE0D0))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "★", color = TerracottaPrimary, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Heritage & Craft Story:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TerracottaPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = draftState.craftHistory,
                                fontSize = 12.sp,
                                color = TextBrownSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tag Chips: Handmade, GI Tagged, Eco-Friendly
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        draftState.tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFF3EBE3))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = tag,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextDarkBrown
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // GREEN ACTION BUTTON: Proceed to Last Step → (Navigates to Pricing Summary)
            AccessibleButton(
                text = "Proceed to Last Step →",
                onClick = onNavigateToPricingSummary,
                backgroundColor = ArtisanGreen
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
