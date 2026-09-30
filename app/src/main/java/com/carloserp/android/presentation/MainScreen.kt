package com.carloserp.android.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.carloserp.android.presentation.components.CarlosTopAppBar
import com.carloserp.android.presentation.components.OfflineBanner
import com.carloserp.android.presentation.navigation.BottomNavBar
import com.carloserp.android.presentation.navigation.CarlosNavHost
import com.carloserp.android.presentation.navigation.Destination

/**
 * Authenticated app shell (tasks 50.3 / 50.5).
 *
 * Hosts the [CarlosNavHost] inside a [Scaffold] with a title bar (whose title
 * tracks the current tab) and the [BottomNavBar]. The top bar exposes a logout
 * action; signing out clears the session and the host ([MainActivity]) swaps
 * back to the login screen. An [OfflineBanner] sits above the content when
 * [isOffline] is true.
 */
@Composable
fun MainScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    isOffline: Boolean = false,
    navController: NavHostController = rememberNavController(),
) {
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route
    val currentTab = Destination.TopLevel.entries.firstOrNull { it.route == currentRoute }
    // Detail/create screens are not top-level; they render their own top bar and
    // have no bottom nav, so the shell hides its shared bars for them.
    val isTopLevel = currentTab != null

    Scaffold(
        modifier = modifier,
        topBar = {
            currentTab?.let { tab ->
                CarlosTopAppBar(
                    title = tab.label,
                    actions = {
                        IconButton(onClick = onLogout) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Cerrar sesión",
                            )
                        }
                    },
                )
            }
        },
        bottomBar = {
            if (isTopLevel) BottomNavBar(navController)
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            OfflineBanner(visible = isOffline)
            CarlosNavHost(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
