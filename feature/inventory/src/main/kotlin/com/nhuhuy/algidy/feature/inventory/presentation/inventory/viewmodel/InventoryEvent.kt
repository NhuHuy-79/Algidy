package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import com.nhuhuy.algidy.core.presentation.viewmodel.UiEvent

sealed interface InventoryEvent : UiEvent {
    data class NavigateToDetail(val id: String) : InventoryEvent
    data object NavigateToSearch : InventoryEvent
    data object NavigateToFoodEntry : InventoryEvent
    data object NavigateToAnalytics : InventoryEvent
    data object NavigateToSetting : InventoryEvent
    data object NavigateToCamera : InventoryEvent
}
