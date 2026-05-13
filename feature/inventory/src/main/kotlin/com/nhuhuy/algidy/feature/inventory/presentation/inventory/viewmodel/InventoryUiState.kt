package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.model.food.FoodItem

import com.nhuhuy.algidy.core.presentation.viewmodel.UiState

@Stable
sealed interface InventoryUiState : UiState {
    object Loading : InventoryUiState
    data class Success(val items: List<FoodItem>) : InventoryUiState
    object Empty : InventoryUiState
}
