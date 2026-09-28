package com.carloserp.android.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Auth request/response payloads for the backend `/auth` endpoints (task 50.1).
 *
 * Shapes mirror the backend contract exactly:
 * - `POST /auth/login`   → [LoginRequestDto]  → [AuthResponseDto]
 * - `POST /auth/refresh` → [RefreshRequestDto] → [RefreshResponseDto]
 * - `POST /auth/logout`  → [LogoutRequestDto]  → (no body)
 *
 * Nullable fields default to `null` so the tolerant [kotlinx.serialization.json.Json]
 * (configured with `ignoreUnknownKeys`/`explicitNulls`) decodes partial payloads.
 */
@Serializable
data class LoginRequestDto(
    val tenantId: String,
    val email: String,
    val password: String,
)

@Serializable
data class RefreshRequestDto(
    val refreshToken: String,
)

@Serializable
data class LogoutRequestDto(
    val refreshToken: String,
    val tenantId: String? = null,
)

/** Response of `POST /auth/login`: the authenticated user plus the token pair. */
@Serializable
data class AuthResponseDto(
    val user: UserDto,
    val accessToken: String,
    val refreshToken: String,
)

/** Response of `POST /auth/refresh`: a freshly rotated token pair. */
@Serializable
data class RefreshResponseDto(
    val accessToken: String,
    val refreshToken: String,
)

/** The authenticated user as returned by the backend. */
@Serializable
data class UserDto(
    val id: String,
    val tenantId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val roleId: String,
    val phone: String? = null,
    val avatar: String? = null,
    val isActive: Boolean = true,
    val lastLoginAt: String? = null,
)
