package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.pager

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.InventoryGridList
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.EmptyPage
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.LoadingPage
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.getFilteredAndSortedList
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryResultState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventorySortMode
import kotlinx.collections.immutable.toImmutableList

@Composable
fun InventoryPager(
    pagerState: PagerState,
    sortMode: InventorySortMode,
    showExpiredOnly: Boolean,
    modifier: Modifier = Modifier,
    inventoryResultState: InventoryResultState,
    onItemClick: (id: String) -> Unit
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) { pageIndex ->
        when (inventoryResultState) {
            InventoryResultState.Loading -> LoadingPage(
                modifier = Modifier.fillMaxSize()
            )

            is InventoryResultState.Empty -> EmptyPage(
                modifier = Modifier.fillMaxSize()
            )

            is InventoryResultState.Success -> {
                val items = remember(pageIndex, sortMode, showExpiredOnly) {
                    inventoryResultState.items.getFilteredAndSortedList(
                        pageIndex,
                        sortMode,
                        showExpiredOnly
                    )
                }.toImmutableList()

                if (items.isEmpty()) {
                    EmptyPage(modifier = Modifier.fillMaxSize())
                } else InventoryGridList(
                    items = items,
                    onItemClick = { foodItem ->
                        onItemClick(foodItem.id)
                    }
                )
            }
        }
    }
}