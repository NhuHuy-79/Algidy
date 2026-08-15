package com.nhuhuy.algidy.feature.inventory.presentation.search.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchAction
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchUiState
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SearchContent(
    uiState: SearchUiState,
    onAction: (SearchAction) -> Unit
) {
    if (uiState.query.isEmpty()) {
        RecentSearchContent(
            modifier = Modifier.fillMaxWidth(),
            searchHistories = uiState.searchHistories.toImmutableList(),
            onItemClick = { history ->
                onAction(SearchAction.OnQueryChange(history.name))
            }
        )
    } else {
        SearchResultContent(
            modifier = Modifier.fillMaxWidth(),
            searchResults = uiState.searchResults.toImmutableList(),
            onItemClick = { item ->
                onAction(SearchAction.OnSearchResultClick(item))
            }
        )
    }
}