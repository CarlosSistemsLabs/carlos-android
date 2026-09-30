package com.carloserp.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.carloserp.android.presentation.customers.CustomerDetailScreen
import com.carloserp.android.presentation.customers.CustomersScreen
import com.carloserp.android.presentation.dashboard.DashboardScreen
import com.carloserp.android.presentation.products.ProductDetailScreen
import com.carloserp.android.presentation.products.ProductsScreen
import com.carloserp.android.presentation.sales.SaleCreateScreen
import com.carloserp.android.presentation.sales.SalesScreen
import com.carloserp.android.presentation.stock.StockScreen

/**
 * Navigation graph for the authenticated shell (tasks 50.3 / 50.4).
 *
 * Registers the five bottom-nav destinations (each with a `carloserp://app/<route>`
 * deep link) plus the detail/create screens they push. Detail/create screens
 * render their own top bar (with back), so [MainScreen] only shows its shared
 * top/bottom bars on the top-level destinations.
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
        composable(
            route = Destination.TopLevel.Dashboard.route,
            deepLinks = listOf(navDeepLink { uriPattern = Destination.TopLevel.Dashboard.deepLink }),
        ) {
            DashboardScreen()
        }

        composable(
            route = Destination.TopLevel.Products.route,
            deepLinks = listOf(navDeepLink { uriPattern = Destination.TopLevel.Products.deepLink }),
        ) {
            ProductsScreen(
                onProductClick = { id ->
                    navController.navigate(Destination.ProductDetail.createRoute(id))
                },
            )
        }

        composable(
            route = Destination.ProductDetail.route,
            arguments = listOf(navArgument(Destination.ProductDetail.ARG) { type = NavType.StringType }),
        ) {
            ProductDetailScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Destination.TopLevel.Sales.route,
            deepLinks = listOf(navDeepLink { uriPattern = Destination.TopLevel.Sales.deepLink }),
        ) {
            SalesScreen(onCreateClick = { navController.navigate(Destination.SaleCreate.route) })
        }

        composable(route = Destination.SaleCreate.route) {
            SaleCreateScreen(
                onBack = { navController.popBackStack() },
                onCreated = { navController.popBackStack() },
            )
        }

        composable(
            route = Destination.TopLevel.Customers.route,
            deepLinks = listOf(navDeepLink { uriPattern = Destination.TopLevel.Customers.deepLink }),
        ) {
            CustomersScreen(
                onCustomerClick = { id ->
                    navController.navigate(Destination.CustomerDetail.createRoute(id))
                },
            )
        }

        composable(
            route = Destination.CustomerDetail.route,
            arguments = listOf(navArgument(Destination.CustomerDetail.ARG) { type = NavType.StringType }),
        ) {
            CustomerDetailScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Destination.TopLevel.Stock.route,
            deepLinks = listOf(navDeepLink { uriPattern = Destination.TopLevel.Stock.deepLink }),
        ) {
            StockScreen()
        }
    }
}
