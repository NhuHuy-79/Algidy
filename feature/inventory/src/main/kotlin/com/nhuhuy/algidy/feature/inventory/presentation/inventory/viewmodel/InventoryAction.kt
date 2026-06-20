package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction

@Stable
sealed interface InventoryAction : UiAction {
    data class OnCategorySelect(val categoryUiModel: CategoryUiModel) : InventoryAction
    data class RemoveItem(val id: String) : InventoryAction
    data object OnDismiss : InventoryAction
    data class OnCreateCategory(val name: String) : InventoryAction
    sealed interface OnEditCategorySheet : InventoryAction {
        data object Open : OnEditCategorySheet
        data class OnInputChange(val value: String) : OnEditCategorySheet
        data object Save : OnEditCategorySheet
    }

    data object OnDeleteAlertConfirm: InventoryAction
    data object OnDeleteCategory : InventoryAction
    data object OnSearchClick : InventoryAction
    data class OnItemClick(val item: FoodItem) : InventoryAction
    data object OnResetFilters : InventoryAction
    data object OnSortByExpiry : InventoryAction
    data object OnSortByName : InventoryAction
    data object OnShowExpiredOnly : InventoryAction
}


@Stable
sealed interface InventoryDetailAction : InventoryAction {
    data object Open : InventoryDetailAction
    data object OnEditClick : InventoryDetailAction
    data object OnConsumedClick : InventoryDetailAction
    data object OnWastedClick : InventoryDetailAction
}

@Stable
sealed interface InventoryFabAction : InventoryAction {
    data class ToggleFabMenu(val value: Boolean) : InventoryFabAction
    data object Manual : InventoryFabAction
    data object Analytics : InventoryFabAction
    data object Setting : InventoryFabAction
    data object BarcodeScan : InventoryFabAction
}