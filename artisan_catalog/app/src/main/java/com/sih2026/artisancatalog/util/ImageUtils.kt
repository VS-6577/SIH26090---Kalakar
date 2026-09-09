package com.sih2026.artisancatalog.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.max

object ImageUtils {

    /**
     * Reads an image file from disk, safely downscales it to fit within [maxDimension] x [maxDimension],
     * compresses it as JPEG (quality 85%), and returns a valid Base64 Data URL.
     * Format: "data:image/jpeg;base64,..."
     */
    fun fileToBase64DataUrl(
        file: File,
        maxDimension: Int = 1024,
        quality: Int = 85
    ): String? {
        if (!file.exists() || !file.canRead()) return null

        try {
            // Step 1: Decode image bounds only to compute sample size
            val boundsOptions = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(file.absolutePath, boundsOptions)

            val originalWidth = boundsOptions.outWidth
            val originalHeight = boundsOptions.outHeight
            if (originalWidth <= 0 || originalHeight <= 0) return null

            // Step 2: Calculate inSampleSize to avoid loading full resolution into memory
            var sampleSize = 1
            val maxEdge = max(originalWidth, originalHeight)
            while ((maxEdge / sampleSize) > (maxDimension * 1.5)) {
                sampleSize *= 2
            }

            // Step 3: Decode sampled bitmap
            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
            val sampledBitmap = BitmapFactory.decodeFile(file.absolutePath, decodeOptions) ?: return null

            // Step 4: Fine scale to exact target bounds if still larger than maxDimension
            val currentMax = max(sampledBitmap.width, sampledBitmap.height)
            val finalBitmap = if (currentMax > maxDimension) {
                val scale = maxDimension.toFloat() / currentMax
                val targetW = (sampledBitmap.width * scale).toInt()
                val targetH = (sampledBitmap.height * scale).toInt()
                val scaled = Bitmap.createScaledBitmap(sampledBitmap, targetW, targetH, true)
                if (scaled != sampledBitmap) {
                    sampledBitmap.recycle()
                }
                scaled
            } else {
                sampledBitmap
            }

            // Step 5: Compress to JPEG ByteArray
            val outputStream = ByteArrayOutputStream()
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            val imageBytes = outputStream.toByteArray()
            finalBitmap.recycle()

            // Step 6: Encode to Base64 Data URL (NO_WRAP prevents newline insertions)
            val base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
            return "data:image/jpeg;base64,$base64String"

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Helper overload accepting a file path string.
     */
    fun pathToBase64DataUrl(
        filePath: String?,
        maxDimension: Int = 1024,
        quality: Int = 85
    ): String? {
        if (filePath.isNullOrBlank()) return null
        return fileToBase64DataUrl(File(filePath), maxDimension, quality)
    }
}
