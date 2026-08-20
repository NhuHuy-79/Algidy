package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryResultState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventorySortMode
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.getDataOrEmpty
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel
import com.nhuhuy.algidy.feature.inventory.utils.GridCategory
import com.nhuhuy.algidy.feature.inventory.utils.getFilteredAndSortedList
import com.nhuhuy.algidy.feature.inventory.utils.toStringRes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList

@Composable
fun InventoryPager(
    pagerState: PagerState,
    sortMode: InventorySortMode,
    showExpiredOnly: Boolean,
    selectedIds: ImmutableSet<String>,
    inventoryResultState: InventoryResultState,
    onItemClick: (item: FoodUiModel) -> Unit,
    onItemLongClick: (item: FoodUiModel) -> Unit,
    onAddManuallyClick: () -> Unit,
    onFabVisibilityChange: (Boolean) -> Unit,
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        InventoryGridContent(
            inventoryResultState = inventoryResultState,
            selectedIds = selectedIds,
            onItemClick = onItemClick,
            onItemLongClick = onItemLongClick,
            onAddManuallyClick = onAddManuallyClick,
            itemProvider = {
                inventoryResultState.getDataOrEmpty()
                    .getFilteredAndSortedList(
                        pageIndex = page,
                        sortMode = sortMode,
                        showExpiredOnly = showExpiredOnly
                    ).toImmutableList()
            },
            onScroll = onFabVisibilityChange
        )
    }
}

@Composable
fun InventoryTabRow(
    modifier: Modifier = Modifier,
    categories: ImmutableList<GridCategory>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    PrimaryScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = Color.Transparent,
        divider = {},
        edgePadding = 0.dp
    ) {
        categories.forEachIndexed { index, category ->
            val selected = selectedTabIndex == index
            Tab(
                modifier = Modifier.animateContentSize(),
                selected = selected,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = stringResource(category.toStringRes()),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                    )
                }
            )
        }
    }
}