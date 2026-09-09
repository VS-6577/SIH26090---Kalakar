package com.sih2026.artisancatalog.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.sih2026.artisancatalog.ui.theme.TerracottaPrimary
import com.sih2026.artisancatalog.ui.viewmodel.ProductOrientation
import kotlin.math.cos
import kotlin.math.sin

data class Point3D(val x: Float, val y: Float, val z: Float)
data class Point2D(val x: Float, val y: Float)

@Composable
fun OrientationGuideCube(
    targetOrientation: ProductOrientation,
    modifier: Modifier = Modifier
) {
    // Determine target Euler angles for the orientation
    val (targetRotX, targetRotY) = when (targetOrientation) {
        ProductOrientation.FRONT -> Pair(-15f, 20f)
        ProductOrientation.RIGHT -> Pair(-15f, 110f)
        ProductOrientation.BACK -> Pair(-15f, 200f)
        ProductOrientation.LEFT -> Pair(-15f, 290f)
        ProductOrientation.TOP -> Pair(65f, 20f)
        ProductOrientation.BOTTOM -> Pair(-75f, 20f)
    }

    val animatedRotX by animateFloatAsState(
        targetValue = targetRotX,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "rotX"
    )
    val animatedRotY by animateFloatAsState(
        targetValue = targetRotY,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "rotY"
    )

    Box(
        modifier = modifier.size(190.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val cubeRadius = size.width * 0.28f

            // 8 Vertices of the cube
            val vertices3D = listOf(
                Point3D(-1f, -1f, -1f), // 0: Front Top Left
                Point3D(1f, -1f, -1f),  // 1: Front Top Right
                Point3D(1f, 1f, -1f),   // 2: Front Bottom Right
                Point3D(-1f, 1f, -1f),  // 3: Front Bottom Left
                Point3D(-1f, -1f, 1f),  // 4: Back Top Left
                Point3D(1f, -1f, 1f),   // 5: Back Top Right
                Point3D(1f, 1f, 1f),    // 6: Back Bottom Right
                Point3D(-1f, 1f, 1f)    // 7: Back Bottom Left
            )

            // Convert degrees to radians
            val radX = Math.toRadians(animatedRotX.toDouble()).toFloat()
            val radY = Math.toRadians(animatedRotY.toDouble()).toFloat()

            // 3D rotation and perspective projection
            val projectedPoints = vertices3D.map { p ->
                // Rotate around Y axis
                val cosY = cos(radY)
                val sinY = sin(radY)
                val x1 = p.x * cosY + p.z * sinY
                val y1 = p.y
                val z1 = -p.x * sinY + p.z * cosY

                // Rotate around X axis
                val cosX = cos(radX)
                val sinX = sin(radX)
                val x2 = x1
                val y2 = y1 * cosX - z1 * sinX
                val z2 = y1 * sinX + z1 * cosX

                // Perspective projection
                val distance = 3.2f
                val scale = distance / (distance + z2)
                Point2D(centerX + x2 * cubeRadius * scale, centerY + y2 * cubeRadius * scale)
            }

            // Define 6 faces (indices into vertices):
            // FRONT (0,1,2,3), RIGHT (1,5,6,2), BACK (5,4,7,6), LEFT (4,0,3,7), TOP (4,5,1,0), BOTTOM (3,2,6,7)
            val faces = listOf(
                Pair(ProductOrientation.FRONT, listOf(0, 1, 2, 3)),
                Pair(ProductOrientation.RIGHT, listOf(1, 5, 6, 2)),
                Pair(ProductOrientation.BACK, listOf(5, 4, 7, 6)),
                Pair(ProductOrientation.LEFT, listOf(4, 0, 3, 7)),
                Pair(ProductOrientation.TOP, listOf(4, 5, 1, 0)),
                Pair(ProductOrientation.BOTTOM, listOf(3, 2, 6, 7))
            )

            // Draw all faces with subtle transparent fills and highlight active face
            faces.forEach { (faceOrientation, vertexIndices) ->
                val isActive = faceOrientation == targetOrientation
                drawCubeFace(
                    projectedPoints = projectedPoints,
                    indices = vertexIndices,
                    isActive = isActive
                )
            }
        }
    }
}

private fun DrawScope.drawCubeFace(
    projectedPoints: List<Point2D>,
    indices: List<Int>,
    isActive: Boolean
) {
    val path = Path().apply {
        val p0 = projectedPoints[indices[0]]
        moveTo(p0.x, p0.y)
        for (i in 1 until indices.size) {
            val p = projectedPoints[indices[i]]
            lineTo(p.x, p.y)
        }
        close()
    }

    // Subtle translucent fill
    val fillColor = if (isActive) TerracottaPrimary.copy(alpha = 0.28f) else Color.White.copy(alpha = 0.05f)
    drawPath(path, color = fillColor)

    // Clear outline
    val strokeColor = if (isActive) TerracottaPrimary else Color.White.copy(alpha = 0.45f)
    val strokeWidth = if (isActive) 3.5.dp.toPx() else 1.5.dp.toPx()
    drawPath(path, color = strokeColor, style = Stroke(width = strokeWidth))
}
