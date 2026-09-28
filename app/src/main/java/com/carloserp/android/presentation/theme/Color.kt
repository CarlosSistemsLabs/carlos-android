package com.carloserp.android.presentation.theme

import androidx.compose.ui.graphics.Color

/**
 * Brand + Material 3 palette (task 49.1).
 *
 * The brand seed colours mirror the web client's design tokens
 * (`--color-brand-primary/secondary/accent`). Dynamic per-tenant branding
 * (task 50.2) will later override these at runtime; these are the static
 * defaults used until then.
 */

// Brand seed colours (aligned with the web design system).
val BrandPrimary = Color(0xFF1E40AF)
val BrandSecondary = Color(0xFF64748B)
val BrandAccent = Color(0xFF0EA5E9)

// Light scheme roles.
val LightPrimary = BrandPrimary
val LightOnPrimary = Color(0xFFFFFFFF)
val LightSecondary = BrandSecondary
val LightOnSecondary = Color(0xFFFFFFFF)
val LightBackground = Color(0xFFF8FAFC)
val LightOnBackground = Color(0xFF0F172A)
val LightSurface = Color(0xFFFFFFFF)
val LightOnSurface = Color(0xFF0F172A)
val LightError = Color(0xFFDC2626)
val LightOnError = Color(0xFFFFFFFF)

// Dark scheme roles.
val DarkPrimary = Color(0xFF93B4FF)
val DarkOnPrimary = Color(0xFF0B1B3F)
val DarkSecondary = Color(0xFFB0BAC9)
val DarkOnSecondary = Color(0xFF1B2430)
val DarkBackground = Color(0xFF0F172A)
val DarkOnBackground = Color(0xFFE2E8F0)
val DarkSurface = Color(0xFF1E293B)
val DarkOnSurface = Color(0xFFE2E8F0)
val DarkError = Color(0xFFFCA5A5)
val DarkOnError = Color(0xFF450A0A)
