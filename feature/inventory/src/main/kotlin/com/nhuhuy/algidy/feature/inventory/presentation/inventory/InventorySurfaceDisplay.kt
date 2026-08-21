package com.nhuhuy.algidy.feature.inventory.presentation.inventory

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.AppNewFeatureBottomSheet
import com.nhuhuy.algidy.core.presentation.component.TextFieldDialog
import com.nhuhuy.algidy.feature.food_entry.navigation.FoodEntryRoute
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction.OnEditCategorySheet.OnInputChange
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction.OnEditCategorySheet.Save
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryOverlay
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryUiState
import com.nhuhuy.algidy.feature.inventory.presentation.shared.DetailBottomSheetRoute

@Composable
internal fun InventoryOverlayContainer(
    uiState: InventoryUiState,
    onAction: (InventoryAction) -> Unit
) {
    val onDismiss = { onAction(InventoryAction.OnDismiss) }
    when (val overlay = uiState.overlay) {
        InventoryOverlay.None -> Unit

        InventoryOverlay.CategoryEdit -> TextFieldDialog(
            title = stringResource(R.string.category_edit_dialog_title),
            value = uiState.categoryInput,
            confirmText = stringResource(R.string.inventory_category_edit_btn),
            onValueChange = { category -> onAction(OnInputChange(category)) },
            onDismiss = onDismiss,
            onConfirm = { onAction(Save) }
        )

        InventoryOverlay.CategoryDelete -> AlgidyAlertDialog(
            icon = AlgidyIcons.Inventory.DeleteCategory.toImageVector(),
            onDismissRequest = onDismiss,
            onConfirm = { onAction(InventoryAction.OnDeleteAlertConfirm) },
            title = stringResource(R.string.delete_category_dialog_title),
            text = stringResource(R.string.delete_category_dialog_content),
            confirmText = stringResource(R.string.delete_category_dialog_confirm)
        )

        InventoryOverlay.ItemDetail -> DetailBottomSheetRoute(
            foodItem = uiState.currentFoodItem,
            onDismiss = onDismiss
        )

        InventoryOverlay.CategoryAdd -> TextFieldDialog(
            value = uiState.categoryInput,
            title = stringResource(R.string.inventory_category_add),
            confirmText = stringResource(R.string.inventory_category_add_btn),
            onValueChange = {
                onAction(InventoryAction.OnAddCategory.OnInputChange(it))
            },
            onDismiss = onDismiss,
            onConfirm = { onAction(InventoryAction.OnAddCategory.Save) }
        )

        is InventoryOverlay.NewFeatureSheet -> AppNewFeatureBottomSheet(
            versionFeatures = overlay.versionFeature,
            onDismiss = onDismiss
        )

        InventoryOverlay.ConsumeConfirm -> AlgidyAlertDialog(
            icon = AlgidyIcons.ConsumeFood.toImageVector(),
            onDismissRequest = onDismiss,
            onConfirm = { onAction(InventoryAction.OnConsumeConfirm) },
            title = stringResource(R.string.detail_dialog_consume_title),
            text = stringResource(R.string.detail_dialog_consume_content),
            confirmText = stringResource(R.string.detail_fab_consume_this)
        )

        InventoryOverlay.WasteConfirm -> AlgidyAlertDialog(
            icon = AlgidyIcons.WasteFood.toImageVector(),
            onDismissRequest = onDismiss,
            onConfirm = { onAction(InventoryAction.OnWasteConfirm) },
            title = stringResource(R.string.detail_dialog_waste_title),
            text = stringResource(R.string.detail_dialog_waste_content),
            confirmText = stringResource(R.string.detail_fab_mark_as_wasted),
            isDestructive = true
        )

        InventoryOverlay.CameraPolicySheet -> AlgidyAlertDialog(
            icon = AlgidyIcons.Inventory.ScanFood.toImageVector(),
            onDismissRequest = onDismiss,
            onConfirm = { onAction(InventoryAction.OnConfirmCameraPolicy) },
            title = stringResource(R.string.camera_permission_title),
            text = stringResource(R.string.camera_permission_description),
            confirmText = stringResource(R.string.camera_permission_confirm_btn),
        )

        is InventoryOverlay.AddFoodBottomSheet -> FoodEntryRoute(foodId = overlay.food?.id)
    }
}