package com.nhuhuy.algidy.feature.analytics.domain.model

import com.nhuhuy.algidy.core.model.food.StorageLocation

/**
 * Statistics for food waste by storage location.
 */
data class CategoryWasteStats(
    val location: StorageLocation,
    val percentage: Float
)

/**
 * History of spoiled (wasted) vs consumed food items, typically for a specific timeframe.
 */
data class SpoilageHistory(
    val wastedValues: List<Double> = emptyList(),
    val consumedValues: List<Double> = emptyList(),
    val labels: List<String> = emptyList()
)

/**
 * Summary statistics for the dashboard cards.
 */
data class SummaryStats(
    val weeklyCount: Int = 0,
    val wastedCount: Int = 0,
    val consumedCount: Int = 0,
    val otherCount: Int = 0
)
