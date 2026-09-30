package com.carloserp.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.presentation.auth.LoginScreen
import com.carloserp.android.presentation.theme.CarlosErpTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single-Activity host (tasks 49.1 / 49.3 / 50.1).
 *
 * Renders a splash while the session resolves, then switches between
 * [LoginScreen] and [HomeScreen] based on the app-level auth state exposed by
 * [MainViewModel]. Because the login repository persists the session and the
 * auth state re-emits, the switch happens automatically without explicit
 * navigation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarlosErpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = hiltViewModel<MainViewModel>()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    val contentModifier = Modifier.padding(innerPadding)

                    when {
                        uiState.isResolvingSession ->
                            SplashScreen(modifier = contentModifier)

                        uiState.isLoggedIn ->
                            HomeScreen(
                                state = uiState,
                                onLogout = viewModel::logout,
                                modifier = contentModifier,
                            )

                        else ->
                            LoginScreen(modifier = contentModifier)
                    }
                }
            }
        }
    }
}

@Composable
private fun SplashScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun HomeScreen(
    state: MainUiState,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = state.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = state.subtitle, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onLogout) {
            Text("Cerrar sesión")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    CarlosErpTheme {
        HomeScreen(state = MainUiState(isLoggedIn = true), onLogout = {})
    }
}
