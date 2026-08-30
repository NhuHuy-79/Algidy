package com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState
import com.nhuhuy.algidy.feature.inventory.domain.model.SearchHistory
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class SearchUiState(
    val query: String = "",
    val searchHistories: ImmutableList<SearchHistory> = persistentListOf(),
    val surface: SearchUiSurface = SearchUiSurface.None,
    val currentCategory: CategoryUiModel = CategoryUiModel.All,
    val categories: ImmutableList<CategoryUiModel> = persistentListOf(
        CategoryUiModel.All, CategoryUiModel.Uncategorized,
    ),
) : UiState

sealed interface SearchUiSurface {
    data object None : SearchUiSurface
    data class EditFoodSheet(val food: FoodUiModel) : SearchUiSurface
    data class DetailBottomSheet(val food: FoodUiModel) : SearchUiSurface
}