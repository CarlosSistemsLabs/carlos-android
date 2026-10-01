package com.carloserp.android

import android.app.Application
import android.content.Context
import com.carloserp.android.core.locale.AppLocale
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point (tasks 49.1 / 51.1).
 *
 * [HiltAndroidApp] generates the application-level DI container. On startup it
 * also initializes Firebase: [FirebaseApp.initializeApp] is a no-op that returns
 * `null` (without throwing) when no `google-services.json` is bundled, so the
 * app runs fine without Firebase credentials and the Analytics/Crashlytics/
 * Performance services degrade to no-ops (see `core/firebase`).
 */
@HiltAndroidApp
class CarlosApp : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(AppLocale.wrap(base))
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
