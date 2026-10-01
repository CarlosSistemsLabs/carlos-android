package com.carloserp.android.core.firebase

import android.content.Context
import com.google.firebase.FirebaseApp

/**
 * Whether Firebase is configured and initialized (task 51.1).
 *
 * Returns `true` only when a `google-services.json` was bundled and
 * [FirebaseApp] initialized successfully. The Firebase-backed Analytics/
 * Crashlytics/Performance services check this before touching any Firebase API,
 * so they stay inert (no crashes, no logs) when Firebase is absent.
 */
fun isFirebaseAvailable(context: Context): Boolean =
    FirebaseApp.getApps(context).isNotEmpty()
