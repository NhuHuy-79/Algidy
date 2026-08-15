package com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.grid_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.InventoryGridList
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.EmptyPage
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.LoadingPage
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryResultState
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodCardUiModel
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun InventoryContent(
    inventoryResultState: InventoryResultState,
    selectedIds: Set<String>,
    onItemClick: (FoodCardUiModel) -> Unit,
    onItemLongClick: (FoodCardUiModel) -> Unit,
    onAddManuallyClick: () -> Unit,
    itemProvider: () -> ImmutableList<FoodCardUiModel>,
) {
    when (inventoryResultState) {
        InventoryResultState.Loading -> {
            LoadingPage(modifier = Modifier.fillMaxSize())
        }

        is InventoryResultState.Empty -> {
            EmptyPage(
                onClick = onAddManuallyClick,
                modifier = Modifier.fillMaxSize()
            )
        }

        is InventoryResultState.Success -> {
            val items = itemProvider()
            if (items.isEmpty()) {
                EmptyPage(
                    onClick = onAddManuallyClick,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                InventoryGridList(
                    items = items,
                    selectedIds = selectedIds,
                    onItemClick = onItemClick,
                    onItemLongClick = onItemLongClick,
                )
            }
        }
    }
}