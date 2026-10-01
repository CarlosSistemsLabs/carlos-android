package com.carloserp.android.core.analytics

/**
 * Stable analytics event + screen names (task 51.2).
 *
 * Centralized so names stay consistent across the app (Firebase treats each
 * distinct string as its own event/screen). `login`/`logout` reuse Firebase's
 * recommended names; the rest are app-specific snake_case.
 */
object AnalyticsEvents {
    const val LOGIN = "login"
    const val LOGOUT = "logout"
    const val SALE_CREATED = "sale_created"
    const val PRODUCT_VIEWED = "product_viewed"
    const val CUSTOMER_VIEWED = "customer_viewed"
}

object AnalyticsParams {
    const val TENANT_ID = "tenant_id"
    const val ITEM_ID = "item_id"
}

object AnalyticsScreens {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val PRODUCTS = "products"
    const val PRODUCT_DETAIL = "product_detail"
    const val SALES = "sales"
    const val SALE_CREATE = "sale_create"
    const val CUSTOMERS = "customers"
    const val CUSTOMER_DETAIL = "customer_detail"
    const val STOCK = "stock"
}
