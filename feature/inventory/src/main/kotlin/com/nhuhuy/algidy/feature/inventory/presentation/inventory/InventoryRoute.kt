package com.nhuhuy.algidy.feature.inventory.presentation.inventory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryViewModel

@Composable
fun InventoryRoute(
    viewModel: InventoryViewModel,
    onNavigateToDetail: (id: String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCamera: () -> Unit
) = BoxLayout {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    InventoryScreen(
        uiState = uiState,
        categories = StorageLocation.entries.map { location -> location.name },
        onSearchClick = onNavigateToSearch,
        onItemClick = onNavigateToDetail,
        onScanClick = onNavigateToCamera,
        onManualAddClick = {
            //TODO
        },
    )
}
