package com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.model.toFoodCategory
import com.nhuhuy.algidy.core.presentation.model.toUiModel
import com.nhuhuy.algidy.core.presentation.navigation.Navigator
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.food_entry.domain.usecase.GetCategoriesUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.GetHistoryResultUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.ObserveFoodItemsUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.UpdateSearchHistoryUseCase
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.model.toFoodUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchUiSurface.DetailBottomSheet
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchUiSurface.EditFoodSheet
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchUiSurface.None
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class SearchViewModel(
    private val getHistoryResultUseCase: GetHistoryResultUseCase,
    private val updateSearchHistoryUseCase: UpdateSearchHistoryUseCase,
    private val navigator: Navigator,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val observeFoodItemsUseCase: ObserveFoodItemsUseCase,
) : BaseViewModel<SearchUiState, SearchEvent, SearchAction>() {
    private val _uiState = MutableStateFlow(SearchUiState())
    override val uiState: StateFlow<SearchUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val historyResults = getHistoryResultUseCase()
                .sortedByDescending { historyResult -> historyResult.timeStamp }
                .distinctBy { it.name }
                .toImmutableList()
            val categories = getCategoriesUseCase().toUiModel().toImmutableList()
            _uiState.product { copy(searchHistories = historyResults, categories = categories) }
        }
    }

    val foodResult: StateFlow<ImmutableList<FoodUiModel>> = combine(
        _uiState.map { it.currentCategory to it.query },
        observeFoodItemsUseCase()
    ) { (category, query), foodItems ->
        foodItems
            .asSequence()
            .filter { food -> food.name.contains(query, ignoreCase = true) }
            .filter { food ->
                category == CategoryUiModel.All
                        || food.categoryId == category.toFoodCategory()?.id
            }
            .map { food -> food.toFoodUiModel() }
            .toImmutableList()

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        persistentListOf()
    )

    override fun onAction(action: SearchAction) {
        when (action) {
            SearchAction.OnBack -> navigator.navigateBack()

            SearchAction.OnClearQuery -> {
                _uiState.product { copy(query = "") }
            }

            is SearchAction.OnHistoryClick -> {
                _uiState.product { copy(query = action.history) }
            }

            is SearchAction.OnQueryChange -> {
                _uiState.product { copy(query = action.newQuery) }
            }

            is SearchAction.OnEditSheetOpen -> {
                _uiState.product { copy(surface = EditFoodSheet(action.food)) }
            }

            is SearchAction.OnSearchResultClick -> viewModelScope.launch {
                updateSearchHistoryUseCase(action.searchResult.name)
                _uiState.product {
                    copy(surface = DetailBottomSheet(action.searchResult))
                }
            }

            SearchAction.OnDismiss -> {
                _uiState.product { copy(surface = None) }
            }

            is SearchAction.OnCategorySelect -> {
                _uiState.product { copy(currentCategory = action.category) }
            }
        }
    }
}