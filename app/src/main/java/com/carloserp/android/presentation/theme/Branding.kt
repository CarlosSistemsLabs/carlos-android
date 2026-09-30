package com.carloserp.android.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/**
 * Per-tenant branding (task 50.2).
 *
 * Runtime overrides sourced from the tenant's branding settings (the same
 * `primary`/`secondary` tokens the web client exposes). Colours are optional so
 * a tenant can override just the primary; a `null` field falls back to the
 * default brand palette. Parse hex strings from the API with [fromHex].
 */
data class TenantBranding(
    val primary: Color? = null,
    val secondary: Color? = null,
) {
    val hasOverrides: Boolean get() = primary != null || secondary != null

    companion object {
        val None = TenantBranding()

        /**
         * Builds a branding from `#RRGGBB` / `#AARRGGBB` hex strings (as stored
         * by the backend), ignoring values that fail to parse.
         */
        fun fromHex(primary: String?, secondary: String? = null): TenantBranding =
            TenantBranding(primary = primary.toColorOrNull(), secondary = secondary.toColorOrNull())
    }
}

/** Branding in scope for the current composition; [TenantBranding.None] by default. */
val LocalTenantBranding = staticCompositionLocalOf { TenantBranding.None }

/**
 * Returns a copy of this [ColorScheme] with the tenant's brand colours applied
 * to the primary/secondary roles.
 *
 * On-colours and container tones are derived from each seed (contrast-aware) so
 * text stays legible without shipping a full tonal-palette generator. Roles the
 * tenant did not override are left untouched.
 */
fun ColorScheme.withBranding(branding: TenantBranding, dark: Boolean): ColorScheme {
    if (!branding.hasOverrides) return this
    var scheme = this
    branding.primary?.let { seed ->
        scheme = scheme.copy(
            primary = seed,
            onPrimary = seed.contrastingOn(),
            primaryContainer = if (dark) seed.darken(0.35f) else seed.lighten(0.6f),
            onPrimaryContainer = if (dark) seed.lighten(0.6f) else seed.darken(0.45f),
        )
    }
    branding.secondary?.let { seed ->
        scheme = scheme.copy(
            secondary = seed,
            onSecondary = seed.contrastingOn(),
            secondaryContainer = if (dark) seed.darken(0.35f) else seed.lighten(0.6f),
            onSecondaryContainer = if (dark) seed.lighten(0.6f) else seed.darken(0.45f),
        )
    }
    return scheme
}

/** Black or white, whichever contrasts better with this colour. */
internal fun Color.contrastingOn(): Color =
    if (luminance() > CONTRAST_THRESHOLD) Color.Black else Color.White

/** Blends toward white by [fraction] (0f = unchanged, 1f = white). */
internal fun Color.lighten(fraction: Float): Color = blend(Color.White, fraction)

/** Blends toward black by [fraction] (0f = unchanged, 1f = black). */
internal fun Color.darken(fraction: Float): Color = blend(Color.Black, fraction)

private fun Color.blend(other: Color, fraction: Float): Color {
    val f = fraction.coerceIn(0f, 1f)
    return Color(
        red = red + (other.red - red) * f,
        green = green + (other.green - green) * f,
        blue = blue + (other.blue - blue) * f,
        alpha = alpha,
    )
}

private fun String?.toColorOrNull(): Color? {
    val raw = this?.trim()?.removePrefix("#") ?: return null
    val hex = when (raw.length) {
        6 -> "FF$raw"
        8 -> raw
        else -> return null
    }
    return hex.toLongOrNull(radix = 16)?.let { Color(it.toInt()) }
}

private const val CONTRAST_THRESHOLD = 0.5f
