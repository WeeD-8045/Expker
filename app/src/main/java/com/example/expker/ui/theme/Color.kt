package com.example.expker.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Colors
val BackgroundDeepNavy = Color(0xFF0F111A)
val SurfaceDarkBlue = Color(0xFF1B1E2B)
val CardBackground = Color(0xFF24283B)

// Accents
val AccentYellowGradientStart = Color(0xFFF1F589)
val AccentYellowGradientEnd = Color(0xFFC9CF2C)
val AccentGreen = Color(0xFF4ADE80)
val AccentRed = Color(0xFFF87171)
val AccentBlue = Color(0xFF60A5FA)
val AccentPurple = Color(0xFFA78BFA)
val AccentOrange = Color(0xFFFB923C)

// Gradients
val MainCardGradient = Brush.verticalGradient(
    colors = listOf(AccentYellowGradientStart, AccentYellowGradientEnd)
)

val IncomeGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF4ADE80), Color(0xFF22C55E))
)

val ExpenseGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFF87171), Color(0xFFEF4444))
)

// Text Colors
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFF94A3B8)
val TextTertiary = Color(0xFF64748B)

// Chart Colors
val ChartBlue = Color(0xFF3B82F6)
val ChartPurple = Color(0xFF8B5CF6)
val ChartOrange = Color(0xFFF59E0B)
val ChartPink = Color(0xFFEC4899)
val ChartCyan = Color(0xFF06B6D4)
