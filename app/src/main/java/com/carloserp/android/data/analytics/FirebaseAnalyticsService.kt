package com.carloserp.android.data.analytics

import android.content.Context
import android.os.Bundle
import com.carloserp.android.core.analytics.AnalyticsParams
import com.carloserp.android.core.analytics.AnalyticsService
import com.carloserp.android.core.firebase.isFirebaseAvailable
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [AnalyticsService] backed by Firebase Analytics (task 51.2).
 *
 * The [FirebaseAnalytics] handle is obtained only when Firebase is configured
 * ([isFirebaseAvailable]); otherwise it stays `null` and every method is a safe
 * no-op, so analytics calls never crash a build without `google-services.json`.
 */
@Singleton
class FirebaseAnalyticsService @Inject constructor(
    @ApplicationContext context: Context,
) : AnalyticsService {

    private val analytics: FirebaseAnalytics? =
        if (isFirebaseAvailable(context)) FirebaseAnalytics.getInstance(context) else null

    override fun logScreenView(screenName: String) {
        analytics?.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            Bundle().apply { putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName) },
        )
    }

    override fun logEvent(name: String, params: Map<String, String>) {
        analytics?.logEvent(
            name,
            Bundle().apply { params.forEach { (key, value) -> putString(key, value) } },
        )
    }

    override fun setUser(userId: String?, tenantId: String?) {
        analytics?.apply {
            setUserId(userId)
            setUserProperty(AnalyticsParams.TENANT_ID, tenantId)
        }
    }
}
