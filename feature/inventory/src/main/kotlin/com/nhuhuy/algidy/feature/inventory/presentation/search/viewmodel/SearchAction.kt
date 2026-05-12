package com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel

import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction


sealed interface SearchAction : UiAction {
    data class OnQueryChange(val newQuery: String) : SearchAction
    data class OnSearch(val query: String) : SearchAction
    data class OnExpandedChange(val expanded: Boolean) : SearchAction
    data object OnClearQuery : SearchAction
    data object OnBack : SearchAction
    data class OnHistoryClick(val history: String) : SearchAction
}