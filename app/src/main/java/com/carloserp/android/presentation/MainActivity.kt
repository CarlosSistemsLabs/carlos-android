package com.carloserp.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.presentation.theme.CarlosErpTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single-Activity host (tasks 49.1/49.3).
 *
 * [AndroidEntryPoint] lets Hilt inject into this activity and the Compose
 * ViewModels reached from it. The screen obtains its [MainViewModel] via
 * `hiltViewModel()` and renders its state — the end-to-end DI wiring. Real
 * navigation + feature screens arrive in tasks 50.x.
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
                    HomeScreen(state = uiState, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(state: MainUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = state.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = state.subtitle, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    CarlosErpTheme {
        HomeScreen(state = MainUiState())
    }
}
