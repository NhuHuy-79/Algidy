package com.nhuhuy.algidy.feature.analytics.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.feature.analytics.presentation.AnalyticsScreen
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsAction
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsEvent
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnalyticsRoute(
    viewModel: AnalyticsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveEffect(viewModel.events) { event ->
        when (event) {
            AnalyticsEvent.NavigateBack -> onNavigateBack()
        }
    }

    AnalyticsScreen(
        uiState = uiState,
        onBackPress = { viewModel.onAction(AnalyticsAction.OnBackClick) }
    )
}
