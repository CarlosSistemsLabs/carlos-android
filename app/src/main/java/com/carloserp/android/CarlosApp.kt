package com.carloserp.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point (task 49.1).
 *
 * Annotated with [HiltAndroidApp] so Hilt generates the application-level
 * dependency container and can inject into activities/ViewModels across the app
 * (DI is wired in task 49.3). Kept intentionally empty otherwise.
 */
@HiltAndroidApp
class CarlosApp : Application()
