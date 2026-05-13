package com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState

@Immutable
data class SearchUiState(
    val query: String = "",
    val isExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val searchHistory: List<String> = emptyList(),
    val searchResults: List<FoodItem> = emptyList()
) : UiState