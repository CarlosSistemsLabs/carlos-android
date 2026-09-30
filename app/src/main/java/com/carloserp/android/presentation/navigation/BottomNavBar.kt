package com.carloserp.android.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Bottom navigation bar for the top-level destinations (task 50.3).
 *
 * Highlights the tab matching the current back-stack entry and, on tap,
 * navigates with the standard bottom-nav semantics: pop up to the graph's start
 * (so back always returns there), [launchSingleTop] to avoid stacking copies,
 * and [restoreState]/`saveState` so each tab keeps its own scroll/nav position.
 */
@Composable
fun BottomNavBar(navController: NavHostController, modifier: Modifier = Modifier) {
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentEntry?.destination

    NavigationBar(modifier = modifier) {
        Destination.TopLevel.entries.forEach { tab ->
            val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) },
            )
        }
    }
}
