package com.sih2026.artisancatalog.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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

@Composable
fun EstimatedPricingScreen(
    viewModel: ProductViewModel,
    onNavigateToReview: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToMarket: () -> Unit,
    modifier: Modifier = Modifier
) {
    val draftState by viewModel.draftState.collectAsState()
    var currentStep by remember { mutableIntStateOf(1) } // 1: Material, 2: Labor, 3: Logistics, 4: Margin

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
                onPlusClick = { /* already in creation */ }
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
                        .clickable {
                            if (currentStep > 1) {
                                currentStep--
                            } else {
                                onNavigateBack()
                            }
                        },
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
                    text = "Estimated Pricing",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CARD 1: ESTIMATED PRICE BANNER (Dynamic State)
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
                        .padding(vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Estimated Price",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDarkBrown
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "₹${draftState.calculatedFinalPrice.toInt()}",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TerracottaPrimary,
                        letterSpacing = (-1).sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CARD 2: QUESTION WIZARD (Q1 - Q4)
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
                    // TAB ROW: Q1 Material | Q2 Labor | Q3 Logistics | Q4 Margin
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PricingTabItem(label = "Material", step = 1, currentStep = currentStep) { currentStep = 1 }
                        PricingTabItem(label = "Labor", step = 2, currentStep = currentStep) { currentStep = 2 }
                        PricingTabItem(label = "Logistics", step = 3, currentStep = currentStep) { currentStep = 3 }
                        PricingTabItem(label = "Margin", step = 4, currentStep = currentStep) { currentStep = 4 }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // QUESTION CONTENT BASED ON ACTIVE STEP
                    when (currentStep) {
                        1 -> Part1MaterialContent(viewModel, draftState)
                        2 -> Part2LaborContent(viewModel, draftState)
                        3 -> Part3LogisticsContent(viewModel, draftState)
                        4 -> Part4MarginContent(viewModel, draftState)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // BOTTOM CONTROLS INSIDE CARD: < Prev | Dots | Next Question >
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Prev Button
                        val isPrevEnabled = currentStep > 1
                        OutlinedButton(
                            onClick = { if (isPrevEnabled) currentStep-- },
                            enabled = isPrevEnabled,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, if (isPrevEnabled) Color(0xFFDCC8B8) else Color(0xFFE5DDD5)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color(0xFFF7F4EF),
                                disabledContainerColor = Color(0xFFFBF8F5),
                                contentColor = TextDarkBrown,
                                disabledContentColor = Color(0xFF9E9287)
                            ),
                            modifier = Modifier.height(46.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Prev", modifier = Modifier.size(18.dp))
                            Text("Prev", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }

                        // 4 Indicators
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(4) { i ->
                                val stepIndex = i + 1
                                val isActive = stepIndex == currentStep
                                val width = if (isActive) 22.dp else 7.dp
                                val color = if (isActive) TerracottaPrimary else Color(0xFFD6CCC2)
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 3.dp)
                                        .size(width = width, height = 7.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                )
                            }
                        }

                        // Next Question Button (only on steps 1, 2, 3; omitted on step 4)
                        if (currentStep < 4) {
                            Button(
                                onClick = { currentStep++ },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary),
                                modifier = Modifier.height(46.dp)
                            ) {
                                Text(
                                    text = "Next Question",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next", modifier = Modifier.size(18.dp))
                            }
                        } else {
                            // Empty box matching prev width to keep dots centered
                            Box(modifier = Modifier.width(80.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // GREEN ACTION BUTTON: Proceed to Step 3 → (Direct jump to Review)
            AccessibleButton(
                text = "Proceed to Step 3 →",
                onClick = {
                    viewModel.generateCatalogContent()
                    onNavigateToReview()
                },
                backgroundColor = ArtisanGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            // AI Footnote
            Text(
                text = "AI pricing is based on regional craft market data.\nYou can edit anytime from your seller dashboard.",
                fontSize = 11.sp,
                color = TextBrownSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PricingTabItem(
    label: String,
    step: Int,
    currentStep: Int,
    onClick: () -> Unit
) {
    val isSelected = step == currentStep
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 4.dp)
    ) {
        Text(
            text = "Q$step",
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) TerracottaPrimary else TextBrownSecondary
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) TerracottaPrimary else TextBrownSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(TerracottaPrimary)
            )
        } else {
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

// -------------------------------------------------------------
// PART 1: MATERIAL
// -------------------------------------------------------------
@Composable
private fun Part1MaterialContent(viewModel: ProductViewModel, draftState: com.sih2026.artisancatalog.ui.viewmodel.ProductDraftState) {
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    val categories = listOf("Textile", "Pottery", "Woodcraft", "Metalwork", "Jewelry", "Leather")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "What did raw materials cost for this piece?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextDarkBrown,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Include clay, thread, dyes, or any base craft inputs.",
            fontSize = 13.sp,
            color = TextBrownSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Material Cost Field
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Material Cost", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextDarkBrown)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = draftState.materialCost.toInt().toString(),
                    onValueChange = { str ->
                        val parsed = str.toDoubleOrNull() ?: 0.0
                        viewModel.updateMaterialCost(parsed)
                    },
                    leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, color = TextDarkBrown) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFFBF8F5),
                        unfocusedContainerColor = Color(0xFFFBF8F5),
                        focusedBorderColor = TerracottaPrimary,
                        unfocusedBorderColor = Color(0xFFDCC8B8)
                    ),
                    singleLine = true
                )
            }

            // Category Picker Field
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Category", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextDarkBrown)
                Spacer(modifier = Modifier.height(6.dp))
                Box {
                    OutlinedTextField(
                        value = "🧵 ${draftState.materialCategory}",
                        onValueChange = {},
                        readOnly = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFFBF8F5),
                            unfocusedContainerColor = Color(0xFFFBF8F5),
                            focusedBorderColor = TerracottaPrimary,
                            unfocusedBorderColor = Color(0xFFDCC8B8)
                        ),
                        modifier = Modifier.clickable { categoryMenuExpanded = true },
                        singleLine = true
                    )
                    DropdownMenu(
                        expanded = categoryMenuExpanded,
                        onDismissRequest = { categoryMenuExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    viewModel.updateMaterialCategory(cat)
                                    categoryMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Green result card: Material Input Total ₹250
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(ArtisanGreenLight)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Material Input Total",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisanGreenHover
                )
                Text(
                    text = "₹${draftState.materialCost.toInt()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ArtisanGreenHover
                )
            }
        }
    }
}

