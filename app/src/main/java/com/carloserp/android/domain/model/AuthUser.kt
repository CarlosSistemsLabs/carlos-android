package com.carloserp.android.domain.model

/**
 * The authenticated user (task 50.1) — the domain view of the account.
 *
 * A framework-free model the presentation layer depends on, mapped from the
 * network `UserDto`. [displayName] is derived for convenience in the UI.
 */
data class AuthUser(
    val id: String,
    val tenantId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val roleId: String,
) {
    val displayName: String
        get() = "$firstName $lastName".trim().ifBlank { email }
}
