package com.nhuhuy.algidy.feature.analytics.presentation.navigation

import androidx.compose.runtime.Composable
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.feature.analytics.presentation.AnalyticsScreen

@Composable
fun AnalyticsRoute(
    onNavigateBack: () -> Unit
) {
    BoxLayout {
        AnalyticsScreen(
            onBackPress = onNavigateBack
        )
    }
}