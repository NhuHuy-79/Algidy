package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction
import com.nhuhuy.algidy.feature.analytics.domain.model.AnalyticsPeriod

sealed interface AnalyticsAction : UiAction {
    data class OnPeriodSelect(val period: AnalyticsPeriod) : AnalyticsAction
    data object OnRefresh : AnalyticsAction
    data object OnBackClick : AnalyticsAction
}
