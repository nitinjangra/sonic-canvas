package com.pixelpushers.sonixa.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class MainTab(
    val title: String,
    val icon: ImageVector,
    val accessibilityName: String
) {
    LIBRARY("Library", Icons.Default.Book, "Music Library"),
    SEARCH("Search", Icons.Default.Search, "Search Music"),
    PLAYLISTS("Playlists", Icons.AutoMirrored.Filled.QueueMusic, "Playlists"),
    SETTINGS("Settings", Icons.Default.Settings, "Settings")
}

@Composable
fun GlassNavigationBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    // Reduced width by ~15px (220.dp -> 204.dp) for neat rounded corner alignment
    GlassPill(
        modifier = modifier
            .width(204.dp)
            .height(56.dp),
        cornerRadius = 28.dp
    ) {
        Row(
            modifier = Modifier
                .width(204.dp)
                .height(56.dp)
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainTab.entries.forEach { tab ->
                val isSelected = selectedTab == tab

                val indicatorColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    label = "tab_indicator"
                )

                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) Color.Black else MaterialTheme.colorScheme.primary,
                    label = "tab_icon"
                )

                val tabScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.15f else 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "tab_scale"
                )

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .graphicsLayer {
                            scaleX = tabScale
                            scaleY = tabScale
                        }
                        .clip(CircleShape)
                        .background(indicatorColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTabSelected(tab) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.accessibilityName,
                        tint = iconColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
