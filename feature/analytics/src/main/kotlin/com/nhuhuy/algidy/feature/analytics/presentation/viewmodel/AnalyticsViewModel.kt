package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.navigation.Navigator
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.analytics.domain.model.AnalyticsPeriod
import com.nhuhuy.algidy.feature.analytics.domain.repository.FoodAnalyticsRepository
import com.nhuhuy.algidy.feature.analytics.presentation.model.toUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val foodAnalyticsRepository: FoodAnalyticsRepository,
    private val navigator: Navigator,
) : BaseViewModel<AnalyticsUiState, AnalyticsEvent, AnalyticsAction>() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    override val uiState = _uiState.asStateFlow()

    init {
        loadAnalytics()
    }

    override fun onAction(action: AnalyticsAction) {
        when (action) {
            AnalyticsAction.OnBackClick -> {
                navigator.navigateBack()
            }

            AnalyticsAction.OnRefresh -> {
                loadAnalytics()
            }

            is AnalyticsAction.OnPeriodSelect -> {
                _uiState.product { copy(period = action.period) }
            }
        }
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            val overviewDeferred = async {
                foodAnalyticsRepository.getOverviewStatistic()
            }

            val spoilageWeekDeferred = async {
                foodAnalyticsRepository.getSpoilageStatistic(
                    AnalyticsPeriod.WEEK,
                )
            }

            val spoilageMonthDeferred = async {
                foodAnalyticsRepository.getSpoilageStatistic(
                    AnalyticsPeriod.MONTH,
                )
            }

            val freshnessWeekDeferred = async {
                foodAnalyticsRepository.getFreshnessStatistic(
                    AnalyticsPeriod.WEEK,
                )
            }

            val freshnessMonthDeferred = async {
                foodAnalyticsRepository.getFreshnessStatistic(
                    AnalyticsPeriod.MONTH,
                )
            }

            val overview = overviewDeferred.await()
            val spoilageWeek = spoilageWeekDeferred.await().points.map { it.toUiModel() }
            val spoilageMonth = spoilageMonthDeferred.await().points.map { it.toUiModel() }
            val freshnessWeek = freshnessWeekDeferred.await()
            val freshnessMonth = freshnessMonthDeferred.await()

            _uiState.product {
                copy(
                    expiryCount = overview.expiryCount,
                    expiringSoon = overview.expiringCount,
                    spoilageStatisticByWeekend = spoilageWeek,
                    spoilageStatisticByMonth = spoilageMonth,
                    freshnessStatisticByWeekend = freshnessWeek,
                    freshnessStatisticByMonth = freshnessMonth,
                )
            }
        }
    }
}
