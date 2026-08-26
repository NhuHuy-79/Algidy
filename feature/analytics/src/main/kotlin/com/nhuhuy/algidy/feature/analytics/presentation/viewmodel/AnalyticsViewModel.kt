package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.navigation.Navigator
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.analytics.domain.mapper.toFreshnessStatistic
import com.nhuhuy.algidy.feature.analytics.domain.mapper.toOverviewStatistic
import com.nhuhuy.algidy.feature.analytics.domain.mapper.toSpoilageStatistic
import com.nhuhuy.algidy.feature.analytics.domain.mapper.toWeeklyExpiryStatistic
import com.nhuhuy.algidy.feature.analytics.domain.model.AnalyticsPeriod
import com.nhuhuy.algidy.feature.analytics.domain.repository.FoodAnalyticsRepository
import com.nhuhuy.algidy.feature.analytics.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AnalyticsViewModel(
    private val foodAnalyticsRepository: FoodAnalyticsRepository,
    private val navigator: Navigator,
) : BaseViewModel<AnalyticsUiState, AnalyticsEvent, AnalyticsAction>() {

    private val _period = MutableStateFlow(AnalyticsPeriod.WEEK)
    private val _uiState = MutableStateFlow(AnalyticsUiState())
    override val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        observeAnalyticsStatistic()
    }

    override fun onAction(action: AnalyticsAction) {
        when (action) {
            AnalyticsAction.OnBackClick -> {
                navigator.navigateBack()
            }

            AnalyticsAction.OnRefresh -> {
                // Flow based, updates automatically.
            }

            is AnalyticsAction.OnPeriodSelect -> {
                _uiState.product { copy(period = action.period) }
            }

            AnalyticsAction.OnSpoilageChartHide -> {
                /* _uiState.product { copy(wastedValue = 0, consumedValue = 0) }*/
            }

            is AnalyticsAction.OnSpoilageChartPressed -> {
                /*_uiState.product {
                    copy(wastedValue = action.wasted, consumedValue = action.consumed)
                }*/
            }
        }
    }

    private fun observeAnalyticsStatistic() {
        foodAnalyticsRepository.observeAllFoodItems()
            .onEach { foodItems ->
                val overview = foodItems.toOverviewStatistic()
                val spoilageMonth = foodItems.toSpoilageStatistic(AnalyticsPeriod.MONTH)
                val freshnessMonth = foodItems.toFreshnessStatistic(AnalyticsPeriod.MONTH)
                val expiryStatistic = foodItems.toWeeklyExpiryStatistic()

                _uiState.product {
                    copy(
                        consumedValue = spoilageMonth.points.sumOf { it.consumed },
                        wastedValue = spoilageMonth.points.sumOf { it.waste },
                        expiryCount = overview.expiryCount,
                        expiringSoon = overview.expiringCount,
                        spoilageStatisticByMonth = spoilageMonth.points.map { it.toUiModel() },
                        freshnessStatisticByMonth = freshnessMonth,
                        weeklyExpiryStatistic = expiryStatistic.toUiModel()
                    )
                }
            }
            .launchIn(viewModelScope)

    }
}
