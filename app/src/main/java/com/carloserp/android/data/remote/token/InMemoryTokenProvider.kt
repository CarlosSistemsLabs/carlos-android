package com.carloserp.android.data.remote.token

import com.carloserp.android.core.network.TokenProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Temporary in-memory [TokenProvider] (task 49.2).
 *
 * Holds the access token in a process-lifetime field so the network layer is
 * fully wired and testable now. It is intentionally NOT persistent — the
 * encrypted, disk-backed implementation replaces this in task 50.1 (auth flow),
 * at which point the Hilt binding is repointed with no change to callers.
 */
@Singleton
class InMemoryTokenProvider @Inject constructor() : TokenProvider {
    @Volatile
    private var accessToken: String? = null

    override fun accessToken(): String? = accessToken

    /** Stores (or clears, with `null`) the current access token. */
    fun update(token: String?) {
        accessToken = token
    }
}
