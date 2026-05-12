package com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState

@Immutable
data class SearchUiState(
    val query: String = "",
    val isExpanded: Boolean = false,
    val searchHistory: List<String> = emptyList(),
    val searchResults: List<String> = emptyList()
) : UiState