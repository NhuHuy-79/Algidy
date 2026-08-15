package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.InventoryGridList
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.EmptyPage
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.LoadingPage
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.getFilteredAndSortedList
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryResultState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventorySortMode
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodCardUiModel
import kotlinx.collections.immutable.toImmutableList

@Composable
fun InventoryCategoryList(
    modifier: Modifier = Modifier,
    currentCategory: CategoryUiModel,
    inventoryResultState: InventoryResultState,
    sortMode: InventorySortMode,
    showExpiredOnly: Boolean,
    selectedIds: Set<String> = emptySet(),
    onItemClick: (FoodCardUiModel) -> Unit,
    onItemLongClick: (FoodCardUiModel) -> Unit,
    onAddManuallyClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        when (inventoryResultState) {
            InventoryResultState.Loading -> LoadingPage(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
            )

            is InventoryResultState.Empty -> EmptyPage(
                onClick = onAddManuallyClick,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
            )

            is InventoryResultState.Success -> {
                val items = remember(sortMode, showExpiredOnly, currentCategory) {
                    inventoryResultState.items.getFilteredAndSortedList(
                        category = currentCategory,
                        sortMode = sortMode,
                        showExpiredOnly = showExpiredOnly
                    )
                }.toImmutableList()

                if (items.isEmpty()) {
                    EmptyPage(
                        onClick = onAddManuallyClick,
                        modifier = Modifier.fillMaxSize()
                    )
                } else InventoryGridList(
                    items = items,
                    selectedIds = selectedIds,
                    onItemLongClick = onItemLongClick,
                    onItemClick = { foodItem ->
                        onItemClick(foodItem)
                    }
                )
            }
        }
    }
}
