package com.carloserp.android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.carloserp.android.presentation.theme.CarlosErpTheme
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * Reusable full-screen state placeholders (task 50.2).
 *
 * [LoadingView], [ErrorView] (with a retry action), and [EmptyView] give every
 * screen a consistent look for the three non-content states, so feature screens
 * just branch on their UI state. The richer retry/error handling wiring lands in
 * task 50.5; these are the shared visuals it builds on.
 */
@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
) {
    CenteredMessage(
        title = "Algo salió mal",
        message = message,
        modifier = modifier,
        actionText = if (onRetry != null) "Reintentar" else null,
        onAction = onRetry,
    )
}

@Composable
fun EmptyView(
    message: String,
    modifier: Modifier = Modifier,
    title: String = "Nada por aquí",
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    CenteredMessage(
        title = title,
        message = message,
        modifier = modifier,
        actionText = actionText,
        onAction = onAction,
    )
}

@Composable
private fun CenteredMessage(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(CarlosTheme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(CarlosTheme.spacing.sm))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        if (actionText != null && onAction != null) {
            Spacer(Modifier.height(CarlosTheme.spacing.lg))
            PrimaryButton(text = actionText, onClick = onAction)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorViewPreview() {
    CarlosErpTheme {
        ErrorView(message = "No pudimos cargar los datos. Revisá tu conexión.", onRetry = {})
    }
}
