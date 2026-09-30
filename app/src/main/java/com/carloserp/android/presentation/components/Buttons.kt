package com.carloserp.android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carloserp.android.presentation.theme.CarlosErpTheme

/**
 * Reusable action buttons (task 50.2).
 *
 * [PrimaryButton] is the filled call-to-action with a built-in [loading] state
 * that swaps the label for a spinner and blocks re-entry; [SecondaryButton] is
 * the outlined, lower-emphasis variant. Both stretch to full width by default so
 * forms line up, and read their colours from the (tenant-branded) theme.
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier.fillMaxWidth(),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.height(BUTTON_SPINNER_SIZE),
                strokeWidth = 2.dp,
            )
        } else {
            Text(text)
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(text)
    }
}

private val BUTTON_SPINNER_SIZE = 20.dp

@Preview(showBackground = true)
@Composable
private fun ButtonsPreview() {
    CarlosErpTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PrimaryButton(text = "Guardar", onClick = {}, modifier = Modifier.width(140.dp))
            Spacer(Modifier.width(8.dp))
            SecondaryButton(text = "Cancelar", onClick = {}, modifier = Modifier.width(140.dp))
        }
    }
}
