package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list

import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
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
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodCardUiModel
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
    onItemClick: (item: FoodCardUiModel) -> Unit,
    onItemLongClick: (item: FoodCardUiModel) -> Unit,
    onAddManuallyClick: () -> Unit,
) {
    InventoryGridContent(
        inventoryResultState = inventoryResultState,
        selectedIds = selectedIds,
        onItemClick = onItemClick,
        onItemLongClick = onItemLongClick,
        onAddManuallyClick = onAddManuallyClick,
        itemProvider = {
            inventoryResultState.getDataOrEmpty().getFilteredAndSortedList(
                pageIndex = pagerState.currentPage,
                sortMode = sortMode,
                showExpiredOnly = showExpiredOnly
            ).toImmutableList()
        }
    )
}

@Composable
fun InventoryTabRow(
    categories: ImmutableList<GridCategory>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    SecondaryScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = Color.Transparent,
        divider = {},
        edgePadding = 0.dp
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = stringResource(category.toStringRes()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selectedTabIndex == index)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        fontWeight = if (selectedTabIndex == index) FontWeight.Medium else FontWeight.Normal
                    )
                }
            )
        }
    }
}