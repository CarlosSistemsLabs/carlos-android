package com.carloserp.android.data.theme

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * User-customizable theme colors (ARGB ints, `null` = app default): screen
 * [backgroundColor], top-bar [headerTextColor], and bottom [navBarColor].
 */
data class ThemePrefs(
    val backgroundColor: Int? = null,
    val headerTextColor: Int? = null,
    val navBarColor: Int? = null,
)

/**
 * Persists the user's theme customization and exposes it as a hot [state] so the
 * UI re-applies colors immediately on change (no restart). Backed by a plain
 * prefs file; a missing key means "use the app default".
 */
@Singleton
class ThemePreferences @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _state = MutableStateFlow(read())
    val state: StateFlow<ThemePrefs> = _state.asStateFlow()

    fun setBackgroundColor(color: Int?) = update(KEY_BACKGROUND, color)
    fun setHeaderTextColor(color: Int?) = update(KEY_HEADER_TEXT, color)
    fun setNavBarColor(color: Int?) = update(KEY_NAV_BAR, color)

    fun reset() {
        prefs.edit().clear().apply()
        _state.value = read()
    }

    private fun update(key: String, color: Int?) {
        prefs.edit().apply {
            if (color == null) remove(key) else putInt(key, color)
        }.apply()
        _state.value = read()
    }

    private fun read(): ThemePrefs = ThemePrefs(
        backgroundColor = colorOrNull(KEY_BACKGROUND),
        headerTextColor = colorOrNull(KEY_HEADER_TEXT),
        navBarColor = colorOrNull(KEY_NAV_BAR),
    )

    private fun colorOrNull(key: String): Int? =
        if (prefs.contains(key)) prefs.getInt(key, 0) else null

    private companion object {
        const val PREFS_NAME = "carlos_theme"
        const val KEY_BACKGROUND = "background_color"
        const val KEY_HEADER_TEXT = "header_text_color"
        const val KEY_NAV_BAR = "nav_bar_color"
    }
}
