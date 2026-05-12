package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    repository: AnalyticsRepository
) : ViewModel() {

    private val _events = Channel<AnalyticsEvent>()
    val events = _events.receiveAsFlow()

    val uiState = repository.getAnalyticsStats()
        .map { stats ->
            AnalyticsUiState(
                wastedCount = stats.wastedCount,
                consumedCount = stats.consumedCount,
                expiryChartUiModel = stats.dailyStats.toExpiryChartUiModel(),
                wastedByCategory = stats.wastedByCategory.map {
                    CategoryWasteUiModel(
                        location = it.location,
                        label = it.location.name.capitalize(),
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

    fun onAction(action: AnalyticsAction) {
        when (action) {
            AnalyticsAction.OnBackClick -> {
                viewModelScope.launch {
                    _events.send(AnalyticsEvent.NavigateBack)
                }
            }

            AnalyticsAction.OnRefresh -> {
                // Flow will automatically refresh when DB changes
            }
        }
    }
}
