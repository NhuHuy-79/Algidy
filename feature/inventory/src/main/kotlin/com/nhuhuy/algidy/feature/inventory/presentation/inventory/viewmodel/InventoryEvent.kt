package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.viewmodel.UiEvent

sealed interface InventoryEvent : UiEvent {
    data class NavigateToEdit(val item: FoodItem) : InventoryEvent
    data object NavigateToSearch : InventoryEvent
    data object NavigateToFoodEntry : InventoryEvent
    data object NavigateToAnalytics : InventoryEvent
    data object NavigateToSetting : InventoryEvent
    data object NavigateToCamera : InventoryEvent
}
