package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction

sealed interface AnalyticsAction : UiAction {
    data class OnSpoilageChartPressed(val consumed: Int, val wasted: Int) : AnalyticsAction
    data object OnSpoilageChartHide : AnalyticsAction
}
