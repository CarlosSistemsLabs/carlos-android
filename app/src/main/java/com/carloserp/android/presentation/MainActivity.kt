package com.carloserp.android.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.carloserp.android.core.locale.AppLocale
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.presentation.appearance.ThemeViewModel
import com.carloserp.android.presentation.auth.LoginScreen
import com.carloserp.android.presentation.theme.CarlosErpTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single-Activity host (tasks 49.1 / 49.3 / 50.1 / 50.3).
 *
 * Renders a splash while the session resolves, then switches between the
 * [LoginScreen] (signed out) and the [MainScreen] navigation shell (signed in)
 * based on the app-level auth state from [MainViewModel]. Because the login
 * repository persists the session and the auth state re-emits, the switch
 * happens automatically without manual navigation.
 */
@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppLocale.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel = hiltViewModel<ThemeViewModel>()
            val themePrefs by themeViewModel.prefs.collectAsStateWithLifecycle()

            CarlosErpTheme(backgroundColor = themePrefs.backgroundColor?.let { Color(it) }) {
                val viewModel = hiltViewModel<MainViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                when {
                    uiState.isResolvingSession ->
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            SplashScreen(modifier = Modifier.padding(innerPadding))
                        }

                    uiState.isLoggedIn ->
                        MainScreen(
                            onLogout = viewModel::logout,
                            isOffline = uiState.isOffline,
                            headerTextColor = themePrefs.headerTextColor?.let { Color(it) },
                            navBarColor = themePrefs.navBarColor?.let { Color(it) },
                            modifier = Modifier.fillMaxSize(),
                        )

                    else ->
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            LoginScreen(modifier = Modifier.padding(innerPadding))
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
