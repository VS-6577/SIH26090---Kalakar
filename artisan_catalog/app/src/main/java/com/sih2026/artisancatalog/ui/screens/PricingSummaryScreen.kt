package com.sih2026.artisancatalog.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih2026.artisancatalog.ui.components.AccessibleButton
import com.sih2026.artisancatalog.ui.components.KalakritiBottomNav
import com.sih2026.artisancatalog.ui.components.NavTab
import com.sih2026.artisancatalog.ui.theme.ArtisanGreen
import com.sih2026.artisancatalog.ui.theme.ArtisanGreenHover
import com.sih2026.artisancatalog.ui.theme.ArtisanGreenLight
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
fun PricingSummaryScreen(
    viewModel: ProductViewModel,
    onLaunchSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToMarket: () -> Unit,
    modifier: Modifier = Modifier
) {
    val draftState by viewModel.draftState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            // TOP HEADER: Terracotta Rounded Banner with Back Arrow & "Pricing Summary"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(TerracottaPrimary)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = PureWhite,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Pricing Summary",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // CARD 1: COST BENCHMARK COMPARISON
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
                    Text(
                        text = "COST BENCHMARK COMPARISON",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDarkBrown,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Card 1: AI Market Estimate
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = BeigeCard)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "AI Market Estimate",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextDarkBrown
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "₹${draftState.aiMarketEstimate.toInt()}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDarkBrown
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Market average",
                                    fontSize = 11.sp,
                                    color = TextBrownSecondary
                                )
                            }
                        }

                        // Card 2: Your True Cost (Terracotta Border)
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            border = BorderStroke(1.5.dp, TerracottaPrimary)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Your True Cost",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TerracottaPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "₹${draftState.calculatedTrueCost.toInt()}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TerracottaPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Actual breakeven",
                                    fontSize = 11.sp,
                                    color = TextBrownSecondary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // CARD 2: RECOMMENDED SELLING RANGE
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
                    Text(
                        text = "Recommended Selling Range",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDarkBrown
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Floating Sweet Spot Badge pointing to middle of track
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(ArtisanGreenHover)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Sweet Spot: ₹${draftState.sweetSpotPrice.toInt()}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Gradient Track (Orange to Green) with Marker
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Track Line
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(TerracottaPrimary, ArtisanGreen, TerracottaPrimary)
                                    )
                                )
                        )

                        // Center Circle Marker
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(PureWhite)
                                .border(3.dp, ArtisanGreenHover, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Min & Max Labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Min: ₹${draftState.minSellingPrice.toInt()}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TerracottaPrimary
                            )
                            Text(text = "No Profit", fontSize = 10.sp, color = TextBrownSecondary)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Max: ₹${draftState.maxSellingPrice.toInt()}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TerracottaPrimary
                            )
                            Text(text = "Competitive Limit", fontSize = 10.sp, color = TextBrownSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Green Wage Protection Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(ArtisanGreenLight)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "At the ₹${draftState.sweetSpotPrice.toInt()} sweet spot, your daily labor\nwage is fully protected with ${draftState.marginPercent.toInt()}% margin.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = ArtisanGreenHover,
                            lineHeight = 19.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // GREEN ACTION BUTTON: Launch to Marketplace
            AccessibleButton(
                text = "Launch to Marketplace",
                onClick = {
                    viewModel.saveProduct {
                        scope.launch {
                            snackbarHostState.showSnackbar("Catalog published successfully to ONDC marketplace!")
                        }
                        onLaunchSuccess()
                    }
                },
                backgroundColor = ArtisanGreen
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
