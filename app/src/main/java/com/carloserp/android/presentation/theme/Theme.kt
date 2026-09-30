package com.carloserp.android.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    error = LightError,
    onError = LightOnError,
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    error = DarkError,
    onError = DarkOnError,
)

/**
 * App Material 3 theme (tasks 49.1 / 50.2).
 *
 * Resolves the colour scheme in priority order:
 *  1. Material You wallpaper palette when [dynamicColor] is on (Android 12+),
 *  2. otherwise the default brand light/dark scheme,
 * then layers the per-tenant [branding] overrides on top so a tenant's primary/
 * secondary colours flow through every component. Also installs the shared
 * [CarlosShapes] and [Spacing] design tokens and publishes [branding] via
 * [LocalTenantBranding].
 */
@Composable
fun CarlosErpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    branding: TenantBranding = TenantBranding.None,
    content: @Composable () -> Unit,
) {
    val baseScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }
    val colorScheme = baseScheme.withBranding(branding, dark = darkTheme)

    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
        LocalTenantBranding provides branding,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = CarlosShapes,
            typography = CarlosTypography,
            content = content,
        )
    }
}

/**
 * Convenience accessors for the app's design tokens that Material 3 does not
 * carry on [MaterialTheme] (currently [spacing]). Colours/shapes/typography stay
 * on `MaterialTheme`.
 */
object CarlosTheme {
    val spacing: Spacing
        @Composable @ReadOnlyComposable get() = LocalSpacing.current

    val shapes: Shapes
        @Composable @ReadOnlyComposable get() = MaterialTheme.shapes

    val typography: Typography
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography
}
