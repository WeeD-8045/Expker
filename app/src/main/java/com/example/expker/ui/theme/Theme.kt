package com.example.expker.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AccentGreen,
    secondary = AccentBlue,
    tertiary = AccentPurple,
    background = BackgroundDeepNavy,
    surface = SurfaceDarkBlue,
    onPrimary = BackgroundDeepNavy,
    onSecondary = BackgroundDeepNavy,
    onTertiary = BackgroundDeepNavy,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = CardBackground,
    onSurfaceVariant = TextSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = AccentGreen,
    secondary = AccentBlue,
    tertiary = AccentPurple,
    background = Color(0xFFF8FAFC),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF64748B)
)

@Composable
fun ExpkerTheme(
    themeMode: Int = 0, // 0: System, 1: Light, 2: Dark
    accentColor: Long = 0xFF4ADE80L,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        1 -> false
        2 -> true
        else -> isSystemInDarkTheme()
    }

    val baseColorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val colorScheme = baseColorScheme.copy(
        primary = Color(accentColor)
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
