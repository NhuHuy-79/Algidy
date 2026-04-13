package com.nhuhuy.aldidy.feature.inventory.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.aldidy.feature.inventory.presentation.component.InventoryContent
import com.nhuhuy.aldidy.feature.inventory.presentation.viewmodel.InventoryViewModel
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.model.StorageLocation

@Composable
fun InventoryRoute(
    viewModel: InventoryViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReview: (id: String) -> Unit,
) = BoxLayout {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    InventoryContent(
        uiState = uiState,
        categories = StorageLocation.entries.map { location -> location.name },
        onBackPress = onNavigateBack,
        onItemClick = onNavigateToReview
    )
}