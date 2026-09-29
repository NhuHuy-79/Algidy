package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel

@Stable
sealed interface InventoryFoodAction : InventoryAction {
    data class DeleteFood(val id: String) : InventoryFoodAction
    data object Consume : InventoryFoodAction
    data object Waste : InventoryFoodAction
    data class Click(val food: FoodUiModel) : InventoryFoodAction
    data class Edit(val food: FoodUiModel) : InventoryFoodAction
}