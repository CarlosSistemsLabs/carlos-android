package com.carloserp.android.presentation.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.carloserp.android.R
import com.carloserp.android.core.locale.AppLocale

/**
 * Top-bar action to switch the app language (task 52.1).
 *
 * Persists the choice via [AppLocale] and recreates the hosting Activity so the
 * new locale is applied through `attachBaseContext` and Compose recomposes with
 * the translated resources.
 */
@Composable
fun LanguageSwitcher() {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.Language,
                contentDescription = stringResource(R.string.language_cd),
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.language_spanish)) },
                onClick = {
                    expanded = false
                    changeLanguage(context, "es")
                },
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.language_english)) },
                onClick = {
                    expanded = false
                    changeLanguage(context, "en")
                },
            )
        }
    }
}

private fun changeLanguage(context: Context, tag: String) {
    if (AppLocale.persistedTag(context) == tag) return
    AppLocale.setLanguage(context, tag)
    context.findActivity()?.recreate()
}

private fun Context.findActivity(): Activity? {
    var current: Context? = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
}
