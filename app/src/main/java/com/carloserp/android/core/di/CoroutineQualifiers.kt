package com.carloserp.android.core.di

import javax.inject.Qualifier

/**
 * Hilt qualifiers for the coroutine dispatchers (task 49.3).
 *
 * Injecting a dispatcher (instead of hard-coding `Dispatchers.IO` etc.) lets
 * repositories/use cases declare their threading needs explicitly and lets
 * tests swap in a test dispatcher. Provided by
 * [com.carloserp.android.core.di.DispatchersModule].
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher
