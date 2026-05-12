package com.nhuhuy.algidy.feature.inventory.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.feature.inventory.presentation.viewmodel.InventoryViewModel

@Composable
fun InventoryRoute(
    viewModel: InventoryViewModel,
    onNavigateToDetail: (id: String) -> Unit,
    onNavigateToCamera: () -> Unit
) = BoxLayout {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    InventoryScreen(
        uiState = uiState,
        categories = StorageLocation.entries.map { location -> location.name },
        onItemClick = onNavigateToDetail,
        onScanClick = onNavigateToCamera,
        onManualAddClick = {
            //TODO
        },
    )
}
