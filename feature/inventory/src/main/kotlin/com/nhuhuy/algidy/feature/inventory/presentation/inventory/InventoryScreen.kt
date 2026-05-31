@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhuhuy.algidy.feature.inventory.presentation.inventory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.component.CategoryFilterGroup
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventoryTabRow
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventoryTopBar
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list.InventoryCategoryList
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list.InventoryFoodItem
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.pager.InventoryPager
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryCombineState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryResultState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventorySortMode
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryUiState
import com.nhuhuy.algidy.feature.inventory.utils.GridCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InventoryScreen(
    uiState: InventoryUiState,
    combineState: InventoryCombineState,
    inventoryResultState: InventoryResultState,
    onAction: (InventoryAction) -> Unit,
) {
    val categories = GridCategory.entries.toImmutableList()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                InventoryTopBar(
                    showCategoryEditMode = uiState.showCategoryEdit,
                    isExpiredOnlyActive = uiState.showExpiredOnly,
                    categoryEnabled = combineState.categoryEnabled,
                    currentSortMode = uiState.sortMode,
                    onAction = onAction,
                    scrollBehavior = scrollBehavior
                )

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
                showExpiredOnly = uiState.showExpiredOnly,
                onItemClick = { id -> onAction(InventoryAction.OnItemClick(id)) }
            )
        } else {
            InventoryPager(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                pagerState = pagerState,
                sortMode = uiState.sortMode,
                showExpiredOnly = uiState.showExpiredOnly,
                inventoryResultState = inventoryResultState,
                onItemClick = { id -> onAction(InventoryAction.OnItemClick(id)) }
            )
        }
    }
}

@Composable
fun InventoryGridList(
    items: ImmutableList<FoodItem>,
    onItemClick: (FoodItem) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp
    ) {
        items(
            items = items,
            key = { it.id }
        ) { foodItem ->
            InventoryFoodItem(
                item = foodItem,
                onItemClick = onItemClick,
                modifier = Modifier
                    .animateItem()
            )
        }
    }
}

fun List<FoodItem>.getFilteredAndSortedList(
    category: CategoryUiModel,
    showExpiredOnly: Boolean,
    sortMode: InventorySortMode
): List<FoodItem> {
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
        filteredByCategory.filter { it.getFreshnessStatus() == Freshness.EXPIRED }
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

fun List<FoodItem>.getFilteredAndSortedList(
    pageIndex: Int,
    sortMode: InventorySortMode,
    showExpiredOnly: Boolean
): List<FoodItem> {
    val filteredByLocation = when (pageIndex) {
        0 -> this
        1 -> this.filter { it.location == StorageLocation.FRIDGE }
        2 -> this.filter { it.location == StorageLocation.FREEZER }
        3 -> this.filter { it.location == StorageLocation.PANTRY }
        4 -> this.filter { it.location == StorageLocation.OTHER }
        else -> this
    }

    val filteredByStatus = if (showExpiredOnly) {
        filteredByLocation.filter { it.getFreshnessStatus() == Freshness.EXPIRED }
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
