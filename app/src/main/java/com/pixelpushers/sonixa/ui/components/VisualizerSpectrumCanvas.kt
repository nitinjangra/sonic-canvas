package com.pixelpushers.sonixa.ui.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import kotlin.math.sin

@Composable
fun VisualizerSpectrumCanvas(
    waveform: ByteArray?,
    modifier: Modifier = Modifier,
    color: Color = Color.Cyan
) {
    val infiniteTransition = rememberInfiniteTransition(label = "visualizer")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.2831855f, // 2 * PI
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val barCount = 36
        val barWidth = width / barCount * 0.7f

        for (i in 0 until barCount) {
            val percent = if (waveform != null && waveform.isNotEmpty()) {
                val step = waveform.size / barCount
                val byteVal = waveform[(i * step).coerceIn(0, waveform.size - 1)].toInt() and 0xFF
                (byteVal / 128f).coerceIn(0.1f, 1f)
            } else {
                // Animated dynamic wave when audio waveform is not attached
                val wave = sin(phase + i * 0.3f) * 0.4f + 0.5f
                wave.coerceIn(0.15f, 0.95f)
            }

            val barHeight = (height * percent).coerceIn(4f, height)
            val x = i * (width / barCount) + barWidth / 2f
            val yStart = height / 2f - barHeight / 2f
            val yEnd = height / 2f + barHeight / 2f

            drawLine(
                color = color,
                start = Offset(x, yStart),
                end = Offset(x, yEnd),
                strokeWidth = barWidth,
                cap = StrokeCap.Round
            )
        }
    }
}
