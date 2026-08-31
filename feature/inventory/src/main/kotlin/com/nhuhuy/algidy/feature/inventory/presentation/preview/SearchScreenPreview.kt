package com.nhuhuy.algidy.feature.inventory.presentation.preview

import androidx.compose.runtime.Composable
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.presentation.preview.MarketPreview
import com.nhuhuy.algidy.core.presentation.preview.StoreMarketingScreen
import com.nhuhuy.algidy.feature.inventory.presentation.search.SearchInventoryScreen
import kotlinx.collections.immutable.toImmutableList

@Composable
@MarketPreview
fun SearchScreenPreview() {
    AlgidyTheme {
        StoreMarketingScreen(
            headlineLines = listOf(
                "Find what",
                "you need"
            ),
            bottomBarVisible = false
        ) {
            SearchInventoryScreen(
                foodItems = FakeData.items.toImmutableList(),
                uiState = FakeData.fakeSearchUiState,
                onAction = {}
            )
        }
    }
}