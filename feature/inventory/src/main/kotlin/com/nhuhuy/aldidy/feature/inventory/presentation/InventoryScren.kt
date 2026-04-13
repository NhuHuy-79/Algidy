package com.nhuhuy.aldidy.feature.inventory.presentation

import androidx.compose.runtime.Composable
import com.nhuhuy.aldidy.feature.inventory.presentation.component.InventoryContent
import com.nhuhuy.aldidy.feature.inventory.presentation.viewmodel.InventoryUiState
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.model.StorageLocation

@Composable
fun InventoryScreen(
    uiState: InventoryUiState,
) = BoxLayout {
    InventoryContent(
        categories = StorageLocation.entries.map { location -> location.name },
        onBackPress = {},
        onFilterPress = {},
        onItemClick = {}
    )
}