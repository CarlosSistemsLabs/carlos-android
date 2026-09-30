package com.carloserp.android.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Corner-radius design tokens (task 50.2).
 *
 * The Material 3 [Shapes] scale the theme applies, so components pick their
 * corner radius from `MaterialTheme.shapes` (e.g. cards use `medium`, buttons
 * `small`) instead of hard-coding shapes.
 */
val CarlosShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp),
)
