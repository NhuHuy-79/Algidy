@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhuhuy.algidy.feature.inventory.presentation.inventory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventoryCategoryFilter
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventoryTabRow
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventoryTopBar
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list.InventoryCategoryList
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list.InventoryFoodItem
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.pager.InventoryPager
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.model.CategoryUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryResultState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryUiState
import com.nhuhuy.algidy.feature.inventory.utils.GridCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InventoryScreen(
    uiState: InventoryUiState,
    categoryEnabled: Boolean,
    inventoryResultState: InventoryResultState,
    onCategorySelect: (CategoryUiModel) -> Unit = {},
    onSearchClick: () -> Unit,
    onItemClick: (String) -> Unit,
) {
    val categories = GridCategory.entries.toImmutableList()
    var expanded by remember { mutableStateOf(false) }
    var sortMode by remember { mutableStateOf(InventorySortMode.NONE) }
    var showExpiredOnly by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Column {
                    InventoryTopBar(
                        onSearchClick = onSearchClick,
                        onSortByExpiry = { sortMode = InventorySortMode.BY_EXPIRY },
                        onSortByName = { sortMode = InventorySortMode.BY_NAME },
                        onShowExpiredOnly = { showExpiredOnly = !showExpiredOnly },
                        onResetFilters = {
                            sortMode = InventorySortMode.NONE
                            showExpiredOnly = false
                        },
                        isExpiredOnlyActive = showExpiredOnly,
                        currentSortMode = sortMode,
                        scrollBehavior = scrollBehavior
                    )

                    if (categoryEnabled) {
                        InventoryCategoryFilter(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            selectedCategory = uiState.currentCategory,
                            categories = emptyList(),
                            onCategoryClick = onCategorySelect
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
                alpha = if (expanded) 0.2f else 1f
            ),
        ) { paddingValues ->
            if (categoryEnabled) {
                InventoryCategoryList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    currentCategory = uiState.currentCategory,
                    inventoryResultState = inventoryResultState,
                    sortMode = sortMode,
                    showExpiredOnly = showExpiredOnly,
                    onItemClick = onItemClick
                )
            } else {
                InventoryPager(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    pagerState = pagerState,
                    sortMode = sortMode,
                    showExpiredOnly = showExpiredOnly,
                    inventoryResultState = inventoryResultState,
                    onItemClick = onItemClick
                )
            }
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

enum class InventorySortMode {
    BY_NAME, BY_EXPIRY, NONE
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
