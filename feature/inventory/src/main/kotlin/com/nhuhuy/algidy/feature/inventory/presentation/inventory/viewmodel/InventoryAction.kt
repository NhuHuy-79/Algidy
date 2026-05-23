package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.compose.runtime.Stable

import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel

@Stable
sealed interface InventoryAction : UiAction {
    data class ToggleFabMenu(val value: Boolean) : InventoryAction
    data class OnCategorySelect(val categoryUiModel: CategoryUiModel) : InventoryAction
    data class RemoveItem(val id: String) : InventoryAction
    data object OnAddFabClick : InventoryAction
    data object OnManuallyClick : InventoryAction
    data object OnDismiss : InventoryAction
    data class OnCreateCategory(val name: String) : InventoryAction
    sealed interface OnEditCategorySheet : InventoryAction {
        data object Open : OnEditCategorySheet
        data class OnInputChange(val value: String) : OnEditCategorySheet
        data object Save : OnEditCategorySheet
    }

    data object OnDeleteCategory : InventoryAction
    data object OnSearchClick : InventoryAction
    data class OnItemClick(val id: String) : InventoryAction
    data object OnResetFilters : InventoryAction
    data object OnSortByExpiry : InventoryAction
    data object OnSortByName : InventoryAction
    data object OnShowExpiredOnly : InventoryAction
}
