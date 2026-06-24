package com.nhuhuy.algidy.feature.inventory.presentation.inventory

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scrim
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.AppNewFeatureBottomSheet
import com.nhuhuy.algidy.core.presentation.component.TextFieldDialog
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventoryFabMenu
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.detail.DetailBottomSheet
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction.OnEditCategorySheet.OnInputChange
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction.OnEditCategorySheet.Save
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryDetailAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryEvent
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryFabAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryOverlay
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun InventoryRoute(
    onNavigateToAnalytics: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCamera: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToAddFood: () -> Unit,
    onNavigateToEditFood: (item: FoodItem) -> Unit,
) = BoxLayout {
    val viewModel: InventoryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val combineState by viewModel.combineState.collectAsStateWithLifecycle()
    val inventoryResultState by viewModel.resultState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    LaunchedEffect(Unit) {
        if (uiState.currentVersionCode < combineState.appVersionToNotify) {
            onAction(InventoryAction.ShowAppFeature)
        }
    }

    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            InventoryEvent.NavigateToFoodEntry -> onNavigateToAddFood()
            InventoryEvent.NavigateToAnalytics -> onNavigateToAnalytics()
            is InventoryEvent.NavigateToEdit -> onNavigateToEditFood(event.item)
            InventoryEvent.NavigateToSearch -> onNavigateToSearch()
            InventoryEvent.NavigateToSetting -> onNavigateToSetting()
            InventoryEvent.NavigateToCamera -> onNavigateToCamera()
        }
    }

    // Chỉ xử lý Back cho Fab Menu và các Dialog không phải Bottom Sheet
    BackHandler(enabled = uiState.expanded || uiState.overlay == InventoryOverlay.CategoryAdd || uiState.overlay == InventoryOverlay.CategoryEdit || uiState.overlay == InventoryOverlay.CategoryDelete) {
        if (uiState.expanded) {
            onAction(InventoryFabAction.ToggleFabMenu(false))
        } else {
            onAction(InventoryAction.OnDismiss)
        }
    }

    InventoryScreen(
        uiState = uiState,
        combineState = combineState,
        inventoryResultState = inventoryResultState,
        onAction = onAction
    )

    when (val overlay = uiState.overlay) {
        InventoryOverlay.None -> Unit

        InventoryOverlay.CategoryEdit -> TextFieldDialog(
            title = stringResource(R.string.category_edit_dialog_title),
            value = uiState.categoryInput,
            confirmText = stringResource(R.string.inventory_category_edit_btn),
            onValueChange = { category -> onAction(OnInputChange(category)) },
            onDismiss = { onAction(InventoryAction.OnDismiss) },
            onConfirm = { onAction(Save) }
        )

        InventoryOverlay.CategoryDelete -> AlgidyAlertDialog(
            onDismissRequest = { onAction(InventoryAction.OnDismiss) },
            onConfirm = { onAction(InventoryAction.OnDeleteAlertConfirm) },
            title = stringResource(R.string.delete_category_dialog_title),
            text = stringResource(R.string.delete_category_dialog_content),
            confirmText = stringResource(R.string.delete_category_dialog_confirm)
        )

        InventoryOverlay.ItemDetail -> DetailBottomSheet(
            foodItem = uiState.currentFoodItem,
            categoryUiModel = uiState.currentCategory,
            onDismiss = { onAction(InventoryAction.OnDismiss) },
            onEditClick = { onAction(InventoryDetailAction.OnEditClick) },
            onWastedClick = { onAction(InventoryDetailAction.OnWastedClick) },
            onConsumedClick = { onAction(InventoryDetailAction.OnConsumedClick) }
        )

        InventoryOverlay.CategoryAdd -> TextFieldDialog(
            value = uiState.categoryInput,
            title = stringResource(R.string.inventory_category_add),
            confirmText = stringResource(R.string.inventory_category_add_btn),
            onValueChange = {
                onAction(InventoryAction.OnAddCategory.OnInputChange(it))
            },
            onDismiss = {
                onAction(InventoryAction.OnDismiss)
            },
            onConfirm = {
                onAction(InventoryAction.OnAddCategory.Save)
            }
        )

        is InventoryOverlay.NewFeatureSheet -> AppNewFeatureBottomSheet(
            versionFeatures = overlay.versionFeature,
            onDismiss = {
                onAction(InventoryAction.OnDismiss)
            }
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
