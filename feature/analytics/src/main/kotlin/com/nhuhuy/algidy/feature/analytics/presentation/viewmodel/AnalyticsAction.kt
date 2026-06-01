package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction

sealed interface AnalyticsAction : UiAction {
    data class OnChartDataSelect(val dataChartData: CircularChartData) : AnalyticsAction
    data object OnRefresh : AnalyticsAction
    data object OnBackClick : AnalyticsAction
}
