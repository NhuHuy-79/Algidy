package com.nhuhuy.algidy.feature.inventory.presentation.inventory

import android.Manifest
import androidx.activity.compose.BackHandler
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventoryFabMenu
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.category.CategoryEditDialog
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction.OnEditCategorySheet.OnInputChange
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction.OnEditCategorySheet.Save
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryEvent
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryFabAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryOverlay
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun InventoryRoute(
    onNavigateToDetail: (id: String) -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCamera: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToAddFood: () -> Unit,
) = BoxLayout {
    val viewModel: InventoryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val combineState by viewModel.combineState.collectAsStateWithLifecycle()
    val inventoryResultState by viewModel.resultState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            InventoryEvent.NavigateToFoodEntry -> onNavigateToAddFood()
            InventoryEvent.NavigateToAnalytics -> onNavigateToAnalytics()
            is InventoryEvent.NavigateToDetail -> onNavigateToDetail(event.id)
            InventoryEvent.NavigateToSearch -> onNavigateToSearch()
            InventoryEvent.NavigateToSetting -> onNavigateToSetting()
            InventoryEvent.NavigateToCamera -> {
                if (!cameraPermissionState.status.isGranted) {
                    cameraPermissionState.launchPermissionRequest()
                } else {
                    onNavigateToCamera()
                }
            }
        }
    }

    BackHandler(enabled = uiState.expanded || uiState.overlay != InventoryOverlay.NONE) {
        onAction(InventoryAction.OnDismiss)
        onAction(InventoryFabAction.ToggleFabMenu(false))
    }

    InventoryScreen(
        uiState = uiState,
        combineState = combineState,
        inventoryResultState = inventoryResultState,
        onAction = onAction
    )

    when (uiState.overlay) {
        InventoryOverlay.NONE -> Unit

        InventoryOverlay.CATEGORY_EDIT -> CategoryEditDialog(
            value = uiState.categorySheetInput,
            onValueChange = { category ->
                onAction(OnInputChange(category))
            },
            onDismiss = {
                onAction(InventoryAction.OnDismiss)
            },
            onConfirm = {
                onAction(Save)
            }
        )

        InventoryOverlay.CATEGORY_DELETE -> AlgidyAlertDialog(
            onDismissRequest = {
                onAction(InventoryAction.OnDismiss)
            },
            onConfirm = {
                onAction(InventoryAction.OnDeleteAlertConfirm)
            },
            title = stringResource(R.string.delete_category_dialog_title),
            text = stringResource(R.string.delete_category_dialog_content),
            confirmText = stringResource(R.string.delete_category_dialog_confirm)
        )
    }

    if (uiState.expanded) {
        Scrim(
            color = MaterialTheme.colorScheme.surface,
            onClick = { onAction(InventoryFabAction.ToggleFabMenu(false)) },
            contentDescription = "scrim",
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
            onExpandClose = { onAction(InventoryFabAction.ToggleFabMenu(it)) },
            onManualClick = { onAction(InventoryFabAction.Manual) },
            onSettingClick = { onAction(InventoryFabAction.Setting) },
            onBarcodeScanClick = { onAction(InventoryFabAction.BarcodeScan) },
            onAnalyticsClick = { onAction(InventoryFabAction.Analytics) }
        )
    }
}
