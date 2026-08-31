package com.nhuhuy.algidy.feature.analytics.presentation.preview

import com.nhuhuy.algidy.feature.analytics.domain.model.FreshnessStatistic
import com.nhuhuy.algidy.feature.analytics.presentation.model.SpoilagePointUiModel
import com.nhuhuy.algidy.feature.analytics.presentation.model.WeeklyExpiryStatisticUiModel
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsUiState

internal object FakeData {
    val mockSpoilagePoints = listOf(
        SpoilagePointUiModel(label = "1 Th01", waste = 2, consumed = 14),
        SpoilagePointUiModel(label = "8 Th01", waste = 1, consumed = 18),
        SpoilagePointUiModel(label = "15 Th01", waste = 4, consumed = 12),
        SpoilagePointUiModel(label = "22 Th01", waste = 0, consumed = 16),
        SpoilagePointUiModel(label = "29 Th01", waste = 3, consumed = 15),
        SpoilagePointUiModel(label = "5 Th02", waste = 1, consumed = 20),
    )

    val mockWeeklyExpiry = listOf(
        WeeklyExpiryStatisticUiModel(label = "Mon", count = 1),
        WeeklyExpiryStatisticUiModel(label = "Tue", count = 3),
        WeeklyExpiryStatisticUiModel(label = "Wed", count = 2),
        WeeklyExpiryStatisticUiModel(label = "Thu", count = 0),
        WeeklyExpiryStatisticUiModel(label = "Fri", count = 5),
        WeeklyExpiryStatisticUiModel(label = "Sat", count = 4),
        WeeklyExpiryStatisticUiModel(label = "Sun", count = 2),
    )

    val mockFreshness = FreshnessStatistic(
        fresh = 0,
        warning = 10,
        urgent = 5,
        expiry = 3
    )

    val mockAnalyticsUiState = AnalyticsUiState(
        expiryCount = 3,
        expiringSoon = 5,
        spoilageStatisticByMonth = mockSpoilagePoints,
        freshnessStatisticByMonth = mockFreshness,
        weeklyExpiryStatistic = mockWeeklyExpiry,
        consumedValue = 1_450_000,
        wastedValue = 180_000
    )
}