@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhuhuy.algidy.feature.inventory.presentation.inventory

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.CategoryFilterGroup
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventorySelectBar
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventoryTabRow
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventoryTopBar
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list.InventoryCategoryList
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list.InventoryFoodGridItem
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list.InventoryPager
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryCombineState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryResultState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventorySelectAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventorySortMode
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryUiState
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodCardUiModel
import com.nhuhuy.algidy.feature.inventory.utils.GridCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun InventoryScreen(
    uiState: InventoryUiState,
    combineState: InventoryCombineState,
    inventoryResultState: InventoryResultState,
    onAction: (InventoryAction) -> Unit,
) {
    val categories = GridCategory.entries.toImmutableList()
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()
    val localSpacing = LocalAlgidySpacing.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                AnimatedContent(
                    targetState = uiState.isSelectMode
                ) { selectedMode ->
                    if (!selectedMode) {
                        InventoryTopBar(
                            title = stringResource(R.string.inventory_title),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    } else {
                        InventorySelectBar(
                            selectedCount = uiState.selectedFoodIds.size,
                            onAction = onAction
                        )
                    }
                }

                Spacer(modifier = Modifier.height(localSpacing.large))

                if (combineState.categoryEnabled) {
                    CategoryFilterGroup(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        selectedCategory = uiState.currentCategory,
                        categories = combineState.categories.toImmutableList(),
                        onCategoryClick = { onAction(InventoryAction.OnCategorySelect(it)) }
                    )
                } else {
                    InventoryTabRow(
                        categories = categories,
                        selectedTabIndex = pagerState.currentPage,
                        onTabSelected = { index ->
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background.copy(
            alpha = if (uiState.expanded) 0.2f else 1f
        ),
    ) { paddingValues ->
        if (combineState.categoryEnabled) {
            InventoryCategoryList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                currentCategory = uiState.currentCategory,
                inventoryResultState = inventoryResultState,
                sortMode = uiState.sortMode,
                selectedIds = uiState.selectedFoodIds,
                showExpiredOnly = uiState.showExpiredOnly,
                onItemClick = { item ->
                    if (uiState.isSelectMode) {
                        onAction(InventorySelectAction.OnClick(item.id))
                    } else {
                        onAction(InventoryAction.OnItemClick(item))
                    }
                },
                onAddManuallyClick = {
                    onAction(InventoryAction.OnEmptyPageClick)
                },
                onItemLongClick = { item -> onAction(InventorySelectAction.OnLongClick(id = item.id)) }
            )
        } else {
            InventoryPager(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                pagerState = pagerState,
                sortMode = uiState.sortMode,
                selectedIds = uiState.selectedFoodIds,
                showExpiredOnly = uiState.showExpiredOnly,
                inventoryResultState = inventoryResultState,
                onItemClick = { item ->
                    if (uiState.isSelectMode) {
                        onAction(InventorySelectAction.OnClick(item.id))
                    } else {
                        onAction(InventoryAction.OnItemClick(item))
                    }
                },
                onAddManuallyClick = { onAction(InventoryAction.OnEmptyPageClick) },
                onItemLongClick = { item -> onAction(InventorySelectAction.OnLongClick(item.id)) }
            )
        }
    }
}

@Composable
fun InventoryGridList(
    modifier: Modifier = Modifier,
    items: ImmutableList<FoodCardUiModel>,
    selectedIds: Set<String> = emptySet(),
    onItemClick: (FoodCardUiModel) -> Unit,
    onItemLongClick: (FoodCardUiModel) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp
    ) {
        items(
            items = items,
            key = { it.id }
        ) { foodItem ->
            val localShape = LocalAlgidyShapes.current
            InventoryFoodGridItem(
                item = foodItem,
                isSelected = foodItem.id in selectedIds,
                modifier = Modifier
                    .animateItem()
                    .clip(localShape.large)
                    .combinedClickable(
                        enabled = true,
                        onClick = { onItemClick(foodItem) },
                        onLongClick = { onItemLongClick(foodItem) }
                    )
            )
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

fun List<FoodCardUiModel>.getFilteredAndSortedList(
    category: CategoryUiModel,
    showExpiredOnly: Boolean,
    sortMode: InventorySortMode
): List<FoodCardUiModel> {
    val filteredByCategory = when (category) {
        CategoryUiModel.All -> this
        is CategoryUiModel.ByCategory -> this.filter {
            it.categoryId == category.data.id
        }

        CategoryUiModel.Uncategorized -> this.filter {
            it.categoryId == null
        }
    }

    val filteredByStatus = if (showExpiredOnly) {
        filteredByCategory.filter { it.freshness == Freshness.EXPIRED }
    } else {
        filteredByCategory
    }

    return when (sortMode) {
        InventorySortMode.BY_NAME -> {
            filteredByStatus.sortedBy { it.name.lowercase() }
        }

        InventorySortMode.BY_EXPIRY -> {
            filteredByStatus.sortedBy { it.expiryDate }
        }

        InventorySortMode.NONE -> {
            filteredByStatus.sortedByDescending { it.purchaseDate }
        }
    }
}

fun List<FoodCardUiModel>.getFilteredAndSortedList(
    pageIndex: Int,
    sortMode: InventorySortMode,
    showExpiredOnly: Boolean
): List<FoodCardUiModel> {
    val filteredByLocation = when (pageIndex) {
        0 -> this
        1 -> this.filter { it.location == StorageLocation.FRIDGE }
        2 -> this.filter { it.location == StorageLocation.FREEZER }
        3 -> this.filter { it.location == StorageLocation.PANTRY }
        4 -> this.filter { it.location == StorageLocation.OTHER }
        else -> this
    }

    val filteredByStatus = if (showExpiredOnly) {
        filteredByLocation.filter { it.freshness == Freshness.EXPIRED }
    } else {
        filteredByLocation
    }

    return when (sortMode) {
        InventorySortMode.BY_NAME -> {
            filteredByStatus.sortedBy { it.name.lowercase() }
        }

        InventorySortMode.BY_EXPIRY -> {
            filteredByStatus.sortedBy { it.expiryDate }
        }

        InventorySortMode.NONE -> {
            filteredByStatus.sortedByDescending { it.purchaseDate }
        }
    }
}
