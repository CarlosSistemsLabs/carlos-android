package com.carloserp.android.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.carloserp.android.R
import com.carloserp.android.presentation.components.BiometricMenuAction
import com.carloserp.android.presentation.components.CarlosTopAppBar
import com.carloserp.android.presentation.components.LanguageSwitcher
import com.carloserp.android.presentation.components.OfflineBanner
import com.carloserp.android.presentation.navigation.BottomNavBar
import com.carloserp.android.presentation.navigation.CarlosNavHost
import com.carloserp.android.presentation.navigation.Destination

/**
 * Authenticated app shell (tasks 50.3 / 50.5 + detail-header fix).
 *
 * Owns the **single** [Scaffold] for the whole authenticated area: one top bar
 * for every destination (tab title + actions on top-level screens; a back arrow
 * + screen title on detail/create screens) and the [BottomNavBar] on top-level
 * only. Child screens render content without their own Scaffold, which avoids
 * the nested-Scaffold double inset that previously pushed detail headers off.
 */
@Composable
fun MainScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    isOffline: Boolean = false,
    headerTextColor: Color? = null,
    navBarColor: Color? = null,
    navController: NavHostController = rememberNavController(),
) {
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route
    val currentTab = Destination.TopLevel.entries.firstOrNull { it.route == currentRoute }
    val isTopLevel = currentTab != null
    val titleRes: Int? = currentTab?.labelRes ?: titleForRoute(currentRoute)

    Scaffold(
        modifier = modifier,
        topBar = {
            if (titleRes != null) {
                CarlosTopAppBar(
                    title = stringResource(titleRes),
                    contentColor = headerTextColor,
                    navigationIcon = {
                        if (!isTopLevel) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.action_back),
                                )
                            }
                        }
                    },
                    actions = {
                        if (isTopLevel) {
                            IconButton(onClick = { navController.navigate(Destination.Appearance.route) }) {
                                Icon(
                                    imageVector = Icons.Filled.Palette,
                                    contentDescription = stringResource(R.string.appearance_menu),
                                )
                            }
                            LanguageSwitcher()
                            BiometricMenuAction()
                            IconButton(onClick = onLogout) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = stringResource(R.string.action_logout),
                                )
                            }
                        }
                    },
                )
            }
        },
        bottomBar = {
            if (isTopLevel) BottomNavBar(navController, containerColor = navBarColor)
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

/** Top-bar title for non-top-level (detail/create) routes. */
@StringRes
private fun titleForRoute(route: String?): Int? = when (route) {
    Destination.ProductDetail.route -> R.string.product_title
    Destination.ProductCreate.route -> R.string.product_new_title
    Destination.CustomerDetail.route -> R.string.customer_title
    Destination.CustomerCreate.route -> R.string.customer_new_title
    Destination.SaleCreate.route -> R.string.sale_new_title
    Destination.Appearance.route -> R.string.appearance_title
    else -> null
}
