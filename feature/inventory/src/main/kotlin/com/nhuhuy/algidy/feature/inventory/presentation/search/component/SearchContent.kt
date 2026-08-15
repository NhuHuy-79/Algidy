package com.nhuhuy.algidy.feature.inventory.presentation.search.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.FoodImageCard
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchAction
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchUiState
import com.nhuhuy.algidy.toReadableText

@Composable
fun SearchContent(
    uiState: SearchUiState,
    onSearchResultClick: (id: String) -> Unit,
    onAction: (SearchAction) -> Unit
) {
    if (uiState.query.isEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.search_history_title),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                )
            }
            items(uiState.searchHistory) { historyItem ->
                ListItem(
                    headlineContent = { Text(historyItem) },
                    leadingContent = { Icon(Icons.Default.History, contentDescription = null) },
                    modifier = Modifier.clickable {
                        onAction(
                            SearchAction.OnHistoryClick(
                                historyItem
                            )
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            if (uiState.searchResults.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.search_no_results),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            items(uiState.searchResults) { result ->
                ListItem(
                    headlineContent = {
                        Text(
                            text = result.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    supportingContent = {
                        Text(
                            text = result.location.name.capitalize(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    trailingContent = {
                        Text(
                            text = result.expiryDate.toReadableText(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    leadingContent = {
                        FoodImageCard(
                            modifier = Modifier.size(36.dp),
                            imageUri = result.imageUri
                        )

                    },
                    modifier = Modifier.clickable {
                        onSearchResultClick(result.id)
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }
    }
}