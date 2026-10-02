package com.carloserp.android.presentation.biometric

import com.carloserp.android.core.auth.SessionStore
import com.carloserp.android.data.biometric.BiometricCredentialStore
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt entry point to reach the biometric credential store and the session store
 * from composables that drive the BiometricPrompt but don't own a ViewModel
 * (biometric feature).
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface BiometricEntryPoint {
    fun biometricCredentialStore(): BiometricCredentialStore
    fun sessionStore(): SessionStore
}
