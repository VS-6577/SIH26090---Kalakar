package com.sih2026.artisancatalog.ui.screens

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.sih2026.artisancatalog.ui.components.OrientationGuideCube
import com.sih2026.artisancatalog.ui.theme.ArtisanGreen
import com.sih2026.artisancatalog.ui.theme.PureWhite
import com.sih2026.artisancatalog.ui.theme.TerracottaPrimary
import com.sih2026.artisancatalog.ui.viewmodel.ProductOrientation
import com.sih2026.artisancatalog.ui.viewmodel.ProductViewModel
import java.io.File
import java.util.concurrent.Executor

@Composable
fun GuidedCameraScreen(
    viewModel: ProductViewModel,
    onAllViewsCaptured: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val draftState by viewModel.draftState.collectAsState()
    val currentOrientation = draftState.currentOrientation

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var isCameraBound by remember { mutableStateOf(false) }

    // Retake / Review State for current photo
    var capturedPhotoPath by remember { mutableStateOf<String?>(null) }
    var isReviewingCurrentPhoto by remember { mutableStateOf(false) }

    val previewView = remember { PreviewView(context) }

    val orientationIndex = ProductOrientation.entries.indexOf(currentOrientation)

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val capture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    capture
                )

                imageCapture = capture
                isCameraBound = true
            } catch (_: Exception) {
                isCameraBound = false
            }
        }, ContextCompat.getMainExecutor(context))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (isCameraBound) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Fallback camera background simulation
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E1E1E)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Live Camera Preview (Ready)",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )
            }
        }

        // Center: Subtle 3D Wireframe Orientation Cube (Rotates smoothly to guide the artisan)
        if (!isReviewingCurrentPhoto) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp),
                contentAlignment = Alignment.Center
            ) {
                OrientationGuideCube(targetOrientation = currentOrientation)
            }
        } else {
            // Photo Review Modal (Retake vs Use Photo)
            capturedPhotoPath?.let { path ->
                AsyncImage(
                    model = path,
                    contentDescription = "Captured Orientation",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Top Control Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 18.dp, end = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = PureWhite,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Orientation Badge (e.g. "FRONT 1/6")
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(TerracottaPrimary)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${currentOrientation.displayName.uppercase()} (${orientationIndex + 1}/6)",
                        color = PureWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Sample photos shortcut for quick testing
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable {
                            viewModel.useSampleCraftPhotos()
                            onAllViewsCaptured()
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(text = "Auto Fill", color = PureWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Instruction Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.65f)),
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Text(
                    text = currentOrientation.instruction,
                    color = PureWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6 Progress Indicators: ● ○ ○ ○ ○ ○
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(6) { index ->
                    val isDone = index < orientationIndex || (index == orientationIndex && isReviewingCurrentPhoto)
                    val isCurrent = index == orientationIndex
                    val color = when {
                        isDone -> ArtisanGreen
                        isCurrent -> TerracottaPrimary
                        else -> Color.White.copy(alpha = 0.35f)
                    }
                    val size = if (isCurrent) 11.dp else 9.dp

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(size)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
        }

        // Bottom Controls
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp, start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (!isReviewingCurrentPhoto) {
                // Circular Shutter Trigger
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .border(4.dp, PureWhite, CircleShape)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .background(TerracottaPrimary)
                        .clickable {
                            captureOrientationPhoto(
                                context = context,
                                imageCapture = imageCapture,
                                executor = ContextCompat.getMainExecutor(context),
                                orientation = currentOrientation,
                                onCaptured = { path ->
                                    capturedPhotoPath = path
                                    isReviewingCurrentPhoto = true
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "📸", fontSize = 28.sp)
                }
            } else {
                // Review Actions: Retake vs Use Photo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            isReviewingCurrentPhoto = false
                            capturedPhotoPath = null
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PureWhite),
                        border = androidx.compose.foundation.BorderStroke(2.dp, PureWhite),
                        modifier = Modifier
                            .height(52.dp)
                            .weight(1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Retake", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            capturedPhotoPath?.let { path ->
                                viewModel.saveOrientationPhoto(currentOrientation, path)
                            }
                            val next = viewModel.nextOrientation()
                            if (next == null) {
                                // All 6 views captured!
                                onAllViewsCaptured()
                            } else {
                                isReviewingCurrentPhoto = false
                                capturedPhotoPath = null
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ArtisanGreen),
                        modifier = Modifier
                            .height(52.dp)
                            .weight(1f)
                    ) {
                        Text("Use Photo ✓", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PureWhite)
                    }
                }
            }
        }
    }
}

private fun captureOrientationPhoto(
    context: Context,
    imageCapture: ImageCapture?,
    executor: Executor,
    orientation: ProductOrientation,
    onCaptured: (String) -> Unit
) {
    val dir = File(context.filesDir, "product_views").apply { if (!exists()) mkdirs() }
    val photoFile = File(dir, "view_${orientation.name.lowercase()}_${System.currentTimeMillis()}.jpg")

    if (imageCapture != null) {
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    onCaptured(photoFile.absolutePath)
                }

                override fun onError(exception: ImageCaptureException) {
                    // Fallback to sample photo on capture failure
                    createFallbackPhoto(photoFile, orientation)
                    onCaptured(photoFile.absolutePath)
                }
            }
        )
    } else {
        createFallbackPhoto(photoFile, orientation)
        onCaptured(photoFile.absolutePath)
    }
}

private fun createFallbackPhoto(photoFile: File, orientation: ProductOrientation) {
    try {
        val bmp = android.graphics.Bitmap.createBitmap(600, 600, android.graphics.Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bmp)
        canvas.drawColor(android.graphics.Color.rgb(245, 239, 235))
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.rgb(192, 74, 21)
            textSize = 32f
            textAlign = android.graphics.Paint.Align.CENTER
        }
        canvas.drawText("${orientation.displayName} Sample", 300f, 300f, paint)
        java.io.FileOutputStream(photoFile).use { out ->
            bmp.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, out)
        }
    } catch (_: Exception) {}
}
