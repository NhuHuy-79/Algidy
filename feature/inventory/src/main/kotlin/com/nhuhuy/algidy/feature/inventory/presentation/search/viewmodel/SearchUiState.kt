package com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState
import com.nhuhuy.algidy.feature.inventory.domain.model.SearchHistory
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodCardUiModel

@Immutable
data class SearchUiState(
    val query: String = "",
    val isExpanded: Boolean = false,
    val isLoading: Boolean = false,
    val searchHistory: List<String> = emptyList(),
    val searchHistories: List<SearchHistory> = emptyList(),
    val searchResults: List<FoodCardUiModel> = emptyList(),
    val surface: SearchUiSurface = SearchUiSurface.None
) : UiState

sealed interface SearchUiSurface {
    data object None : SearchUiSurface
    data class DetailBottomSheet(val food: FoodCardUiModel) : SearchUiSurface
}