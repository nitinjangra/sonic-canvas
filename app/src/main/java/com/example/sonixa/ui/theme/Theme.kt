package com.example.sonixa.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.sonixa.data.preferences.AppTheme

private val DarkColorScheme = darkColorScheme(
    primary = AccentMintDark,
    onPrimary = Color.Black,
    background = BackgroundDark,
    onBackground = TextDark,
    surface = SurfaceDark,
    onSurface = TextDark,
    surfaceVariant = LineDark,
    onSurfaceVariant = MutedTextDark
)

private val LightColorScheme = lightColorScheme(
    primary = AccentMintLight,
    onPrimary = Color.White,
    background = BackgroundLight,
    onBackground = TextLight,
    surface = SurfaceLight,
    onSurface = TextLight,
    surfaceVariant = LineLight,
    onSurfaceVariant = MutedTextLight
)

private val BlackColorScheme = darkColorScheme(
    primary = AccentMintDark,
    onPrimary = Color.Black,
    background = BackgroundBlack,
    onBackground = TextBlack,
    surface = SurfaceBlack,
    onSurface = TextBlack,
    surfaceVariant = LineBlack,
    onSurfaceVariant = MutedTextBlack
)

@Composable
fun SonixaTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    dynamicAccentColor: Color? = null,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()

    val baseColorScheme = when (appTheme) {
        AppTheme.SYSTEM -> if (isSystemDark) DarkColorScheme else LightColorScheme
        AppTheme.LIGHT -> LightColorScheme
        AppTheme.DARK -> DarkColorScheme
        AppTheme.BLACK -> BlackColorScheme
    }

    val finalColorScheme = if (dynamicAccentColor != null) {
        baseColorScheme.copy(primary = dynamicAccentColor)
    } else {
        baseColorScheme
    }

    MaterialTheme(
        colorScheme = finalColorScheme,
        typography = Typography,
        content = content
    )
}
