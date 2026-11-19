package com.pixelpushers.sonixa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class DefaultThumbnailStyle(val title: String, val colors: List<Color>) {
    MINT_GLOW("Mint Glow", listOf(Color(0xFF5BE0B3), Color(0xFF0B7A5A))),
    SUNSET_AMBER("Sunset Amber", listOf(Color(0xFFFFA751), Color(0xFFFFE259))),
    LAVENDER_PURPLE("Lavender Purple", listOf(Color(0xFF8A2387), Color(0xFFE94057))),
    MINT_TEAL("Mint Teal", listOf(Color(0xFF11998E), Color(0xFF38EF7D))),
    NEON_VIOLET("Neon Violet", listOf(Color(0xFF7F00FF), Color(0xFFE100FF))),
    OCEAN_BLUE("Ocean Blue", listOf(Color(0xFF2193B0), Color(0xFF6DD5ED))),
    CORAL_ROSE("Coral Rose", listOf(Color(0xFFEC6F66), Color(0xFFF3A183)))
}

@Composable
fun DefaultAlbumArtPlaceholder(
    styleIndex: Int,
    modifier: Modifier = Modifier,
    initial: String? = null,
    iconSize: Dp = 32.dp
) {
    val style = DefaultThumbnailStyle.entries.getOrElse(styleIndex) { DefaultThumbnailStyle.MINT_TEAL }
    val brush = Brush.linearGradient(colors = style.colors)

    Box(
        modifier = modifier.background(brush),
        contentAlignment = Alignment.Center
    ) {
        // Inner Glossy Sheen Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )
        )

        if (!initial.isNullOrBlank()) {
            val char = initial.trim().first().uppercaseChar().toString()
            Box(
                modifier = Modifier
                    .size(iconSize * 1.5f)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .border(1.dp, Color.White.copy(alpha = 0.35f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = char,
                    fontSize = (iconSize.value * 0.75f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = style.title,
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
