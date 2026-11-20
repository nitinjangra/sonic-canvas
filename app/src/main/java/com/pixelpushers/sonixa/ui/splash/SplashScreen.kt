package com.pixelpushers.sonixa.ui.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pixelpushers.sonixa.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(2200)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F1115)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(240.dp),
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
                    .size(160.dp)
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
                    .size(160.dp)
                    .graphicsLayer {
                        scaleX = ring2Scale
                        scaleY = ring2Scale
                        alpha = ring2Alpha
                    }
                    .border(2.dp, Color(0xFF5BE0B3), CircleShape)
            )

            // Center New Sonixa Logo Image with smooth breathing scale animation
            val logoTransition = rememberInfiniteTransition(label = "logo_pop")
            val logoScale by logoTransition.animateFloat(
                initialValue = 0.9f,
                targetValue = 1.05f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "logoScale"
            )

            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Sonixa Logo",
                modifier = Modifier
                    .size(130.dp)
                    .graphicsLayer {
                        scaleX = logoScale
                        scaleY = logoScale
                    }
            )
        }
    }
}
