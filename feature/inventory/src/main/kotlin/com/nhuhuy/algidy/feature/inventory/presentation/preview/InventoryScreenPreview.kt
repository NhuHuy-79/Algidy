package com.nhuhuy.algidy.feature.inventory.presentation.preview


import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.presentation.preview.MarketPreview
import com.nhuhuy.algidy.core.presentation.preview.StoreMarketingScreen
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.InventoryFabGroup
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.InventoryScreen
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryOverlay

@Composable
@MarketPreview
private fun InventoryScreenPreview() {
    AlgidyTheme(
        dynamicColor = false
    ) {
        StoreMarketingScreen(
            headlineLines = listOf(
                "Never miss",
                "an expiry date"
            ),
            fabContent = {
                InventoryFabGroup(
                    visible = true,
                    onCameraClick = {},
                    onAddClick = {}
                )
            }
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                InventoryScreen(
                    uiState = FakeData.uiState.copy(
                        overlay = InventoryOverlay.AddFoodBottomSheet(null)
                    ),
                    combineState = FakeData.combineState,
                    inventoryResultState = FakeData.resultState,
                    onAction = {}
                )
            }
        }
    }
}