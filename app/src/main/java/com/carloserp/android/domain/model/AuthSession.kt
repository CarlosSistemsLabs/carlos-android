package com.carloserp.android.domain.model

/**
 * A successful authentication result (task 50.1): the signed-in [user] plus the
 * JWT pair issued by the backend. The repository persists the tokens in the
 * encrypted session store and returns this to the caller.
 */
data class AuthSession(
    val user: AuthUser,
    val accessToken: String,
    val refreshToken: String,
)
