package com.carloserp.android.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * Thin banner shown while the device is offline (task 50.5).
 *
 * Animated in/out based on [visible]; uses the error container colors so it
 * reads as a passive warning without blocking the content below.
 */
@Composable
fun OfflineBanner(visible: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(visible = visible, modifier = modifier) {
        Text(
            text = "Sin conexión. Mostrando datos guardados.",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(vertical = CarlosTheme.spacing.sm, horizontal = CarlosTheme.spacing.md),
        )
    }
}
