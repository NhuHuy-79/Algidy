package com.nhuhuy.algidy.feature.detail.presentation.viewModel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.FoodItem

@Immutable
data class DetailUiState(
    val foodItem: FoodItem = FoodItem()
)
