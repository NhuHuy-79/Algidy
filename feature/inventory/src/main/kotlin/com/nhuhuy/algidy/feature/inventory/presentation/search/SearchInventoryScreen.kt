package com.nhuhuy.algidy.feature.inventory.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.feature.inventory.R
import com.nhuhuy.algidy.feature.inventory.presentation.search.component.SearchContent
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchAction
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchInventoryScreen(
    uiState: SearchUiState,
    onNavigateToDetail: (id: String) -> Unit,
    onBackClick: () -> Unit,
    onAction: (SearchAction) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        SearchBar(
            modifier = Modifier.padding(top = if (uiState.isExpanded) 0.dp else 8.dp),
            expanded = uiState.isExpanded,
            onExpandedChange = { onAction(SearchAction.OnExpandedChange(it)) },
            shape = if (uiState.isExpanded) SearchBarDefaults.fullScreenShape else SearchBarDefaults.inputFieldShape,
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            inputField = {
                SearchBarDefaults.InputField(
                    query = uiState.query,
                    onQueryChange = { query ->
                        onAction(SearchAction.OnQueryChange(query))
                    },
                    onSearch = { search ->
                        onAction(SearchAction.OnSearch(search))
                    },
                    expanded = uiState.isExpanded,
                    onExpandedChange = { expand -> onAction(SearchAction.OnExpandedChange(expand)) },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_screen_placeholder)
                        )
                    },
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                if (uiState.isExpanded) {
                                    onAction(SearchAction.OnExpandedChange(false))
                                } else onBackClick()
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    trailingIcon = {
                        if (uiState.query.isNotEmpty()) {
                            IconButton(onClick = { onAction(SearchAction.OnClearQuery) }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    }
                )
            }
        ) {
            AnimatedVisibility(
                visible = uiState.isLoading
            ) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            SearchContent(
                uiState = uiState,
                onAction = onAction,
                onSearchResultClick = onNavigateToDetail
            )
        }
    }
}
