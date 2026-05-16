package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.compose.runtime.Stable

import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction

@Stable
sealed interface InventoryAction : UiAction {
    data class RemoveItem(val id: String) : InventoryAction
    data object OnManualAddClick : InventoryAction
}
