package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.compose.runtime.Stable

import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.model.CategoryUiModel

@Stable
sealed interface InventoryAction : UiAction {
    data class ToggleFabMenu(val value: Boolean) : InventoryAction
    data class OnCategorySelect(val categoryUiModel: CategoryUiModel) : InventoryAction

    data class RemoveItem(val id: String) : InventoryAction
    data object OnAddFabClick : InventoryAction
    data object OnManuallyClick : InventoryAction
    data object OnDismiss : InventoryAction
}
