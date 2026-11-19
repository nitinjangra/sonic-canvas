package com.pixelpushers.sonixa.ui.components

import android.provider.Settings
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlin.math.min

private val Heights = floatArrayOf(58f, 30f, 58f, 116f, 60f, 30f, 54f, 116f, 60f)
private val Widths = floatArrayOf(8f, 8f, 8f, 11f, 8f, 8f, 8f, 11f, 8f)
private val Centers = floatArrayOf(5.5f, 23.5f, 42.5f, 61.5f, 80.5f, 99.5f, 118.5f, 138.5f, 155.5f)
private const val ArtWidth = 161f
private const val ArtHeight = 116f

@Composable
private fun barScale(index: Int, animate: Boolean): State<Float> {
    if (!animate) return remember { mutableStateOf(1f) }
    val transition = rememberInfiniteTransition(label = "waveform")
    return transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500 + (index % 2) * 100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(index * 120, StartOffsetType.FastForward),
        ),
        label = "bar$index",
    )
}

@Composable
fun WaveformIcon(
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    playing: Boolean = true,
) {
    val resolver = LocalContext.current.contentResolver
    val motionAllowed = remember {
        Settings.Global.getFloat(resolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) != 0f
    }
    val scales = List(Heights.size) { barScale(it, playing && motionAllowed) }

    Canvas(modifier) {
        val s = min(size.width / ArtWidth, size.height / ArtHeight)
        val left = (size.width - ArtWidth * s) / 2f
        val midY = size.height / 2f
        for (i in Heights.indices) {
            val h = Heights[i] * s * scales[i].value
            val w = Widths[i] * s
            drawRoundRect(
                color = color,
                topLeft = Offset(left + Centers[i] * s - w / 2f, midY - h / 2f),
                size = Size(w, h),
                cornerRadius = CornerRadius(w / 2f),
            )
        }
    }
}
