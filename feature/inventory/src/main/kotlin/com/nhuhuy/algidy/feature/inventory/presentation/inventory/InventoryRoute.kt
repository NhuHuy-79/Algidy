package com.nhuhuy.algidy.feature.inventory.presentation.inventory

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scrim
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.FoodEntryBottomSheet
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventoryFabMenu
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
    val categoryEnabledState by viewModel.categoryEnableState.collectAsStateWithLifecycle()
    val entryState by viewModel.entryState.collectAsStateWithLifecycle()
    val errorState by viewModel.entryError.collectAsStateWithLifecycle()
    val inventoryResultState by viewModel.resultState.collectAsStateWithLifecycle()
    val onEntryAction = viewModel::onEntryAction
    val onAction = viewModel::onAction

    val scrimAlpha by animateFloatAsState(
        targetValue = if (uiState.expanded) 0.6f else 0f,
        label = "scrim"
    )

    InventoryScreen(
        uiState = uiState,
        categoryEnabled = categoryEnabledState,
        inventoryResultState = inventoryResultState,
        onSearchClick = onNavigateToSearch,
        onItemClick = onNavigateToDetail,
    )

    when (uiState.overlay) {
        InventoryOverlay.NONE -> Unit
        InventoryOverlay.FOOD_SHEET -> FoodEntryBottomSheet(
            title = stringResource(R.string.inventory_sheet_title),
            label = stringResource(R.string.inventory_sheet_btn),
            onDismiss = { onAction(InventoryAction.OnDismiss) },
            foodEntryState = entryState,
            foodEntryError = errorState,
            onEntryAction = onEntryAction,
            onConfirm = { onAction(InventoryAction.OnManuallyClick) }
        )
    }

    if (uiState.expanded) {
        Scrim(
            color = MaterialTheme.colorScheme.surface.copy(alpha = scrimAlpha),
            onClick = { onAction(InventoryAction.ToggleFabMenu(false)) },
            contentDescription = "scrimm",
            modifier = Modifier.fillMaxSize(),
            alpha = { 0.6f }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp, end = 16.dp)
            .safeDrawingPadding(),
        contentAlignment = Alignment.BottomEnd
    ) {
        InventoryFabMenu(
            expanded = uiState.expanded,
            onExpandClose = { onAction(InventoryAction.ToggleFabMenu(it)) },
            onManualClick = { onAction(InventoryAction.OnAddFabClick) },
            onSettingClick = onNavigateToSetting,
            onBarcodeScanClick = onNavigateToCamera,
            onAnalyticsClick = onNavigateToAnalytics
        )
    }

}
