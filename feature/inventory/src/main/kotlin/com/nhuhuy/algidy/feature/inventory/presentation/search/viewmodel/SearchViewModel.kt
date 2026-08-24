package com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.navigation.Navigator
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.inventory.domain.usecase.GetHistoryResultUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.food.SearchFoodUseCase
import com.nhuhuy.algidy.feature.inventory.presentation.model.toFoodUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchUiSurface.DetailBottomSheet
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchUiSurface.None
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getHistoryResultUseCase: GetHistoryResultUseCase,
    private val searchFoodUseCase: SearchFoodUseCase,
    private val navigator: Navigator,
) : BaseViewModel<SearchUiState, SearchEvent, SearchAction>() {
    private val _uiState = MutableStateFlow(SearchUiState())
    override val uiState: StateFlow<SearchUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val historyResults = getHistoryResultUseCase().sortedByDescending { historyResult ->
                historyResult.timeStamp
            }.distinctBy { it.name }
            _uiState.product { copy(searchHistories = historyResults) }
        }
    }

    override fun onAction(action: SearchAction) {
        when (action) {
            SearchAction.OnBack -> navigator.navigateBack()

            SearchAction.OnClearQuery -> {
                _uiState.product { copy(query = "") }
            }

            is SearchAction.OnExpandedChange -> {
                _uiState.product { copy(isExpanded = action.expanded) }
            }

            is SearchAction.OnHistoryClick -> {
                _uiState.product { copy(query = action.history) }
            }

            is SearchAction.OnQueryChange -> {
                _uiState.product { copy(query = action.newQuery) }
            }

            is SearchAction.OnEditSheetOpen -> {
                _uiState.product { copy(surface = SearchUiSurface.EditFoodSheet(action.food)) }
            }

            is SearchAction.OnSearch -> viewModelScope.launch {
                _uiState.product { copy(isLoading = true) }

                val searchResults = searchFoodUseCase(currentState.query).toFoodUiModel()

                _uiState.product {
                    copy(searchResults = searchResults, isLoading = false)
                }
            }

            is SearchAction.OnSearchResultClick -> {
                _uiState.product {
                    copy(
                        surface = DetailBottomSheet(action.searchResult)
                    )
                }
            }

            SearchAction.OnDismiss -> {
                _uiState.product { copy(surface = None) }
            }
        }
    }
}