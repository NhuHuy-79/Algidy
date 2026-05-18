package com.nhuhuy.algidy.feature.analytics.domain.usecase

import com.nhuhuy.algidy.feature.analytics.domain.model.CategoryWasteStats
import com.nhuhuy.algidy.feature.analytics.domain.model.DailyFreshnessStats
import com.nhuhuy.algidy.feature.analytics.domain.model.SpoilageHistory
import com.nhuhuy.algidy.feature.analytics.domain.model.SummaryStats
import com.nhuhuy.algidy.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow

/**
 * UseCase to observe summary statistics.
 */
class GetSummaryStatsUseCase(private val repository: AnalyticsRepository) {
    operator fun invoke(): Flow<SummaryStats> = repository.getSummaryStats()
}

/**
 * UseCase to observe waste statistics by category.
 */
class GetWastedByCategoryUseCase(private val repository: AnalyticsRepository) {
    operator fun invoke(): Flow<List<CategoryWasteStats>> = repository.getWastedByCategory()
}

/**
 * UseCase to observe daily freshness statistics for the upcoming week.
 */
class GetDailyFreshnessStatsUseCase(private val repository: AnalyticsRepository) {
    operator fun invoke(): Flow<List<DailyFreshnessStats>> = repository.getDailyFreshnessStats()
}

/**
 * UseCase to observe weekly spoilage vs consumption history.
 */
class GetWeeklySpoilageHistoryUseCase(private val repository: AnalyticsRepository) {
    operator fun invoke(): Flow<SpoilageHistory> = repository.getWeeklySpoilageHistory()
}
