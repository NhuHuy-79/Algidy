package com.nhuhuy.algidy.feature.analytics.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.feature.analytics.presentation.AnalyticsScreen
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsAction
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsEvent
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnalyticsRoute(
    onNavigateBack: () -> Unit
) {
    val viewModel: AnalyticsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            AnalyticsEvent.NavigateBack -> onNavigateBack()
        }
    }

    AnalyticsScreen(
        uiState = uiState,
        onBackPress = { viewModel.onAction(AnalyticsAction.OnBackClick) },
        onAction = viewModel::onAction
    )
}
