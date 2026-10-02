package com.carloserp.android.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.carloserp.android.presentation.theme.CarlosErpTheme

/**
 * App top bar (tasks 50.2 + theme customization).
 *
 * A thin wrapper over Material 3 [TopAppBar] with consistent title styling and
 * optional [navigationIcon]/[actions] slots. When [contentColor] is provided
 * (user header-color customization), the title and icons use it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarlosTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    contentColor: Color? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    val colors = if (contentColor != null) {
        TopAppBarDefaults.topAppBarColors(
            titleContentColor = contentColor,
            navigationIconContentColor = contentColor,
            actionIconContentColor = contentColor,
        )
    } else {
        TopAppBarDefaults.topAppBarColors()
    }
    TopAppBar(
        title = { Text(title) },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun CarlosTopAppBarPreview() {
    CarlosErpTheme {
        CarlosTopAppBar(title = "Dashboard")
    }
}
