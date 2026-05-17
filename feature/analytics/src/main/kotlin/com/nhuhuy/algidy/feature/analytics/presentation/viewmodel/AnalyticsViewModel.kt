package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    repository: AnalyticsRepository
) : BaseViewModel<AnalyticsUiState, AnalyticsEvent, AnalyticsAction>() {

    override val uiState: StateFlow<AnalyticsUiState> = repository.getAnalyticsStats()
        .map { stats ->
            AnalyticsUiState(
                weeklyFoodItemsCount = stats.weeklyCount,
                wastedCount = stats.wastedCount,
                consumedCount = stats.consumedCount,
                otherCount = stats.otherCount,
                expiryChartUiModel = stats.dailyStats.toExpiryChartUiModel(),
                spoilageChartUiModel = SpoilageChartUiModel(
                    wastedValues = stats.spoilageHistory.wastedByWeek,
                    consumedValues = stats.spoilageHistory.consumedByWeek,
                    labels = stats.spoilageHistory.weekLabels
                ),
                wastedByCategory = stats.wastedByCategory.map {
                    CategoryWasteUiModel(
                        location = it.location,
                        label = it.location.name.lowercase().replaceFirstChar { char -> char.uppercase() },
                        percentage = it.percentage
                    )
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
                // Flow will automatically refresh when DB changes
            }
        }
    }
}
