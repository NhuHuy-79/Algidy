package com.nhuhuy.algidy.feature.analytics.presentation.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.presentation.preview.MarketPreview
import com.nhuhuy.algidy.core.presentation.preview.StoreMarketingScreen
import com.nhuhuy.algidy.feature.analytics.presentation.AnalyticsScreen

@Composable
@MarketPreview
fun AnalyticsScreenPreview() {
    AlgidyTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        StoreMarketingScreen(
            headlineLines = listOf(
                "Consumption Report"
            ),
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            textColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            AnalyticsScreen(
                uiState = FakeData.mockAnalyticsUiState,
                onAction = {}
            )
        }
    }
}