package com.nhuhuy.algidy.feature.inventory.presentation.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.inventory.domain.model.SearchHistory
import com.nhuhuy.segmented_shape.lazy_list.segmentedItems
import com.nhuhuy.segmented_shape.toSegmentedShape
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
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        if (searchHistories.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.search_history_title),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }

        item {
            Spacer(modifier = Modifier.height(localSpacing.extraSmall))
        }

        segmentedItems(
            items = searchHistories,
            key = { _, item -> item.id }
        ) { item, position ->
            RecentSearchListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                item = item,
                shape = position.toSegmentedShape(),
                onClick = { onItemClick(item) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun RecentSearchListItem(
    item: SearchHistory,
    modifier: Modifier = Modifier,
    shape: Shape,
    onClick: () -> Unit
) {
    val localShape = LocalAlgidyShapes.current
    ListItem(
        modifier = modifier,
        onClick = onClick,
        shapes = ListItemDefaults.shapes(
            shape = shape,
            pressedShape = localShape.extraLarge
        ),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        leadingContent = { AppIcon(iconProvider = AlgidyIcons.Inventory.LastHistory) },
        trailingContent = { AppIcon(iconProvider = AlgidyIcons.Inventory.HistoryAction) },
    ) {
        Text(
            text = item.name.lowercase(),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}