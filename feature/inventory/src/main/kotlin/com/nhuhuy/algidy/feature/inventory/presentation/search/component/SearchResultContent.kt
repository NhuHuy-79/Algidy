@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.nhuhuy.algidy.feature.inventory.presentation.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.FoodImage
import com.nhuhuy.algidy.core.presentation.utils.toItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.model.readableRemainDays
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun SearchResultContent(
    modifier: Modifier = Modifier,
    searchResults: ImmutableList<FoodUiModel>,
    onItemClick: (item: FoodUiModel) -> Unit
) {
    val localSpacing = LocalAlgidySpacing.current
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (searchResults.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.search_result_title),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item { Spacer(modifier = Modifier.height(localSpacing.extraSmall)) }

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
    item: FoodUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val localShape = LocalAlgidyShapes.current
    ListItem(
        modifier = modifier,
        onClick = onClick,
        shapes = ListItemDefaults.shapes(
            shape = index.toItemPosition(count).toVerticalSegmentedShape(),
            pressedShape = localShape.extraLarge
        ),
        verticalAlignment = Alignment.CenterVertically,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        leadingContent = {
            FoodImage(
                imageUrl = item.imageUri,
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialShapes.Square.toShape())
            )
        },
        trailingContent = {
            AppIcon(iconProvider = AlgidyIcons.Inventory.HistoryAction)
        },
        supportingContent = {
            Text(
                text = readableRemainDays(remainingDays = item.remainDays),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}