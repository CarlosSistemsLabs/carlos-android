package com.carloserp.android.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * A string that can be produced outside composition (e.g. in a ViewModel) and
 * resolved later against the current locale (task 52.1).
 *
 * ViewModels build [Res] values (a string resource + optional format args) so
 * error/message text stays localizable; the UI resolves them with [asString].
 * [Dynamic] wraps an already-resolved string for the rare non-localizable case.
 */
sealed interface UiText {
    data class Dynamic(val value: String) : UiText
    data class Res(@StringRes val id: Int, val args: List<Any> = emptyList()) : UiText

    companion object {
        fun res(@StringRes id: Int, vararg args: Any): Res = Res(id, args.toList())
    }
}

/** Resolves this [UiText] against the composition's locale. */
@Composable
fun UiText.asString(): String = when (this) {
    is UiText.Dynamic -> value
    is UiText.Res -> stringResource(id, *args.toTypedArray())
}

/** Resolves this [UiText] against a [context]'s configuration (non-composable). */
fun UiText.asString(context: Context): String = when (this) {
    is UiText.Dynamic -> value
    is UiText.Res -> context.getString(id, *args.toTypedArray())
}
