package com.nhuhuy.algidy.feature.inventory.presentation.inventory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.component.FoodEntryForm
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryOverlay
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun InventoryRoute(
    onNavigateToDetail: (id: String) -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCamera: () -> Unit,
    onNavigateToSetting: () -> Unit,
) = BoxLayout {
    val viewModel: InventoryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val entryState by viewModel.entryState.collectAsStateWithLifecycle()
    val errorState by viewModel.entryError.collectAsStateWithLifecycle()
    val inventoryResultState by viewModel.resultState.collectAsStateWithLifecycle()
    val onEntryAction = viewModel::onEntryAction
    val onAction = viewModel::onAction
    InventoryScreen(
        inventoryResultState = inventoryResultState,
        categories = StorageLocation.entries.map { location -> location.name },
        onSearchClick = onNavigateToSearch,
        onItemClick = onNavigateToDetail,
        onManualAddClick = {
            onAction(InventoryAction.OnManualAddClick)
        },
        onAnalyticsClick = onNavigateToAnalytics,
        onSettingClick = onNavigateToSetting,
        onBarcodeScanClick = onNavigateToCamera,
    )

    when (uiState.overlay) {
        InventoryOverlay.NONE -> Unit
        InventoryOverlay.FOOD_SHEET -> FoodEntryForm(
            entryState = entryState,
            errorState = errorState,
            onAction = onEntryAction
        )
    }

}
