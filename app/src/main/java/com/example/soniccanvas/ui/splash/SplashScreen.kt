package com.example.soniccanvas.ui.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F1115)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(220.dp),
            contentAlignment = Alignment.Center
        ) {
            // Ring 1 (staggered delay 0s)
            val ring1Transition = rememberInfiniteTransition(label = "ring1")
            val ring1Scale by ring1Transition.animateFloat(
                initialValue = 0.8f,
                targetValue = 1.6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "ring1Scale"
            )
            val ring1Alpha by ring1Transition.animateFloat(
                initialValue = 0.7f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "ring1Alpha"
            )

            Box(
                modifier = Modifier
                    .size(144.dp)
                    .graphicsLayer {
                        scaleX = ring1Scale
                        scaleY = ring1Scale
                        alpha = ring1Alpha
                    }
                    .border(2.dp, Color(0xFF5BE0B3), CircleShape)
            )

            // Ring 2 (staggered delay 1s)
            val ring2Transition = rememberInfiniteTransition(label = "ring2")
            val ring2Scale by ring2Transition.animateFloat(
                initialValue = 0.8f,
                targetValue = 1.6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    initialStartOffset = StartOffset(1000),
                    repeatMode = RepeatMode.Restart
                ),
                label = "ring2Scale"
            )
            val ring2Alpha by ring2Transition.animateFloat(
                initialValue = 0.7f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    initialStartOffset = StartOffset(1000),
                    repeatMode = RepeatMode.Restart
                ),
                label = "ring2Alpha"
            )

            Box(
                modifier = Modifier
                    .size(144.dp)
                    .graphicsLayer {
                        scaleX = ring2Scale
                        scaleY = ring2Scale
                        alpha = ring2Alpha
                    }
                    .border(2.dp, Color(0xFF5BE0B3), CircleShape)
            )

            // Center App Icon Card (144dp x 144dp, mint-to-teal gradient, rounded 40dp)
            Box(
                modifier = Modifier
                    .size(144.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF5BE0B3), Color(0xFF4FB3C8))
                        )
                    )
                    .padding(bottom = 36.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                // 5 Animated Equalizer Bars in dark color #0F1115
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    val barHeights = listOf(34.dp, 64.dp, 90.dp, 50.dp, 72.dp)
                    val delays = listOf(0, 100, 200, 300, 400)

                    barHeights.forEachIndexed { index, height ->
                        val eqTransition = rememberInfiniteTransition(label = "eq_$index")
                        val scaleYVal by eqTransition.animateFloat(
                            initialValue = 0.35f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(
                                    durationMillis = 700,
                                    delayMillis = delays[index],
                                    easing = FastOutSlowInEasing
                                ),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "eqScale_$index"
                        )

                        Box(
                            modifier = Modifier
                                .width(12.dp)
                                .height(height)
                                .graphicsLayer {
                                    scaleY = scaleYVal
                                    transformOrigin = TransformOrigin(0.5f, 1f)
                                }
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF0F1115))
                        )
                    }
                }
            }
        }
    }
}