// -------------------------------------------------------------
// PART 2: LABOR
// -------------------------------------------------------------
@Composable
private fun Part2LaborContent(viewModel: ProductViewModel, draftState: com.sih2026.artisancatalog.ui.viewmodel.ProductDraftState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "How much craft effort went into this item?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextDarkBrown,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Set your time and fair daily wage to compute labor cost.",
            fontSize = 13.sp,
            color = TextBrownSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Segmented Controls: Days / Hours
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val isDays = draftState.laborMode == "Days"
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isDays) Color(0xFFFFEFE6) else PureWhite)
                    .border(1.5.dp, if (isDays) TerracottaPrimary else Color(0xFFDCC8B8), RoundedCornerShape(14.dp))
                    .clickable { viewModel.updateLaborMode("Days") },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Days",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDays) TerracottaPrimary else TextDarkBrown
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (!isDays) Color(0xFFFFEFE6) else PureWhite)
                    .border(1.5.dp, if (!isDays) TerracottaPrimary else Color(0xFFDCC8B8), RoundedCornerShape(14.dp))
                    .clickable { viewModel.updateLaborMode("Hours") },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Hours",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (!isDays) TerracottaPrimary else TextDarkBrown
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Time Spent (${draftState.laborMode})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextDarkBrown)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = draftState.timeSpent.toInt().toString(),
                    onValueChange = { str ->
                        val parsed = str.toDoubleOrNull() ?: 1.0
                        viewModel.updateTimeSpent(parsed)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFFBF8F5),
                        unfocusedContainerColor = Color(0xFFFBF8F5),
                        focusedBorderColor = TerracottaPrimary,
                        unfocusedBorderColor = Color(0xFFDCC8B8)
                    ),
                    singleLine = true
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Daily Wage", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextDarkBrown)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = draftState.dailyWage.toInt().toString(),
                    onValueChange = { str ->
                        val parsed = str.toDoubleOrNull() ?: 350.0
                        viewModel.updateDailyWage(parsed)
                    },
                    leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, color = TextDarkBrown) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFFBF8F5),
                        unfocusedContainerColor = Color(0xFFFBF8F5),
                        focusedBorderColor = TerracottaPrimary,
                        unfocusedBorderColor = Color(0xFFDCC8B8)
                    ),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Green result card: Calculated Fair Wage ₹700
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(ArtisanGreenLight)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Calculated Fair Wage",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisanGreenHover
                )
                Text(
                    text = "₹${draftState.calculatedLaborCost.toInt()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ArtisanGreenHover
                )
            }
        }
    }
}

