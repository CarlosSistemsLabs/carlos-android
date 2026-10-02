package com.carloserp.android.presentation.customers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carloserp.android.R
import com.carloserp.android.core.analytics.AnalyticsScreens
import com.carloserp.android.domain.model.Customer
import com.carloserp.android.presentation.analytics.TrackScreenView
import com.carloserp.android.presentation.components.EmptyView
import com.carloserp.android.presentation.components.RefreshableUiState
import com.carloserp.android.presentation.theme.CarlosTheme

/**
 * Customers list screen (task 50.4). Renders the [CustomersViewModel] state via
 * the shared [UiStateContent]; tapping a row navigates to the detail via
 * [onCustomerClick].
 */
@Composable
fun CustomersScreen(
    onCustomerClick: (String) -> Unit,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CustomersViewModel = hiltViewModel(),
) {
    TrackScreenView(AnalyticsScreens.CUSTOMERS)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    Box(modifier = modifier.fillMaxSize()) {
        RefreshableUiState(
            state = uiState,
            isRefreshing = isRefreshing,
            onRefresh = viewModel::refresh,
        ) { customers ->
            if (customers.isEmpty()) {
                EmptyView(message = stringResource(R.string.customers_empty))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items = customers, key = { it.id }) { customer ->
                        CustomerRow(customer = customer, onClick = { onCustomerClick(customer.id) })
                        HorizontalDivider()
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = onCreateClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(CarlosTheme.spacing.md),
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.customer_create_cd))
        }
    }
}

@Composable
private fun CustomerRow(customer: Customer, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = CarlosTheme.spacing.md, vertical = CarlosTheme.spacing.sm),
    ) {
        Text(text = customer.name, style = MaterialTheme.typography.bodyLarge)
        val subtitle = customer.email ?: customer.phone
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
