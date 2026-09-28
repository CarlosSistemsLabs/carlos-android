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
import com.carloserp.android.presentation.theme.CarlosErpTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single-Activity host (task 49.1).
 *
 * [AndroidEntryPoint] lets Hilt inject dependencies into this activity (and the
 * Compose ViewModels reached from it). The Compose content is wrapped in the
 * app's Material 3 theme. Real navigation + feature screens land in tasks 50.x;
 * for now it renders a placeholder to keep the scaffold runnable.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarlosErpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PlaceholderScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "Carlos ERP", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Android client", style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceholderScreenPreview() {
    CarlosErpTheme {
        PlaceholderScreen()
    }
}
