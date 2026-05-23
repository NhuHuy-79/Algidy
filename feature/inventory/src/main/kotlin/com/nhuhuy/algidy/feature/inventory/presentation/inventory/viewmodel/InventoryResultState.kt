package com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.model.food.FoodItem

import com.nhuhuy.algidy.core.presentation.viewmodel.UiState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.model.CategoryUiModel

@Stable
sealed interface InventoryResultState {
    object Loading : InventoryResultState
    data class Success(val items: List<FoodItem>) : InventoryResultState
    object Empty : InventoryResultState
}

@Immutable
data class InventoryCombineState(
    val categoryEnabled: Boolean = false,
    val categories: List<CategoryUiModel> = listOf(CategoryUiModel.All),
)

@Immutable
data class InventoryUiState(
    val expanded: Boolean = false,
    val currentCategory: CategoryUiModel = CategoryUiModel.All,
    val categorySheetInput: String = "",
    val overlay: InventoryOverlay = InventoryOverlay.NONE,
    val sortMode: InventorySortMode = InventorySortMode.NONE,
    val showExpiredOnly: Boolean = false
) : UiState {
    val showCategoryEdit : Boolean get() = currentCategory is CategoryUiModel.ByCategory
}

enum class InventoryOverlay {
    NONE, FOOD_SHEET, CATEGORY_EDIT
}

enum class InventorySortMode {
    BY_NAME, BY_EXPIRY, NONE
}
