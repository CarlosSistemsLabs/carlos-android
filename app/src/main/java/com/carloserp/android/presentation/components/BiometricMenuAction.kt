package com.carloserp.android.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.carloserp.android.R
import com.carloserp.android.core.biometric.BiometricCrypto
import com.carloserp.android.core.biometric.canAuthenticateWithBiometrics
import com.carloserp.android.core.biometric.showBiometricPrompt
import com.carloserp.android.core.ui.findFragmentActivity
import com.carloserp.android.data.biometric.BiometricCredentials
import com.carloserp.android.presentation.biometric.BiometricEntryPoint
import dagger.hilt.android.EntryPointAccessors

/**
 * Top-bar overflow action to enable/disable biometric (fingerprint) sign-in
 * (biometric feature).
 *
 * Enabling asks the user to re-enter their password (workspace + email come from
 * the active session), then runs a BiometricPrompt to encrypt and store the
 * credentials behind the biometric-bound Keystore key. Disabling clears them.
 * The action is hidden when the device has no usable biometric and biometric
 * sign-in is not already enabled.
 */
@Composable
fun BiometricMenuAction() {
    val context = LocalContext.current
    val entryPoint = remember {
        EntryPointAccessors.fromApplication(context.applicationContext, BiometricEntryPoint::class.java)
    }
    val store = remember { entryPoint.biometricCredentialStore() }
    val sessionStore = remember { entryPoint.sessionStore() }

    val available = remember { canAuthenticateWithBiometrics(context) }
    var enabled by remember { mutableStateOf(store.isEnabled()) }
    var menuOpen by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    if (!available && !enabled) return

    Box {
        IconButton(onClick = { menuOpen = true }) {
            Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.more_options_cd))
        }
        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
            if (enabled) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.biometric_disable)) },
                    onClick = {
                        menuOpen = false
                        store.clear()
                        enabled = false
                    },
                )
            } else {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.biometric_enable)) },
                    onClick = {
                        menuOpen = false
                        showPasswordDialog = true
                    },
                )
            }
        }
    }

    if (showPasswordDialog) {
        val activity = context.findFragmentActivity()
        val tenantId = sessionStore.tenantId()
        val email = sessionStore.email()
        EnablePasswordDialog(
            onDismiss = { showPasswordDialog = false },
            onConfirm = { password ->
                if (activity != null && tenantId != null && email != null) {
                    val cipher = BiometricCrypto.getEncryptCipher()
                    showBiometricPrompt(
                        activity = activity,
                        cipher = cipher,
                        title = activity.getString(R.string.biometric_prompt_title),
                        subtitle = activity.getString(R.string.biometric_prompt_subtitle_enable),
                        negativeButton = activity.getString(R.string.biometric_cancel),
                        onSuccess = { authedCipher ->
                            store.persist(
                                authedCipher,
                                BiometricCredentials(tenantId = tenantId, email = email, password = password),
                            )
                            enabled = true
                        },
                        onError = {},
                        onCancel = {},
                    )
                }
                showPasswordDialog = false
            },
        )
    }
}

@Composable
private fun EnablePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var password by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.biometric_enable_dialog_title)) },
        text = {
            androidx.compose.foundation.layout.Column {
                Text(stringResource(R.string.biometric_enable_dialog_message))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.login_password)) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(password) },
                enabled = password.isNotBlank(),
            ) {
                Text(stringResource(R.string.biometric_enable_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.biometric_cancel))
            }
        },
    )
}
