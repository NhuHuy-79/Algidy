package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

sealed interface AnalyticsAction {
    data object OnRefresh : AnalyticsAction
    data object OnBackClick : AnalyticsAction
}
