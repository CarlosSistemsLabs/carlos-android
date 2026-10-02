package com.carloserp.android.presentation.appearance

import androidx.lifecycle.ViewModel
import com.carloserp.android.data.theme.ThemePreferences
import com.carloserp.android.data.theme.ThemePrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Exposes the user's theme customization ([prefs]) and the setters the
 * appearance screen uses. Backed by the shared [ThemePreferences] singleton, so
 * the host (which also observes it) re-applies colors immediately.
 */
@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themePreferences: ThemePreferences,
) : ViewModel() {

    val prefs: StateFlow<ThemePrefs> = themePreferences.state

    fun setBackgroundColor(color: Int?) = themePreferences.setBackgroundColor(color)
    fun setHeaderTextColor(color: Int?) = themePreferences.setHeaderTextColor(color)
    fun setNavBarColor(color: Int?) = themePreferences.setNavBarColor(color)
    fun reset() = themePreferences.reset()
}
