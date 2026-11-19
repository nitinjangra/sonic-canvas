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

    // Enhanced multi-stop translucent glass gradient fill
    val glassBackgroundBrush = if (isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0x45FFFFFF), // Brighter frosted top highlight
                Color(0x1AFFFFFF), // Mid translucent body
                Color(0xD00F1219)  // Deep frosted base
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xF5FFFFFF),
                Color(0xE0F0F4F8)
            )
        )
    }

    // Specular border gradient (bright top rim fading down)
    val glassBorderBrush = if (isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xAAFFFFFF),
                Color(0x33FFFFFF),
                Color(0x11FFFFFF)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0x88000000),
                Color(0x22000000)
            )
        )
    }

    val glowSpotColor = MaterialTheme.colorScheme.primary.copy(alpha = if (isDark) 0.4f else 0.2f)

    Box(
        modifier = modifier
            .shadow(
                elevation = 22.dp,
                shape = shape,
                clip = false,
                ambientColor = Color.Black.copy(alpha = 0.6f),
                spotColor = glowSpotColor
            )
            .clip(shape)
            .background(glassBackgroundBrush)
            .border(width = 1.25.dp, brush = glassBorderBrush, shape = shape)
    ) {
        // Top Specular Reflection Shine Layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isDark) 0.22f else 0.4f),
                            Color.White.copy(alpha = if (isDark) 0.04f else 0.1f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = 100f
                    )
                )
        )
        content()
    }
}
