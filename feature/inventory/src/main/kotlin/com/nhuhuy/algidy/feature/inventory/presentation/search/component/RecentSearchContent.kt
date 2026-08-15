package com.nhuhuy.algidy.feature.inventory.presentation.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.inventory.domain.model.SearchHistory
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun RecentSearchContent(
    modifier: Modifier = Modifier,
    searchHistories: ImmutableList<SearchHistory>,
    onItemClick: (SearchHistory) -> Unit,
) {
    val localSpacing = LocalAlgidySpacing.current
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(localSpacing.extraSmall)
    ) {
        if (searchHistories.isEmpty()) {

        } else {
            item {
                Text(
                    text = stringResource(R.string.search_history_title),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            item {
                Spacer(modifier = Modifier.height(localSpacing.medium))
            }

            itemsIndexed(
                items = searchHistories,
                key = { _, item -> item.id }
            ) { index, item ->
                RecentSearchListItem(
                    modifier = Modifier.fillMaxWidth(),
                    item = item,
                    index = index,
                    count = searchHistories.size,
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun RecentSearchListItem(
    item: SearchHistory,
    index: Int,
    count: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val localShape = LocalAlgidyShapes.current
    SegmentedListItem(
        modifier = modifier,
        onClick = onClick,
        shapes = ListItemDefaults.segmentedShapes(
            index = index,
            count = count,
            defaultShapes = ListItemDefaults.shapes(
                shape = localShape.medium,
                pressedShape = localShape.large
            )
        ),
        leadingContent = { AppIcon(iconProvider = AlgidyIcons.Inventory.LastHistory) },
        trailingContent = { AppIcon(iconProvider = AlgidyIcons.Inventory.HistoryAction) },
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}