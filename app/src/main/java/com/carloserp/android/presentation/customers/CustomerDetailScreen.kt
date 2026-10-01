package com.carloserp.android.presentation.customers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.core.analytics.AnalyticsScreens
import com.carloserp.android.domain.model.Customer
import com.carloserp.android.presentation.analytics.TrackScreenView
import com.carloserp.android.presentation.components.SectionCard
import com.carloserp.android.presentation.components.UiStateContent
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * Customer detail screen (task 50.4). Full screen with its own back-arrow top
 * bar; loads via [CustomerDetailViewModel] through the shared [UiStateContent].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CustomerDetailViewModel = hiltViewModel(),
) {
    TrackScreenView(AnalyticsScreens.CUSTOMER_DETAIL)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Cliente") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { innerPadding ->
        UiStateContent(
            state = uiState,
            modifier = Modifier.padding(innerPadding),
            onRetry = viewModel::load,
        ) { customer ->
            CustomerDetailContent(customer = customer)
        }
    }
}

@Composable
private fun CustomerDetailContent(customer: Customer, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(CarlosTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(CarlosTheme.spacing.md),
    ) {
        Text(text = customer.name, style = MaterialTheme.typography.headlineSmall)

        SectionCard {
            customer.email?.let { DetailRow("Email", it) }
            customer.phone?.let { DetailRow("Teléfono", it) }
            customer.taxId?.let { DetailRow("CUIT/CUIL", it) }
            customer.address?.let { DetailRow("Dirección", it) }
            DetailRow("Estado", if (customer.isActive) "Activo" else "Inactivo")
        }

        customer.notes?.takeIf { it.isNotBlank() }?.let { notes ->
            SectionCard {
                Text("Notas", style = MaterialTheme.typography.titleSmall)
                Text(
                    text = notes,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = CarlosTheme.spacing.xs),
                )
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = CarlosTheme.spacing.xs)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
