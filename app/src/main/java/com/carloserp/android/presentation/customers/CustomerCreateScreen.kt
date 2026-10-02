package com.carloserp.android.presentation.customers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.R
import com.carloserp.android.core.ui.asString
import com.carloserp.android.presentation.components.CarlosTextField
import com.carloserp.android.presentation.components.PrimaryButton
import com.carloserp.android.presentation.components.SecondaryButton
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * New-customer form (create customers from the app). Header owned by the shell;
 * on success calls [onCreated] to pop back to the list.
 *
 * The address is not typed free-form: [onValidateAddress] opens the
 * address-validation screen, which returns the chosen address through
 * [validatedAddress]. When a value arrives it is fed into the form and
 * [onValidatedAddressConsumed] clears it so it is applied only once.
 */
@Composable
fun CustomerCreateScreen(
    onCreated: () -> Unit,
    onValidateAddress: () -> Unit,
    modifier: Modifier = Modifier,
    validatedAddress: String? = null,
    onValidatedAddressConsumed: () -> Unit = {},
    viewModel: CustomerCreateViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.created) {
        LaunchedEffect(Unit) { onCreated() }
    }

    LaunchedEffect(validatedAddress) {
        if (validatedAddress != null) {
            viewModel.onAddressChange(validatedAddress)
            onValidatedAddressConsumed()
        }
    }

    val spacing = CarlosTheme.spacing
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(spacing.md),
        verticalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        CarlosTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = stringResource(R.string.field_name),
            enabled = !state.submitting,
        )
        CarlosTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = stringResource(R.string.customer_email),
            enabled = !state.submitting,
            keyboardType = KeyboardType.Email,
        )
        CarlosTextField(
            value = state.phone,
            onValueChange = viewModel::onPhoneChange,
            label = stringResource(R.string.customer_phone),
            enabled = !state.submitting,
            keyboardType = KeyboardType.Phone,
        )
        CarlosTextField(
            value = state.taxId,
            onValueChange = viewModel::onTaxIdChange,
            label = stringResource(R.string.customer_tax_id),
            enabled = !state.submitting,
        )
        if (state.address.isNotBlank()) {
            Text(
                text = stringResource(R.string.customer_address),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = state.address,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        SecondaryButton(
            text = stringResource(
                if (state.address.isBlank()) {
                    R.string.address_validate_entry
                } else {
                    R.string.address_validate_change
                },
            ),
            onClick = onValidateAddress,
            enabled = !state.submitting,
        )

        CarlosTextField(
            value = state.notes,
            onValueChange = viewModel::onNotesChange,
            label = stringResource(R.string.customer_notes),
            enabled = !state.submitting,
        )

        state.error?.let { error ->
            Text(
                text = error.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        PrimaryButton(
            text = stringResource(R.string.action_save),
            onClick = viewModel::submit,
            enabled = state.canSubmit,
            loading = state.submitting,
        )
    }
}
