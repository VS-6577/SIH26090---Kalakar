package com.sih2026.artisancatalog.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih2026.artisancatalog.ui.components.KalakritiBottomNav
import com.sih2026.artisancatalog.ui.components.NavTab
import com.sih2026.artisancatalog.ui.theme.ArtisanGreen
import com.sih2026.artisancatalog.ui.theme.BeigeCard
import com.sih2026.artisancatalog.ui.theme.BeigeCardBorder
import com.sih2026.artisancatalog.ui.theme.MicRed
import com.sih2026.artisancatalog.ui.theme.PureWhite
import com.sih2026.artisancatalog.ui.theme.TerracottaPrimary
import com.sih2026.artisancatalog.ui.theme.TextBrownSecondary
import com.sih2026.artisancatalog.ui.theme.TextDarkBrown
import com.sih2026.artisancatalog.ui.theme.WarmBackground
import com.sih2026.artisancatalog.ui.viewmodel.ProductViewModel

@Composable
fun DashboardScreen(
    viewModel: ProductViewModel,
    onNavigateToAddProduct: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToMarket: () -> Unit,
    modifier: Modifier = Modifier
) {
    val draftState by viewModel.draftState.collectAsState()

    Scaffold(
        bottomBar = {
            KalakritiBottomNav(
                activeTab = NavTab.HOME,
                onTabSelected = { tab ->
                    when (tab) {
                        NavTab.HOME -> { /* already home */ }
                        NavTab.MARKET -> onNavigateToMarket()
                        NavTab.SETTINGS -> onNavigateToSettings()
                        NavTab.NONE -> {}
                    }
                },
                onPlusClick = onNavigateToAddProduct
            )
        },
        containerColor = WarmBackground
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Greeting & Cluster Status
            Text(
                text = "Hello, ${draftState.artisanName}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDarkBrown
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${draftState.artisanCluster} • ",
                    fontSize = 14.sp,
                    color = TextBrownSecondary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "✓ Verified",
                    fontSize = 14.sp,
                    color = ArtisanGreen,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // CARD 1: TOP PERFORMER • DIGITAL PRODUCT PASSPORT
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = BorderStroke(1.dp, BeigeCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Header tag
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🥇", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Top Performer • Digital Product Passport",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TerracottaPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Terracotta pot icon / preview
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(BeigeCard),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🏺", fontSize = 48.sp)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "Terracotta Water Pot\n(5L)",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDarkBrown,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₹450",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = ArtisanGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFA000),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "4.8 / 5.0",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDarkBrown
                                )
                            }
                            Text(
                                text = "142 reviews across platforms",
                                fontSize = 11.sp,
                                color = TextBrownSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Total Revenue Earned pill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFFFF3E0))
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Total Revenue Earned: ₹28,800",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TerracottaPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // CARD 2: SEPTEMBER 2026 OVERVIEW
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = BorderStroke(1.dp, BeigeCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "September 2026 Overview",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDarkBrown
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "₹18,650",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TerracottaPrimary,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Total Monthly Earnings",
                        fontSize = 13.sp,
                        color = TextBrownSecondary
                    )

                    Spacer(modifier = Modifier.height(18.dp))
                    HorizontalDivider(color = Color(0xFFF0E8DF), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column {
                            Text(
                                text = "48",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = ArtisanGreen
                            )
                            Text(
                                text = "Total Items Sold",
                                fontSize = 12.sp,
                                color = TextBrownSecondary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(36.dp)
                                .background(Color(0xFFF0E8DF))
                        )

                        Column {
                            Text(
                                text = "1",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MicRed
                            )
                            Text(
                                text = "Total Returned",
                                fontSize = 12.sp,
                                color = TextBrownSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
