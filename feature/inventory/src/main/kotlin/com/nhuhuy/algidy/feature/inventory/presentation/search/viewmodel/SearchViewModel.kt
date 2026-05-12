package com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel(
    private val foodRepository: FoodRepository
) : BaseViewModel<SearchUiState, SearchEvent, SearchAction>() {
    private val _uiState = MutableStateFlow(SearchUiState())
    override val uiState: StateFlow<SearchUiState>
        get() = _uiState.asStateFlow()

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
                //
            }

            is SearchAction.OnQueryChange -> {
                _uiState.product { copy(query = action.newQuery) }
            }

            is SearchAction.OnSearch -> {
                //Search
            }
        }
    }
}