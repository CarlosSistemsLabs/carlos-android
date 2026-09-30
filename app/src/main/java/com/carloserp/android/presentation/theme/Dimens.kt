package com.carloserp.android.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacing design tokens (task 50.2).
 *
 * A single 4dp-based scale used for padding/gaps across the app so screens
 * reference named steps (`spacing.md`) instead of hard-coding dp values. Exposed
 * through [LocalSpacing] / [CarlosTheme.spacing] so it can travel with the theme
 * and be overridden in previews/tests.
 */
data class Spacing(
    val none: Dp = 0.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 48.dp,
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
