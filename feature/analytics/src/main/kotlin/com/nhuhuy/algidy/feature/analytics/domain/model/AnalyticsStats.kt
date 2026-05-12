package com.nhuhuy.algidy.feature.analytics.domain.model

import com.nhuhuy.algidy.core.model.food.StorageLocation

data class CategoryWasteStats(
    val location: StorageLocation,
    val percentage: Float
)

data class AnalyticsStats(
    val wastedCount: Int = 0,
    val consumedCount: Int = 0,
    val dailyStats: List<DailyFreshnessStats> = emptyList(),
    val wastedByCategory: List<CategoryWasteStats> = emptyList()
)
