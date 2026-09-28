package com.nhuhuy.algidy.feature.analytics.presentation.preview

import com.nhuhuy.algidy.feature.analytics.domain.model.FreshnessStatistic
import com.nhuhuy.algidy.feature.analytics.presentation.model.SpoilagePointUiModel
import com.nhuhuy.algidy.feature.analytics.presentation.model.WeeklyExpiryStatisticUiModel
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsUiState

internal object FakeData {
    val mockSpoilagePoints = listOf(
        SpoilagePointUiModel(label = "1 Th01", waste = 2, consumed = 14),
        SpoilagePointUiModel(label = "8 Th01", waste = 1, consumed = 18),
        SpoilagePointUiModel(label = "15 Th01", waste = 3, consumed = 15),
        SpoilagePointUiModel(label = "22 Th01", waste = 0, consumed = 22),
        SpoilagePointUiModel(label = "29 Th01", waste = 1, consumed = 19),
        SpoilagePointUiModel(label = "5 Th02", waste = 2, consumed = 20),
    )

    val mockWeeklyExpiry = listOf(
        WeeklyExpiryStatisticUiModel(label = "Mon", count = 2),
        WeeklyExpiryStatisticUiModel(label = "Tue", count = 4),
        WeeklyExpiryStatisticUiModel(label = "Wed", count = 3),
        WeeklyExpiryStatisticUiModel(label = "Thu", count = 1),
        WeeklyExpiryStatisticUiModel(label = "Fri", count = 6),
        WeeklyExpiryStatisticUiModel(label = "Sat", count = 5),
        WeeklyExpiryStatisticUiModel(label = "Sun", count = 3),
    )

    val mockFreshness = FreshnessStatistic(
        fresh = 28,
        warning = 12,
        urgent = 6,
        expiry = 2
    )

    val mockAnalyticsUiState = AnalyticsUiState(
        expiryCount = 2,
        expiringSoon = 6,
        spoilageStatisticByMonth = mockSpoilagePoints,
        freshnessStatisticByMonth = mockFreshness,
        weeklyExpiryStatistic = mockWeeklyExpiry,
        consumedValue = 108,
        wastedValue = 9
    )
}