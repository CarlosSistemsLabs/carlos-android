package com.carloserp.android.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * Navigation graph for the authenticated shell (task 50.3).
 *
 * Registers one composable per [Destination.TopLevel], each with a
 * [navDeepLink] so an external `carloserp://app/<route>` URI opens the matching
 * tab. Screens are placeholders for now; the real feature UIs replace them in
 * task 50.4. The graph starts at [Destination.TopLevel.start] (Dashboard).
 */
@Composable
fun CarlosNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Destination.TopLevel.start.route,
        modifier = modifier,
    ) {
        Destination.TopLevel.entries.forEach { destination ->
            composable(
                route = destination.route,
                deepLinks = listOf(navDeepLink { uriPattern = destination.deepLink }),
            ) {
                PlaceholderScreen(title = destination.label)
            }
        }
    }
}

/**
 * Temporary content for a not-yet-built feature screen (task 50.3). Replaced by
 * the real screens in task 50.4.
 */
@Composable
private fun PlaceholderScreen(title: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(CarlosTheme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(CarlosTheme.spacing.sm))
        Text(
            text = "Próximamente",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
