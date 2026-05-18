package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.analytics.domain.usecase.GetDailyFreshnessStatsUseCase
import com.nhuhuy.algidy.feature.analytics.domain.usecase.GetSummaryStatsUseCase
import com.nhuhuy.algidy.feature.analytics.domain.usecase.GetWastedByCategoryUseCase
import com.nhuhuy.algidy.feature.analytics.domain.usecase.GetWeeklySpoilageHistoryUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the Analytics screen.
 * Orchestrates UI state by combining multiple domain UseCases and mapping them to UI models.
 */
class AnalyticsViewModel(
    getSummaryStatsUseCase: GetSummaryStatsUseCase,
    getWastedByCategoryUseCase: GetWastedByCategoryUseCase,
    getDailyFreshnessStatsUseCase: GetDailyFreshnessStatsUseCase,
    getWeeklySpoilageHistoryUseCase: GetWeeklySpoilageHistoryUseCase
) : BaseViewModel<AnalyticsUiState, AnalyticsEvent, AnalyticsAction>() {

    override val uiState: StateFlow<AnalyticsUiState> = combine(
        getSummaryStatsUseCase(),
        getWastedByCategoryUseCase(),
        getDailyFreshnessStatsUseCase(),
        getWeeklySpoilageHistoryUseCase()
    ) { summary, waste, freshness, history ->
        // Business logic mapping to UI state happens here in the ViewModel
        AnalyticsUiState(
            weeklyFoodItemsCount = summary.weeklyCount,
            wastedCount = summary.wastedCount,
            consumedCount = summary.consumedCount,
            otherCount = summary.otherCount,
            expiryChartUiModel = freshness.toExpiryChartUiModel(),
            spoilageChartUiModel = history.toSpoilageChartUiModel(),
            wastedByCategory = waste.map { model ->
                model.toCategoryWastedUiModel()
            },
            isLoading = false
        )
    }
        .onStart { emit(AnalyticsUiState(isLoading = true)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AnalyticsUiState(isLoading = true)
        )

    override fun onAction(action: AnalyticsAction) {
        when (action) {
            AnalyticsAction.OnBackClick -> {
                viewModelScope.launch {
                    emitEvent(AnalyticsEvent.NavigateBack)
                }
            }

            AnalyticsAction.OnRefresh -> {
                // Flow combination will automatically trigger updates when underlying data changes.
            }
        }
    }
}
