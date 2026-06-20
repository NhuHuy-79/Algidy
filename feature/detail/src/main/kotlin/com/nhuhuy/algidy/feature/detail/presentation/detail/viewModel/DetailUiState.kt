package com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState

@Immutable
data class DetailUiState(
    val detailFoodItem: FoodItem = FoodItem(),
    val category: CategoryUiModel = CategoryUiModel.Uncategorized,
    val actionState: DetailOverlay = DetailOverlay.None,
) : UiState

enum class DetailOverlay {
    None,
    Wasted,
    Consume,
    Edit,
}
