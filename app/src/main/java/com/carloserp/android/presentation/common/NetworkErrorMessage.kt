package com.carloserp.android.presentation.common

import com.carloserp.android.core.network.NetworkError
import com.carloserp.android.core.network.isUnauthorized

/**
 * Maps a typed [NetworkError] to a short, user-facing Spanish message (task 50.4).
 *
 * Shared by every feature ViewModel so error copy stays consistent. Screens show
 * the result through `ErrorView`.
 */
fun NetworkError.toUserMessage(): String = when {
    isUnauthorized -> "Tu sesión expiró. Iniciá sesión de nuevo."
    this is NetworkError.Http && code == HTTP_FORBIDDEN ->
        "Tu plan o permisos no habilitan esta función."
    this is NetworkError.Http && code == HTTP_NOT_FOUND -> "No encontramos lo que buscabas."
    this is NetworkError.Http -> "Error del servidor ($code). Intentá de nuevo."
    this is NetworkError.Connection -> "Sin conexión. Revisá tu internet e intentá de nuevo."
    this is NetworkError.Serialization -> "Respuesta inesperada del servidor."
    else -> "Ocurrió un error. Intentá de nuevo."
}

private const val HTTP_FORBIDDEN = 403
private const val HTTP_NOT_FOUND = 404
