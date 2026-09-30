package com.carloserp.android.core.network

import kotlinx.coroutines.flow.Flow

/**
 * Reports device connectivity (task 50.5).
 *
 * [isOnline] is a hot [Flow] that emits the current reachability and then every
 * change, so the UI can show an offline banner and features can react to
 * connectivity without polling.
 */
interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}
