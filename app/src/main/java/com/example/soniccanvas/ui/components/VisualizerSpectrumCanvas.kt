package com.example.soniccanvas.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun VisualizerSpectrumCanvas(
    waveform: ByteArray?,
    modifier: Modifier = Modifier,
    color: Color = Color.Cyan
) {
    Canvas(modifier = modifier) {
        if (waveform == null || waveform.isEmpty()) return@Canvas
        val width = size.width
        val height = size.height
        val barCount = 32
        val step = waveform.size / barCount
        val barWidth = width / barCount * 0.7f

        for (i in 0 until barCount) {
            val byteVal = waveform[i * step].toInt() and 0xFF
            val percent = byteVal / 128f
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