// -------------------------------------------------------------
// PART 3: LOGISTICS
// -------------------------------------------------------------
@Composable
private fun Part3LogisticsContent(viewModel: ProductViewModel, draftState: com.sih2026.artisancatalog.ui.viewmodel.ProductDraftState) {
    val boxTypes = listOf("Eco Jute Box", "Bubble Wrap", "Plain Box")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "What are your packaging and transport costs?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextDarkBrown,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Includes local travel, courier pickup, and packaging materials.",
            fontSize = 13.sp,
            color = TextBrownSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Packaging", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextDarkBrown)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = draftState.packagingCost.toInt().toString(),
                    onValueChange = { str ->
                        val parsed = str.toDoubleOrNull() ?: 0.0
                        viewModel.updatePackagingCost(parsed)
                    },
                    leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, color = TextDarkBrown) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFFBF8F5),
                        unfocusedContainerColor = Color(0xFFFBF8F5),
                        focusedBorderColor = TerracottaPrimary,
                        unfocusedBorderColor = Color(0xFFDCC8B8)
                    ),
                    singleLine = true
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Transport", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextDarkBrown)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = draftState.transportCost.toInt().toString(),
                    onValueChange = { str ->
                        val parsed = str.toDoubleOrNull() ?: 0.0
                        viewModel.updateTransportCost(parsed)
                    },
                    leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, color = TextDarkBrown) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFFBF8F5),
                        unfocusedContainerColor = Color(0xFFFBF8F5),
                        focusedBorderColor = TerracottaPrimary,
                        unfocusedBorderColor = Color(0xFFDCC8B8)
                    ),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3 Packaging Choice Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            boxTypes.forEach { type ->
                val isSelected = draftState.packagingType == type
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Color(0xFFFFEFE6) else PureWhite)
                        .border(1.2.dp, if (isSelected) TerracottaPrimary else Color(0xFFDCC8B8), RoundedCornerShape(12.dp))
                        .clickable { viewModel.updatePackagingType(type) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = type,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) TerracottaPrimary else TextDarkBrown
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Green result card: Logistics Total ₹50
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(ArtisanGreenLight)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Logistics Total",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisanGreenHover
                )
                Text(
                    text = "₹${draftState.calculatedLogisticsCost.toInt()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ArtisanGreenHover
                )
            }
        }
    }
}

// -------------------------------------------------------------
// PART 4: MARGIN
// -------------------------------------------------------------
@Composable
private fun Part4MarginContent(viewModel: ProductViewModel, draftState: com.sih2026.artisancatalog.ui.viewmodel.ProductDraftState) {
    val marginPresets = listOf(20.0, 35.0, 50.0)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "What profit margin would you like to earn?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextDarkBrown,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "35% is the recommended minimum for a fair artisan wage.",
            fontSize = 13.sp,
            color = TextBrownSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4 Choices: 20%, 35%, 50%, Custom
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            marginPresets.forEach { m ->
                val isSelected = !draftState.isCustomMargin && draftState.marginPercent == m
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) Color(0xFFFFEFE6) else PureWhite)
                        .border(1.5.dp, if (isSelected) TerracottaPrimary else Color(0xFFDCC8B8), RoundedCornerShape(14.dp))
                        .clickable { viewModel.updateMarginPercent(m, false) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${m.toInt()}%",
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) TerracottaPrimary else TextDarkBrown
                    )
                }
            }

            // Custom Pill
            val isCustomSelected = draftState.isCustomMargin
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isCustomSelected) Color(0xFFFFEFE6) else PureWhite)
                    .border(1.5.dp, if (isCustomSelected) TerracottaPrimary else Color(0xFFDCC8B8), RoundedCornerShape(14.dp))
                    .clickable { viewModel.updateMarginPercent(draftState.marginPercent, true) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Custom",
                    fontSize = 14.sp,
                    fontWeight = if (isCustomSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isCustomSelected) TerracottaPrimary else TextDarkBrown
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Custom Margin Field
        Text(text = "Custom Margin %", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextDarkBrown)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = draftState.customMarginInput,
            onValueChange = { str ->
                val parsed = str.toDoubleOrNull() ?: 35.0
                viewModel.updateMarginPercent(parsed, isCustom = true, customText = str)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFFBF8F5),
                unfocusedContainerColor = Color(0xFFFBF8F5),
                focusedBorderColor = TerracottaPrimary,
                unfocusedBorderColor = Color(0xFFDCC8B8)
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Green banner showing Profit & Listing Price
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(ArtisanGreenLight)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Artisan Profit",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArtisanGreenHover
                    )
                    Text(
                        text = "₹${draftState.calculatedArtisanProfit.toInt()}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ArtisanGreenHover
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Final Listing Price",
                        fontSize = 13.sp,
                        color = TextBrownSecondary
                    )
                    Text(
                        text = "₹${draftState.calculatedFinalPrice.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TerracottaPrimary
                    )
                }
            }
        }
    }
}
