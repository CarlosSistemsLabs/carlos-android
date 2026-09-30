package com.carloserp.android.core.format

import java.math.BigDecimal

/**
 * Formatting helpers for the backend's decimal-string money values (task 50.4).
 *
 * The API serialises money as strings like `"19.90"`. We keep them as strings
 * end-to-end (no floating-point) and only format for display here, pairing the
 * amount with its ISO currency code. Parsing failures fall back to the raw
 * string so the UI never crashes on unexpected input.
 */
fun formatMoney(amount: String, currency: String): String {
    val decimal = amount.toBigDecimalOrNull() ?: return "$amount $currency".trim()
    val normalized = decimal.setScale(MONEY_SCALE, java.math.RoundingMode.HALF_UP)
    return "$currency $normalized"
}

/**
 * Formats a decimal-string amount to 2 decimals with a leading `$`, for values
 * that don't carry a currency code (e.g. the reports aggregates). Falls back to
 * the raw string on parse failure.
 */
fun formatAmount(amount: String): String {
    val decimal = amount.toBigDecimalOrNull() ?: return amount
    return "$ ${decimal.setScale(MONEY_SCALE, java.math.RoundingMode.HALF_UP)}"
}

private fun String.toBigDecimalOrNull(): BigDecimal? =
    try {
        BigDecimal(this)
    } catch (_: NumberFormatException) {
        null
    }

private const val MONEY_SCALE = 2
