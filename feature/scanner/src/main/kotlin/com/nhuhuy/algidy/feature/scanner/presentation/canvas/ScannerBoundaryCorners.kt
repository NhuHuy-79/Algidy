package com.nhuhuy.algidy.feature.scanner.presentation.canvas

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp


@Composable
fun ScannerBoundaryCorner(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    cornerSpacing: Dp,
    cornerCap: Dp,
    cornerRadius: Dp,
    scanHeight: Dp,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Laser")
    val progression by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laserPos"
    )
    Canvas(
        modifier = modifier
    ) {
        val roundRectWidth = size.width - cornerCap.toPx() * 1.5f
        val roundRectHeight = size.height - cornerCap.toPx() * 1.5f
        val totalCornerWidth = (roundRectWidth - cornerSpacing.toPx()) / 2
        val totalCornerHeight = (roundRectHeight - cornerSpacing.toPx()) / 2

        val startX = (size.width - roundRectWidth) / 2
        val startY = (size.height - roundRectHeight) / 2
        val endX = startX + roundRectWidth
        val endY = startY + roundRectHeight


        val laserStartX = startX + cornerCap.toPx() * 0.5f
        val laserEndX = endX - cornerCap.toPx() * 0.5f
        val laserStartY = startY + cornerCap.toPx() + cornerRadius.toPx()
        val laserEndY = endY - (cornerCap.toPx() + cornerRadius.toPx())
        val currentLaserY = laserStartY + (laserEndY - laserStartY) * progression


        drawScanCorners(
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY,
            totalWidth = totalCornerWidth,
            totalHeight = totalCornerHeight,
            radius = cornerRadius.toPx(),
            cornerCap = cornerCap.toPx(),
            color = containerColor
        )

        drawScanningGround(
            startX = laserStartX,
            endX = laserEndX,
            currentY = currentLaserY,
            laserColor = contentColor,
            scanHeight = scanHeight.toPx()
        )
    }
}

// HÀM VẼ CORNER - GIỮ NGUYÊN LOGIC PATH CỦA BẠN
fun DrawScope.drawScanCorners(
    startX: Float, startY: Float, endX: Float, endY: Float,
    totalWidth: Float, totalHeight: Float,
    radius: Float, cornerCap: Float, color: Color
) {
    val cornerPath = Path().apply {
        // Top Start (Top-Left)
        moveTo(x = startX + totalWidth, y = startY)
        lineTo(x = startX + radius, y = startY)
        arcTo(Rect(startX, startY, startX + radius * 2, startY + radius * 2), 270f, -90f, false)
        lineTo(x = startX, y = startY + totalHeight)

        // Bottom Start (Bottom-Left)
        moveTo(x = startX, y = endY - totalHeight)
        lineTo(x = startX, y = endY - radius)
        arcTo(Rect(startX, endY - radius * 2, startX + radius * 2, endY), 180f, -90f, false)
        lineTo(x = startX + totalWidth, y = endY)

        // Top End (Top-Right)
        moveTo(x = endX - totalWidth, y = startY)
        lineTo(x = endX - radius, y = startY)
        arcTo(Rect(endX - radius * 2, startY, endX, startY + radius * 2), 270f, 90f, false)
        lineTo(x = endX, y = startY + totalHeight)

        // Bottom End (Bottom-Right)
        moveTo(x = endX - totalWidth, y = endY)
        lineTo(x = endX - radius, y = endY)
        arcTo(Rect(endX - radius * 2, endY - radius * 2, endX, endY), 90f, -90f, false)
        lineTo(x = endX, y = endY - totalHeight)
    }

    drawPath(
        path = cornerPath,
        color = color,
        style = Stroke(width = cornerCap, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}

fun DrawScope.drawScanningGround(
    startX: Float,
    endX: Float,
    currentY: Float,
    scanHeight: Float,
    laserColor: Color
) {

    // Vẽ vùng Gradient tỏa sáng (Glow)
    // Chỉnh sửa: Để mượt hơn, Gradient nên bám theo currentY
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                laserColor.copy(alpha = 0.3f),
                Color.Transparent
            ),
            startY = currentY - scanHeight / 2,
            endY = currentY + scanHeight / 2
        ),
        topLeft = Offset(startX, currentY - scanHeight / 2),
        size = Size(endX - startX, scanHeight),
        blendMode = BlendMode.Screen
    )
}
