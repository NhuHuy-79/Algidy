package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list

import androidx.compose.runtime.Composable
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.getFilteredAndSortedList
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryResultState
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventorySortMode
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.getDataOrEmpty
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodCardUiModel
import kotlinx.collections.immutable.toImmutableList

@Composable
fun InventoryCategoryList(
    currentCategory: CategoryUiModel,
    inventoryResultState: InventoryResultState,
    sortMode: InventorySortMode,
    showExpiredOnly: Boolean,
    selectedIds: Set<String> = emptySet(),
    onItemClick: (FoodCardUiModel) -> Unit,
    onItemLongClick: (FoodCardUiModel) -> Unit,
    onAddManuallyClick: () -> Unit
) {
    InventoryContent(
        inventoryResultState = inventoryResultState,
        selectedIds = selectedIds,
        onItemClick = onItemClick,
        onItemLongClick = onItemLongClick,
        onAddManuallyClick = onAddManuallyClick,
        itemProvider = {
            inventoryResultState.getDataOrEmpty().getFilteredAndSortedList(
                category = currentCategory,
                showExpiredOnly = showExpiredOnly,
                sortMode = sortMode
            ).toImmutableList()
        }
    )
}
