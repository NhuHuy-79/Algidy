package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

sealed interface AnalyticsEvent {
    data object NavigateBack : AnalyticsEvent
}
