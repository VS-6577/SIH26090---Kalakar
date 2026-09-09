package com.sih2026.artisancatalog.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.sih2026.artisancatalog.ui.components.AccessibleButton
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
import kotlinx.coroutines.launch

@Composable
fun AddProductScreen(
    viewModel: ProductViewModel,
    onNavigateToGuidedCamera: () -> Unit,
    onNavigateToPricing: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToMarket: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val draftState by viewModel.draftState.collectAsState()
    val isListening by viewModel.isListening.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "micPulse")
    val micScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.25f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "micScale"
    )

    // Camera permission
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onNavigateToGuidedCamera()
        } else {
            // Load sample craft photos if permission denied
            viewModel.useSampleCraftPhotos()
            scope.launch {
                snackbarHostState.showSnackbar("Camera permission denied. Loaded sample craft photos.")
            }
        }
    }

    // Audio permission
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startVoiceRecognition()
        } else {
            viewModel.simulateArtisanVoice()
            scope.launch {
                snackbarHostState.showSnackbar("Microphone permission denied. Switched to demo voice input.")
            }
        }
    }

    fun handleCameraClick() {
        val check = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (check == PackageManager.PERMISSION_GRANTED) {
            onNavigateToGuidedCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun handleStartVoice() {
        val check = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        if (check == PackageManager.PERMISSION_GRANTED) {
            viewModel.startVoiceRecognition()
        } else {
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    val hasPhoto = draftState.frontPhoto != null

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
                onPlusClick = { /* Already on add product */ }
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
                    text = "Add your Product",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // BACKEND STATUS PILL
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (draftState.isBackendOnline) Color(0xFFE8F5E9) else Color(0xFFFFF3E0))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (draftState.isBackendOnline) ArtisanGreen else TerracottaPrimary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (draftState.isBackendOnline) "Backend Online (${draftState.serverUrl.replace("http://", "").replace("/", "")})" else "Offline Mode (Local)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (draftState.isBackendOnline) ArtisanGreen else TerracottaPrimary
                    )
                }

                if (draftState.isAnalyzingWithAi) {
                    Text(
                        text = "AI Analyzing...",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TerracottaPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // PHOTO CAPTURE AREA (Large Beige Card with Thin Terracotta Border)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clickable { handleCameraClick() },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BeigeCard),
                border = BorderStroke(1.2.dp, if (hasPhoto) TerracottaPrimary else BeigeCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (hasPhoto) {
                        AsyncImage(
                            model = draftState.frontPhoto,
                            contentDescription = "Captured Product",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(24.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFEFE6))
                                    .border(1.5.dp, TerracottaPrimary.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = "Camera",
                                    tint = TerracottaPrimary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "तस्वीर लेने के लिए टैप करें",
                                fontSize = 15.sp,
                                color = TextDarkBrown,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // GREEN ACTION BUTTON: CLICK PHOTO / CHANGE PHOTO
            AccessibleButton(
                text = if (hasPhoto) "CHANGE PHOTO" else "CLICK PHOTO",
                onClick = { handleCameraClick() },
                backgroundColor = ArtisanGreen,
                iconVector = Icons.Default.PhotoCamera
            )

            Spacer(modifier = Modifier.height(22.dp))

            // ORANGE MICROPHONE BUTTON WITH "Hold to speak"
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(90.dp)
                ) {
                    if (isListening) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .scale(micScale)
                                .clip(CircleShape)
                                .background(MicRed.copy(alpha = 0.25f))
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(66.dp)
                            .clip(CircleShape)
                            .background(if (isListening) MicRed else TerracottaPrimary)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        handleStartVoice()
                                        tryAwaitRelease()
                                        viewModel.stopVoiceRecognition()
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Hold to speak",
                            tint = PureWhite,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isListening) "Listening..." else "Hold to speak",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isListening) MicRed else TextDarkBrown
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // VOICE DESCRIPTION CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = BorderStroke(1.dp, BeigeCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                        Text(
                            text = "Voice Description",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDarkBrown
                        )

                        // Quick demo shortcut
                        Text(
                            text = "Demo Speech",
                            fontSize = 12.sp,
                            color = TerracottaPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { viewModel.simulateArtisanVoice() }
                                .padding(4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8F5)),
                        border = BorderStroke(1.dp, Color(0xFFEBE2D8))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                        ) {
                            if (draftState.voiceTranscription.isEmpty()) {
                                Text(
                                    text = "Speak about your product: material, method, time spent...",
                                    fontSize = 13.sp,
                                    color = TextBrownSecondary,
                                    lineHeight = 18.sp
                                )
                            }
                            BasicTextField(
                                value = draftState.voiceTranscription,
                                onValueChange = { viewModel.updateVoiceTranscription(it) },
                                textStyle = TextStyle(
                                    fontSize = 14.sp,
                                    color = TextDarkBrown,
                                    lineHeight = 20.sp
                                ),
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // PROCEED TO STEP 2 BUTTON -> PRICING WIZARD
            AccessibleButton(
                text = "Proceed to Step 2 →",
                onClick = {
                    viewModel.analyzeCapturedProduct()
                    onNavigateToPricing()
                },
                backgroundColor = ArtisanGreen
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
