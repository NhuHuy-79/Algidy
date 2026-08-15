package com.nhuhuy.algidy.feature.inventory.presentation.search.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchAction
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchUiState
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    onAction: (SearchAction) -> Unit
) {
    if (uiState.query.isEmpty()) {
        RecentSearchContent(
            modifier = modifier,
            searchHistories = uiState.searchHistories.toImmutableList(),
            onItemClick = { history ->
                onAction(SearchAction.OnQueryChange(history.name))
            }
        )
    } else {
        SearchResultContent(
            modifier = modifier,
            searchResults = uiState.searchResults.toImmutableList(),
            onItemClick = { item ->
                onAction(SearchAction.OnSearchResultClick(item))
            }
        )
    }
}