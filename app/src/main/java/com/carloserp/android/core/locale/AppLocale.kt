package com.carloserp.android.core.locale

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * App language management (task 52.1).
 *
 * Persists the chosen language tag and wraps a [Context] so Android resolves
 * `strings.xml` against it. When no language was chosen, [wrap] returns the base
 * context unchanged, so the app follows the device locale (the standard Android
 * localization behaviour). Apply it from [android.app.Application.attachBaseContext]
 * and each Activity's `attachBaseContext`.
 */
object AppLocale {
    /** BCP-47 tags the app ships translations for. */
    val supportedTags = listOf("es", "en")

    private const val PREFS = "carlos_locale"
    private const val KEY_LANGUAGE = "language_tag"

    /** The persisted language tag, or `null` to follow the device locale. */
    fun persistedTag(context: Context): String? =
        prefs(context).getString(KEY_LANGUAGE, null)

    /** Persists the selected language. The caller recreates the Activity to apply it. */
    fun setLanguage(context: Context, tag: String) {
        prefs(context).edit().putString(KEY_LANGUAGE, tag).apply()
    }

    /**
     * Applies a tenant/user language preference (task 52.1 seam). Same as
     * [setLanguage]; call it once the tenant language is known (the mobile login
     * payload doesn't carry it yet) and recreate the Activity.
     */
    fun applyTenantLanguage(context: Context, tag: String) {
        if (tag in supportedTags) setLanguage(context, tag)
    }

    /** Returns a context localized to the persisted language, or [base] if none. */
    fun wrap(base: Context): Context {
        val tag = persistedTag(base) ?: return base
        val locale = Locale.forLanguageTag(tag)
        Locale.setDefault(locale)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
}
