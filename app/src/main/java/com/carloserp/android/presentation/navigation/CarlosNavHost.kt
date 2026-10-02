package com.carloserp.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.carloserp.android.presentation.appearance.AppearanceScreen
import com.carloserp.android.presentation.customers.CustomerCreateScreen
import com.carloserp.android.presentation.customers.CustomerDetailScreen
import com.carloserp.android.presentation.customers.CustomersScreen
import com.carloserp.android.presentation.dashboard.DashboardScreen
import com.carloserp.android.presentation.products.ProductCreateScreen
import com.carloserp.android.presentation.products.ProductDetailScreen
import com.carloserp.android.presentation.products.ProductsScreen
import com.carloserp.android.presentation.sales.SaleCreateScreen
import com.carloserp.android.presentation.sales.SalesScreen
import com.carloserp.android.presentation.stock.StockScreen

/**
 * Navigation graph for the authenticated shell (tasks 50.3 / 50.4 + create flows).
 *
 * Registers the five bottom-nav destinations (each with a `carloserp://app/<route>`
 * deep link) plus the detail/create screens they push. The shell ([MainScreen])
 * owns the single top bar for every destination, so screens here render content
 * only (no per-screen Scaffold).
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
                onCreateClick = { navController.navigate(Destination.ProductCreate.route) },
            )
        }

        // Literal "products/new" must be registered before the "products/{productId}" pattern.
        composable(route = Destination.ProductCreate.route) {
            ProductCreateScreen(onCreated = { navController.popBackStack() })
        }

        composable(
            route = Destination.ProductDetail.route,
            arguments = listOf(navArgument(Destination.ProductDetail.ARG) { type = NavType.StringType }),
        ) {
            ProductDetailScreen()
        }

        composable(
            route = Destination.TopLevel.Sales.route,
            deepLinks = listOf(navDeepLink { uriPattern = Destination.TopLevel.Sales.deepLink }),
        ) {
            SalesScreen(onCreateClick = { navController.navigate(Destination.SaleCreate.route) })
        }

        composable(route = Destination.SaleCreate.route) {
            SaleCreateScreen(onCreated = { navController.popBackStack() })
        }

        composable(
            route = Destination.TopLevel.Customers.route,
            deepLinks = listOf(navDeepLink { uriPattern = Destination.TopLevel.Customers.deepLink }),
        ) {
            CustomersScreen(
                onCustomerClick = { id ->
                    navController.navigate(Destination.CustomerDetail.createRoute(id))
                },
                onCreateClick = { navController.navigate(Destination.CustomerCreate.route) },
            )
        }

        // Literal "customers/new" before the "customers/{customerId}" pattern.
        composable(route = Destination.CustomerCreate.route) {
            CustomerCreateScreen(onCreated = { navController.popBackStack() })
        }

        composable(
            route = Destination.CustomerDetail.route,
            arguments = listOf(navArgument(Destination.CustomerDetail.ARG) { type = NavType.StringType }),
        ) {
            CustomerDetailScreen()
        }

        composable(
            route = Destination.TopLevel.Stock.route,
            deepLinks = listOf(navDeepLink { uriPattern = Destination.TopLevel.Stock.deepLink }),
        ) {
            StockScreen()
        }

        composable(route = Destination.Appearance.route) {
            AppearanceScreen()
        }
    }
}
