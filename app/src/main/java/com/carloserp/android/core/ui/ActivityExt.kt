package com.carloserp.android.core.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity

/**
 * Walks the context wrapper chain to find the hosting [FragmentActivity]
 * (needed by BiometricPrompt). Returns `null` if the chain reaches a non-Fragment
 * [Activity] or no activity at all.
 */
fun Context.findFragmentActivity(): FragmentActivity? {
    var current: Context? = this
    while (current is ContextWrapper) {
        if (current is FragmentActivity) return current
        if (current is Activity) return null
        current = current.baseContext
    }
    return null
}
