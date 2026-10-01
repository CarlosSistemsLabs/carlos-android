package com.carloserp.android.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.ui.graphics.vector.ImageVector
import com.carloserp.android.R

/**
 * Base URI for app deep links (task 50.3).
 *
 * Destinations register a link like `carloserp://app/products`, and the
 * `MainActivity` intent-filter declares the same scheme/host so an external link
 * opens the matching screen.
 */
const val DEEP_LINK_SCHEME = "carloserp"
const val DEEP_LINK_HOST = "app"
private const val DEEP_LINK_PREFIX = "$DEEP_LINK_SCHEME://$DEEP_LINK_HOST"

/**
 * Every navigation destination in the app (task 50.3).
 *
 * A sealed hierarchy so routes are referenced by type, not loose strings. Each
 * declares its [route] and a [deepLink] built from the shared prefix.
 * [TopLevel] destinations additionally carry the [label]/[icon] shown in the
 * bottom navigation bar; [TopLevel.entries] is their fixed, ordered list.
 */
sealed class Destination(val route: String) {
    val deepLink: String get() = "$DEEP_LINK_PREFIX/$route"

    /** Login screen shown while signed out (outside the bottom-nav shell). */
    data object Login : Destination("login")

    /** Product detail, reached from the products list. Arg: [ARG] product id. */
    data object ProductDetail : Destination("products/{productId}") {
        const val ARG = "productId"
        fun createRoute(productId: String): String = "products/$productId"
    }

    /** Customer detail, reached from the customers list. Arg: [ARG] customer id. */
    data object CustomerDetail : Destination("customers/{customerId}") {
        const val ARG = "customerId"
        fun createRoute(customerId: String): String = "customers/$customerId"
    }

    /** New-sale form, reached from the sales list. */
    data object SaleCreate : Destination("sales/new")

    /** A destination that appears as a tab in the bottom navigation bar. */
    sealed class TopLevel(
        route: String,
        @StringRes val labelRes: Int,
        val icon: ImageVector,
    ) : Destination(route) {
        data object Dashboard : TopLevel("dashboard", R.string.nav_dashboard, Icons.Filled.Dashboard)
        data object Products : TopLevel("products", R.string.nav_products, Icons.Filled.Inventory2)
        data object Sales : TopLevel("sales", R.string.nav_sales, Icons.Filled.PointOfSale)
        data object Customers : TopLevel("customers", R.string.nav_customers, Icons.Filled.Group)
        data object Stock : TopLevel("stock", R.string.nav_stock, Icons.Filled.Warehouse)

        companion object {
            /** Ordered tabs for the bottom navigation bar. */
            val entries: List<TopLevel> = listOf(Dashboard, Products, Sales, Customers, Stock)

            /** The tab shown first after signing in. */
            val start: TopLevel = Dashboard
        }
    }
}
