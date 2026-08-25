package com.nhuhuy.algidy.feature.analytics.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.feature.analytics.presentation.AnalyticsScreen
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsAction
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsViewModel
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnalyticsRoute() {
    val viewModel: AnalyticsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    LifecycleResumeEffect(Unit) {
        Timber.tag("Analytic Route").d("Trigger Analytics")
        onAction(AnalyticsAction.OnRefresh)
        onPauseOrDispose { }
    }

    AnalyticsScreen(
        uiState = uiState,
        onBackPress = { viewModel.onAction(AnalyticsAction.OnBackClick) },
        onAction = onAction
    )
}
