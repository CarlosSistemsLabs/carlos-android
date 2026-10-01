package com.carloserp.android.presentation.common

import com.carloserp.android.R
import com.carloserp.android.core.network.NetworkError
import com.carloserp.android.core.network.isUnauthorized
import com.carloserp.android.core.ui.UiText

/**
 * Maps a typed [NetworkError] to a localizable [UiText] (tasks 50.4 / 52.1).
 *
 * Returns string-resource references (resolved by the UI against the current
 * locale) instead of hard-coded text, so error copy is translated like the rest
 * of the app. Shared by every feature ViewModel; shown through `ErrorView`.
 */
fun NetworkError.toUserMessage(): UiText = when {
    isUnauthorized -> UiText.res(R.string.error_session_expired)
    this is NetworkError.Http && code == HTTP_FORBIDDEN -> UiText.res(R.string.error_forbidden)
    this is NetworkError.Http && code == HTTP_NOT_FOUND -> UiText.res(R.string.error_not_found)
    this is NetworkError.Http -> UiText.res(R.string.error_server, code)
    this is NetworkError.Connection -> UiText.res(R.string.error_connection)
    this is NetworkError.Serialization -> UiText.res(R.string.error_serialization)
    else -> UiText.res(R.string.error_unknown)
}

private const val HTTP_FORBIDDEN = 403
private const val HTTP_NOT_FOUND = 404
