package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState
import com.nhuhuy.algidy.feature.analytics.domain.model.AnalyticsPeriod
import com.nhuhuy.algidy.feature.analytics.domain.model.FreshnessStatistic
import com.nhuhuy.algidy.feature.analytics.presentation.model.SpoilagePointUiModel

@Immutable
data class AnalyticsUiState(
    val period: AnalyticsPeriod = AnalyticsPeriod.WEEK,
    val expiryCount: Int = 0,
    val expiringSoon: Int = 0,
    val spoilageStatisticByWeekend: List<SpoilagePointUiModel> = emptyList(),
    val spoilageStatisticByMonth: List<SpoilagePointUiModel> = emptyList(),
    val freshnessStatisticByMonth: FreshnessStatistic = FreshnessStatistic(),
    val freshnessStatisticByWeekend: FreshnessStatistic = FreshnessStatistic(),
) : UiState