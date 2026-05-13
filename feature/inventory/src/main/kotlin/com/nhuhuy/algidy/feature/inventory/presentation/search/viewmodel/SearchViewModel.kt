package com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.inventory.domain.usecase.GetHistoryResultUseCase
import com.nhuhuy.algidy.feature.inventory.domain.usecase.SearchFoodUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getHistoryResultUseCase: GetHistoryResultUseCase,
    private val searchFoodUseCase: SearchFoodUseCase,
) : BaseViewModel<SearchUiState, SearchEvent, SearchAction>() {
    private val _uiState = MutableStateFlow(SearchUiState())
    override val uiState: StateFlow<SearchUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val historyResults = getHistoryResultUseCase().map { food -> food.name }
            _uiState.product { copy(searchHistory = historyResults) }
        }
    }

    override fun onAction(action: SearchAction) {
        when (action) {
            SearchAction.OnBack -> {
                //On Back button
            }

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

            is SearchAction.OnSearch -> viewModelScope.launch {
                _uiState.product { copy(isLoading = true) }
                val searchResults = searchFoodUseCase(currentState.query)
                _uiState.product { copy(searchResults = searchResults, isLoading = false) }
            }
        }
    }
}