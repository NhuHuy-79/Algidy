package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.model.food.FoodItem

import com.nhuhuy.algidy.core.presentation.viewmodel.UiState

@Stable
sealed interface InventoryResultState {
    object Loading : InventoryResultState
    data class Success(val items: List<FoodItem>) : InventoryResultState
    object Empty : InventoryResultState
}

@Immutable
data class InventoryUiState(
    val overlay: InventoryOverlay = InventoryOverlay.NONE
) : UiState

enum class InventoryOverlay {
    NONE, FOOD_SHEET,
}