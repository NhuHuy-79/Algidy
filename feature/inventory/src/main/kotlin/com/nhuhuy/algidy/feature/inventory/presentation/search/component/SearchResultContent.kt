@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.nhuhuy.algidy.feature.inventory.presentation.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.component.FoodImage
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodCardUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.model.readableRemainDays
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun SearchResultContent(
    modifier: Modifier = Modifier,
    searchResults: ImmutableList<FoodCardUiModel>,
    onItemClick: (item: FoodCardUiModel) -> Unit
) {
    val localSpacing = LocalAlgidySpacing.current
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(localSpacing.extraSmall)
    ) {
        itemsIndexed(
            items = searchResults,
            key = { _, item -> item.id }
        ) { index, item ->
            SearchResultListItem(
                modifier = Modifier.fillMaxWidth(),
                item = item,
                index = index,
                count = searchResults.size,
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
private fun SearchResultListItem(
    index: Int,
    count: Int,
    item: FoodCardUiModel,
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
        leadingContent = {
            FoodImage(
                imageUrl = item.imageUri,
                modifier = Modifier
                    .size(36.dp)
                    .clip(localShape.large)
            )
        },
        trailingContent = {
            AppIcon(iconProvider = AlgidyIcons.Inventory.HistoryAction)
        },
        supportingContent = {
            Text(
                text = readableRemainDays(remainingDays = item.remainDays),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}