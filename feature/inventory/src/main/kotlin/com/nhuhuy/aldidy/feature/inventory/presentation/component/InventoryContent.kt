@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhuhuy.aldidy.feature.inventory.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nhuhuy.aldidy.feature.inventory.presentation.SampleData.foodList
import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.Freshness
import com.nhuhuy.algidy.core.model.StorageLocation
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InventoryContent(
    categories: List<String>,
    onBackPress: () -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var sortMode by remember { mutableStateOf(InventorySortMode.NONE) }
    var showExpiredOnly by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                InventoryTopBar(
                    onSortByExpiry = {
                        sortMode = InventorySortMode.BY_EXPIRY
                    },
                    onSortByName = {
                        sortMode = InventorySortMode.BY_NAME
                    },
                    onShowExpiredOnly = {
                        showExpiredOnly = !showExpiredOnly
                    },
                    onResetFilters = {
                        sortMode = InventorySortMode.NONE
                        showExpiredOnly = false
                    },
                    isExpiredOnlyActive = showExpiredOnly,
                    currentSortMode = sortMode,
                    onBackPress = onBackPress,
                    scrollBehavior = scrollBehavior
                )
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
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalAlignment = Alignment.Top
        ) { pageIndex ->
            val currentItems = remember(pageIndex, foodList, sortMode, showExpiredOnly) {
                val filteredByLocation = when (pageIndex) {
                    0 -> foodList.filter { it.location == StorageLocation.FRIDGE }
                    1 -> foodList.filter { it.location == StorageLocation.FREEZER }
                    2 -> foodList.filter { it.location == StorageLocation.PANTRY }
                    3 -> foodList.filter { it.location == StorageLocation.OTHER }
                    else -> foodList
                }

                val filteredByStatus = if (showExpiredOnly) {
                    filteredByLocation.filter { it.getFreshnessStatus() == Freshness.EXPIRED }
                } else {
                    filteredByLocation
                }
                when (sortMode) {
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
            InventoryGridList(
                items = currentItems,
                onItemClick = { foodItem -> onItemClick(foodItem.id) },
            )
        }
    }
}

@Composable
fun InventoryGridList(
    items: List<FoodItem>,
    onItemClick: (FoodItem) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    if (items.isEmpty()) {
        EmptyPage(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = contentPadding)
        )
    } else {
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
                InventoryFoodCard(
                    item = foodItem,
                    onClick = { onItemClick(foodItem) },
                    modifier = Modifier.animateItem()
                )
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

enum class InventorySortMode {
    BY_NAME, BY_EXPIRY, NONE
}