package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction

sealed interface AnalyticsAction : UiAction {
    data object OnRefresh : AnalyticsAction
    data object OnBackClick : AnalyticsAction
}
