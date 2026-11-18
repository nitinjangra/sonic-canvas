package com.example.soniccanvas.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun LyricsIcon(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    isActive: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "lyrics_bars_animation")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "phase"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val strokeWidth = width * 0.09f

        // 3 Horizontal lines on the left (animating widths if isActive)
        val maxLineLength = width * 0.42f
        val startX = width * 0.08f
        val y1 = height * 0.22f
        val y2 = height * 0.50f
        val y3 = height * 0.78f

        val len1 = if (isActive) maxLineLength * (0.6f + 0.4f * kotlin.math.sin((phase * 2 * Math.PI).toDouble()).toFloat()) else maxLineLength
        val len2 = if (isActive) maxLineLength * (0.5f + 0.5f * kotlin.math.cos((phase * 2 * Math.PI).toDouble()).toFloat()) else maxLineLength * 0.8f
        val len3 = if (isActive) maxLineLength * (0.4f + 0.6f * kotlin.math.sin(((phase + 0.5f) * 2 * Math.PI).toDouble()).toFloat()) else maxLineLength * 0.6f

        drawLine(
            color = tint,
            start = Offset(startX, y1),
            end = Offset(startX + len1, y1),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(startX, y2),
            end = Offset(startX + len2, y2),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = Offset(startX, y3),
            end = Offset(startX + len3, y3),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Musical note on the right
        val noteHeadRadius = width * 0.15f
        val noteHeadCenter = Offset(width * 0.62f, height * 0.72f)
        drawCircle(
            color = tint,
            radius = noteHeadRadius,
            center = noteHeadCenter,
            style = Stroke(width = strokeWidth)
        )

        val stemStartX = noteHeadCenter.x + noteHeadRadius * 0.7f
        val stemStartY = noteHeadCenter.y - noteHeadRadius * 0.2f
        val stemEndY = height * 0.18f
        val stemEndX = stemStartX

        drawLine(
            color = tint,
            start = Offset(stemStartX, stemStartY),
            end = Offset(stemEndX, stemEndY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        val flagPath = Path().apply {
            moveTo(stemEndX, stemEndY)
            cubicTo(
                stemEndX + width * 0.18f, stemEndY,
                stemEndX + width * 0.28f, stemEndY + height * 0.12f,
                stemEndX + width * 0.26f, stemEndY + height * 0.32f
            )
        }
        drawPath(
            path = flagPath,
            color = tint,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}
