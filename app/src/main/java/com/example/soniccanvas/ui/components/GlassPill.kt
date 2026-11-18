package com.example.soniccanvas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassPill(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 32.dp,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val isDark = MaterialTheme.colorScheme.background.red < 0.5f

    val glassBackgroundBrush = if (isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0x35FFFFFF),
                Color(0x18FFFFFF),
                Color(0xC011141B)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xF0FFFFFF),
                Color(0xDDF0F4F8)
            )
        )
    }

    val glassBorderBrush = if (isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0x77FFFFFF),
                Color(0x22FFFFFF),
                Color(0x0EFFFFFF)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0x66000000),
                Color(0x1A000000)
            )
        )
    }

    val glowSpotColor = MaterialTheme.colorScheme.primary.copy(alpha = if (isDark) 0.3f else 0.15f)

    Box(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = shape,
                clip = false,
                ambientColor = Color.Black.copy(alpha = 0.5f),
                spotColor = glowSpotColor
            )
            .clip(shape)
            .background(glassBackgroundBrush)
            .border(width = 1.dp, brush = glassBorderBrush, shape = shape)
    ) {
        // Inner Glossy Highlight Shine Layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.15f else 0.3f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = 80f
                    )
                )
        )
        content()
    }
}
