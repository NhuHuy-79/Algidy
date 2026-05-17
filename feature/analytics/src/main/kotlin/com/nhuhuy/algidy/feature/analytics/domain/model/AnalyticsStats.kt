package com.nhuhuy.algidy.feature.analytics.domain.model

import com.nhuhuy.algidy.core.model.food.StorageLocation

data class CategoryWasteStats(
    val location: StorageLocation,
    val percentage: Float
)

data class AnalyticsStats(
    val weeklyCount: Int = 0,
    val wastedCount: Int = 0,
    val consumedCount: Int = 0,
    val otherCount: Int = 0,
    val dailyStats: List<DailyFreshnessStats> = emptyList(),
    val wastedByCategory: List<CategoryWasteStats> = emptyList(),
    val spoilageHistory: SpoilageHistory = SpoilageHistory()
)

data class SpoilageHistory(
    val wastedByWeek: List<Double> = emptyList(),
    val consumedByWeek: List<Double> = emptyList(),
    val weekLabels: List<String> = emptyList()
)
