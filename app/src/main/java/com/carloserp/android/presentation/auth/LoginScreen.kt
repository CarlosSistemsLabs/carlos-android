package com.carloserp.android.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.R
import com.carloserp.android.core.analytics.AnalyticsScreens
import com.carloserp.android.core.ui.asString
import com.carloserp.android.presentation.analytics.TrackScreenView
import com.carloserp.android.presentation.components.CarlosPasswordField
import com.carloserp.android.presentation.components.CarlosTextField
import com.carloserp.android.presentation.components.PrimaryButton
import com.carloserp.android.presentation.theme.CarlosErpTheme
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * Login screen (tasks 50.1 / 50.2).
 *
 * A stateless [LoginContent] rendered from [LoginUiState], with the stateful
 * entry point obtaining its [LoginViewModel] via Hilt. Built from the shared
 * design-system components ([CarlosTextField]/[CarlosPasswordField]/
 * [PrimaryButton]) and spacing tokens. On a successful login the repository
 * persists the session and the app-level auth state switches away from this
 * screen, so there is no explicit navigation here.
 */
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    TrackScreenView(AnalyticsScreens.LOGIN)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LoginContent(
        state = uiState,
        onWorkspaceChange = viewModel::onWorkspaceChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = viewModel::onSubmit,
        modifier = modifier,
    )
}

@Composable
private fun LoginContent(
    state: LoginUiState,
    onWorkspaceChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = CarlosTheme.spacing
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacing.lg, vertical = spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = stringResource(R.string.app_name), style = MaterialTheme.typography.headlineMedium)
        Text(
            text = stringResource(R.string.login_subtitle),
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(spacing.xl))

        CarlosTextField(
            value = state.workspaceId,
            onValueChange = onWorkspaceChange,
            label = stringResource(R.string.login_workspace),
            enabled = !state.isSubmitting,
            imeAction = ImeAction.Next,
        )
        Spacer(Modifier.height(spacing.md))

        CarlosTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.login_email),
            enabled = !state.isSubmitting,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
        )
        Spacer(Modifier.height(spacing.md))

        CarlosPasswordField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.login_password),
            enabled = !state.isSubmitting,
            imeAction = ImeAction.Done,
        )

        val error = state.errorMessage
        if (error != null) {
            Spacer(Modifier.height(spacing.md))
            Text(
                text = error.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(Modifier.height(spacing.lg))
        PrimaryButton(
            text = stringResource(R.string.action_login),
            onClick = onSubmit,
            enabled = state.canSubmit,
            loading = state.isSubmitting,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginContentPreview() {
    CarlosErpTheme {
        LoginContent(
            state = LoginUiState(email = "admin@carlos-erp.test"),
            onWorkspaceChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {},
        )
    }
}
