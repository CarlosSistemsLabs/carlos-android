package com.carloserp.android.presentation.appearance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.R
import com.carloserp.android.presentation.theme.CarlosTheme

private val BACKGROUND_SWATCHES = listOf(
    Color(0xFFF8FAFC), Color(0xFFFFFFFF), Color(0xFFFFF7ED), Color(0xFFF0FDF4),
    Color(0xFFEFF6FF), Color(0xFFFDF4FF), Color(0xFF0F172A), Color(0xFF1E293B),
)
private val HEADER_TEXT_SWATCHES = listOf(
    Color(0xFFFFFFFF), Color(0xFF0F172A), Color(0xFF1E40AF), Color(0xFF0EA5E9),
    Color(0xFF16A34A), Color(0xFFDC2626), Color(0xFFF59E0B), Color(0xFF7C3AED),
)
private val NAV_BAR_SWATCHES = listOf(
    Color(0xFFFFFFFF), Color(0xFFF1F5F9), Color(0xFF1E40AF), Color(0xFF0EA5E9),
    Color(0xFF16A34A), Color(0xFF0F172A), Color(0xFF7C3AED), Color(0xFFF59E0B),
)

/**
 * Appearance settings (theme customization): lets the user pick the screen
 * background, the header text color and the bottom bar color from swatches, or
 * reset to defaults. Changes apply immediately via [ThemeViewModel].
 */
@Composable
fun AppearanceScreen(
    modifier: Modifier = Modifier,
    viewModel: ThemeViewModel = hiltViewModel(),
) {
    val prefs by viewModel.prefs.collectAsStateWithLifecycle()
    val spacing = CarlosTheme.spacing

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.md),
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        SwatchSection(
            title = stringResource(R.string.appearance_background),
            swatches = BACKGROUND_SWATCHES,
            selected = prefs.backgroundColor,
            onPick = { viewModel.setBackgroundColor(it) },
        )
        SwatchSection(
            title = stringResource(R.string.appearance_header_text),
            swatches = HEADER_TEXT_SWATCHES,
            selected = prefs.headerTextColor,
            onPick = { viewModel.setHeaderTextColor(it) },
        )
        SwatchSection(
            title = stringResource(R.string.appearance_navbar),
            swatches = NAV_BAR_SWATCHES,
            selected = prefs.navBarColor,
            onPick = { viewModel.setNavBarColor(it) },
        )

        OutlinedButton(onClick = viewModel::reset) {
            Text(stringResource(R.string.appearance_reset))
        }
    }
}

@Composable
private fun SwatchSection(
    title: String,
    swatches: List<Color>,
    selected: Int?,
    onPick: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(CarlosTheme.spacing.sm)) {
        Text(text = title, style = MaterialTheme.typography.titleSmall)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(CarlosTheme.spacing.sm),
        ) {
            swatches.forEach { color ->
                val argb = color.toArgb()
                val isSelected = selected == argb
                Swatch(color = color, selected = isSelected, onClick = { onPick(argb) })
            }
        }
    }
}

@Composable
private fun Swatch(color: Color, selected: Boolean, onClick: () -> Unit) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    Column(
        modifier = Modifier
            .size(36.dp)
            .background(color = color, shape = CircleShape)
            .border(
                width = if (selected) 3.dp else 1.dp,
                color = borderColor,
                shape = CircleShape,
            )
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {}
}
