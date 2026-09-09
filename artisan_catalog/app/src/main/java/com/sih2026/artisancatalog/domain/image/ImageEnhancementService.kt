package com.sih2026.artisancatalog.domain.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min

class ImageEnhancementService(
    private val context: Context,
    private val backgroundRemovalEngine: BackgroundRemovalEngine = LocalStudioBackgroundEngine()
) : PhotoProcessor {

    override suspend fun enhanceToStudioQuality(
        inputImageFile: File,
        outputStudioFile: File
    ): File = withContext(Dispatchers.IO) {
        if (!inputImageFile.exists()) {
            return@withContext inputImageFile
        }

        val originalBitmap = BitmapFactory.decodeFile(inputImageFile.absolutePath)
            ?: return@withContext inputImageFile

        // 1. Adjust Brightness & Contrast (Studio Lighting)
        val studioLighted = adjustBrightnessAndContrast(
            source = originalBitmap,
            contrast = 1.15f,  // +15% crisp contrast
            brightness = 10f   // Gentle highlight
        )

        // 2. Apply Studio Background Treatment
        val processedStudioBitmap = backgroundRemovalEngine.removeBackground(studioLighted)

        // 3. Compress and Save locally
        FileOutputStream(outputStudioFile).use { out ->
            processedStudioBitmap.compress(Bitmap.CompressFormat.JPEG, 92, out)
        }

        outputStudioFile
    }

    override suspend fun generateThumbnail(
        inputFile: File,
        outputThumbFile: File,
        maxSize: Int
    ): File = withContext(Dispatchers.IO) {
        if (!inputFile.exists()) return@withContext inputFile

        val bitmap = BitmapFactory.decodeFile(inputFile.absolutePath) ?: return@withContext inputFile
        val width = bitmap.width
        val height = bitmap.height

        val scale = min(maxSize.toFloat() / width, maxSize.toFloat() / height)
        val targetW = (width * scale).toInt()
        val targetH = (height * scale).toInt()

        val scaled = Bitmap.createScaledBitmap(bitmap, targetW, targetH, true)
        FileOutputStream(outputThumbFile).use { out ->
            scaled.compress(Bitmap.CompressFormat.JPEG, 85, out)
        }

        outputThumbFile
    }

    /**
     * Creates a high-quality sample bitmap locally if physical camera is unavailable
     * (e.g. for testing in emulator or demo mode).
     */
    suspend fun createSampleCraftPhoto(sampleName: String, outputFile: File): File = withContext(Dispatchers.IO) {
        val width = 800
        val height = 800
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Warm artisan background
        val bgPaint = Paint().apply {
            color = android.graphics.Color.rgb(245, 239, 235)
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Terracotta decorative border
        val borderPaint = Paint().apply {
            color = android.graphics.Color.rgb(192, 86, 33)
            style = Paint.Style.STROKE
            strokeWidth = 16f
        }
        canvas.drawRoundRect(20f, 20f, width - 20f, height - 20f, 32f, 32f, borderPaint)

        // Center craft motif circle
        val motifPaint = Paint().apply {
            color = android.graphics.Color.rgb(192, 86, 33)
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawCircle(400f, 360f, 180f, motifPaint)

        // Text banner
        val textPaint = Paint().apply {
            color = android.graphics.Color.rgb(255, 255, 255)
            textSize = 36f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
        canvas.drawText("AUTHENTIC CRAFT", 400f, 350f, textPaint)

        val subTextPaint = Paint().apply {
            color = android.graphics.Color.rgb(44, 32, 24)
            textSize = 28f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.drawText(sampleName, 400f, 620f, subTextPaint)
        canvas.drawText("Studio Captured • 100% Handmade", 400f, 670f, subTextPaint)

        FileOutputStream(outputFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        outputFile
    }

    private fun adjustBrightnessAndContrast(
        source: Bitmap,
        contrast: Float,
        brightness: Float
    ): Bitmap {
        val result = Bitmap.createBitmap(source.width, source.height, source.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint()

        // ColorMatrix: [ a, b, c, d, e ]
        // newColor = a*R + b*G + c*B + d*A + e
        val cm = ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, brightness,
                0f, contrast, 0f, 0f, brightness,
                0f, 0f, contrast, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
        )
        paint.colorFilter = ColorMatrixColorFilter(cm)
        canvas.drawBitmap(source, 0f, 0f, paint)
        return result
    }
}
