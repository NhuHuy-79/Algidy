package com.nhuhuy.algidy.feature.analytics.domain.repository

import com.nhuhuy.algidy.feature.analytics.domain.model.CategoryWasteStats
import com.nhuhuy.algidy.feature.analytics.domain.model.DailyFreshnessStats
import com.nhuhuy.algidy.feature.analytics.domain.model.SpoilageHistory
import com.nhuhuy.algidy.feature.analytics.domain.model.SummaryStats
import kotlinx.coroutines.flow.Flow

/**
 * Repository focused on providing processed data for Analytics.
 * De-coupled into smaller streams for better maintainability.
 */
interface AnalyticsRepository {
    /**
     * Observes basic summary counts (consumed, wasted, active).
     */
    fun getSummaryStats(): Flow<SummaryStats>

    /**
     * Observes the percentage of waste categorized by storage location.
     */
    fun getWastedByCategory(): Flow<List<CategoryWasteStats>>

    /**
     * Observes freshness distribution for the next 7 days.
     */
    fun getDailyFreshnessStats(): Flow<List<DailyFreshnessStats>>

    /**
     * Observes historical data of spoilage vs consumption for the current week.
     */
    fun getWeeklySpoilageHistory(): Flow<SpoilageHistory>
}
