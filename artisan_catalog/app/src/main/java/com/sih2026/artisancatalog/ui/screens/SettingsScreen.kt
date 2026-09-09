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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun SettingsScreen(
    viewModel: ProductViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToAddProduct: () -> Unit,
    onNavigateToLanguageSelection: () -> Unit,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val draftState by viewModel.draftState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            KalakritiBottomNav(
                activeTab = NavTab.SETTINGS,
                onTabSelected = { tab ->
                    when (tab) {
                        NavTab.HOME -> onNavigateToHome()
                        NavTab.MARKET -> onNavigateToMarket()
                        NavTab.SETTINGS -> { /* already here */ }
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

            // Screen Title
            Text(
                text = "Settings & Profile",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextDarkBrown
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Artisan Profile Card (Ram Kumar)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            snackbarHostState.showSnackbar("Artisan profile verified under Ministry of Textiles (O/o DC Handicrafts)")
                        }
                    },
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar circle
                    Box(
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .background(TerracottaPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "R",
                            color = PureWhite,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ram Kumar",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDarkBrown
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (draftState.userMobileNumber.isNotEmpty()) "+91 ${draftState.userMobileNumber}" else "+91 98765 43210",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = TextBrownSecondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Gorakhpur Cluster • Artisan ID:\nART-8831",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextBrownSecondary,
                            lineHeight = 16.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "View Profile",
                        tint = TerracottaPrimary.copy(alpha = 0.8f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 1: ACCOUNT
            SectionHeader(title = "ACCOUNT")

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SettingsRowItem(
                        icon = Icons.Default.Person,
                        iconTint = TextDarkBrown,
                        title = "Personal Details & Craft Category",
                        subtitle = "Terracotta Potter",
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Craft Category: Terracotta Pottery (Gorakhpur GI)")
                            }
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.8.dp,
                        color = BeigeCardBorder.copy(alpha = 0.5f)
                    )

                    SettingsRowItem(
                        icon = Icons.Default.Verified,
                        iconTint = ArtisanGreen,
                        title = "Govt Artisan ID & GI Tag Verification",
                        subtitle = "Verified ✓",
                        subtitleColor = ArtisanGreen,
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("GI Tag Certificate #GI-UP-2019-012 Valid")
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // SECTION 2: PREFERENCES
            SectionHeader(title = "PREFERENCES")

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SettingsRowItem(
                        icon = Icons.Default.Translate,
                        iconTint = TextDarkBrown,
                        title = "App Language",
                        subtitle = "Currently ${draftState.selectedLanguage} • Switch to other",
                        onClick = onNavigateToLanguageSelection
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.8.dp,
                        color = BeigeCardBorder.copy(alpha = 0.5f)
                    )

                    SettingsRowItem(
                        icon = Icons.Default.LightMode,
                        iconTint = TextDarkBrown,
                        title = "Display Theme",
                        subtitle = "Light / Dark Mode",
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("High-contrast terracotta theme active for sunlight visibility")
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // SECTION 3: SYSTEM & PRIVACY
            SectionHeader(title = "SYSTEM & PRIVACY")

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SettingsRowItem(
                        icon = Icons.Default.CameraAlt,
                        iconTint = TextDarkBrown,
                        title = "Device Permissions",
                        subtitle = "Camera & Microphone access",
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Camera & Audio permissions granted for offline scanning")
                            }
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.8.dp,
                        color = BeigeCardBorder.copy(alpha = 0.5f)
                    )

                    SettingsRowItem(
                        icon = Icons.Default.CloudDone,
                        iconTint = TextDarkBrown,
                        title = "Offline Data Cache",
                        subtitle = "All items synced",
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("100% Offline SQLite database active. 0 pending cloud syncs.")
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // AI BACKEND & NETWORK
            SectionHeader(title = "AI BACKEND & NETWORK")
            Spacer(modifier = Modifier.height(10.dp))

            var serverUrlInput by remember(draftState.serverUrl) { mutableStateOf(draftState.serverUrl) }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Wifi,
                                contentDescription = "Network",
                                tint = if (draftState.isBackendOnline) ArtisanGreen else TerracottaPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Server Status:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDarkBrown
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (draftState.isBackendOnline) Color(0xFFE8F5E9) else Color(0xFFFFF3E0))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (draftState.isBackendOnline) "ONLINE" else "OFFLINE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (draftState.isBackendOnline) ArtisanGreen else TerracottaPrimary
                                )
                            }
                            IconButton(
                                onClick = { viewModel.checkBackendHealth() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    tint = TextBrownSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Backend Server URL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDarkBrown
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = serverUrlInput,
                        onValueChange = { serverUrlInput = it },
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

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.updateServerUrl(serverUrlInput)
                            scope.launch {
                                snackbarHostState.showSnackbar("Server URL updated. Testing connection...")
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ArtisanGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text(
                            text = "Save & Connect Server",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Default local Wi-Fi: http://10.10.159.148:3001/\nEmulator loopback: http://10.0.2.2:3001/",
                        fontSize = 11.sp,
                        color = TextBrownSecondary,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // LOG OUT BUTTON
            OutlinedButton(
                onClick = onLogOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, Color(0xFFD32F2F)),
                colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                    containerColor = PureWhite
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Log Out",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Log Out of Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = TextBrownSecondary
    )
}

@Composable
private fun SettingsRowItem(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    subtitleColor: Color = TextBrownSecondary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon container box
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BeigeCard),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextDarkBrown
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = subtitleColor
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Go",
            tint = TextBrownSecondary.copy(alpha = 0.6f),
            modifier = Modifier.size(20.dp)
        )
    }
}
