package com.carloserp.android.data.crash

import android.content.Context
import com.carloserp.android.core.crash.CrashReporter
import com.carloserp.android.core.firebase.isFirebaseAvailable
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [CrashReporter] backed by Firebase Crashlytics (task 51.3).
 *
 * The Crashlytics handle is obtained only when Firebase is configured, so every
 * method is a safe no-op without `google-services.json`.
 */
@Singleton
class FirebaseCrashReporter @Inject constructor(
    @ApplicationContext context: Context,
) : CrashReporter {

    private val crashlytics: FirebaseCrashlytics? =
        if (isFirebaseAvailable(context)) FirebaseCrashlytics.getInstance() else null

    override fun setUser(userId: String?, tenantId: String?) {
        crashlytics?.apply {
            setUserId(userId.orEmpty())
            setCustomKey(KEY_TENANT, tenantId.orEmpty())
        }
    }

    override fun log(message: String) {
        crashlytics?.log(message)
    }

    override fun recordException(throwable: Throwable) {
        crashlytics?.recordException(throwable)
    }

    private companion object {
        const val KEY_TENANT = "tenant_id"
    }
}
