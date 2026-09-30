package com.carloserp.android.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.carloserp.android.presentation.theme.CarlosErpTheme

/**
 * App top bar (task 50.2).
 *
 * A thin wrapper over Material 3 [TopAppBar] that fixes the app's title style
 * and optional [navigationIcon]/[actions] slots, so every screen gets a
 * consistent header. Navigation wiring (back button behaviour) arrives with the
 * nav graph in task 50.3.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarlosTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = navigationIcon,
        actions = actions,
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
