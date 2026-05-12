package com.nhuhuy.algidy.feature.analytics.data.repository

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.feature.analytics.domain.model.AnalyticsStats
import com.nhuhuy.algidy.feature.analytics.domain.model.CategoryWasteStats
import com.nhuhuy.algidy.feature.analytics.domain.model.DailyFreshnessStats
import com.nhuhuy.algidy.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class AnalyticsRepositoryImpl(
    private val foodRepository: FoodRepository
) : AnalyticsRepository {
    override fun getAnalyticsStats(): Flow<AnalyticsStats> =
        foodRepository.observeAllFoodItems().map { items ->
            val wastedCount = items.count { it.status == FoodStatus.WASTED }
            val consumedCount = items.count { it.status == FoodStatus.CONSUMED }

            val wastedItems = items.filter { it.status == FoodStatus.WASTED }
            val totalWasted = wastedItems.size.toFloat()
            val wastedByCategory = StorageLocation.entries.map { location ->
                val count = wastedItems.count { it.location == location }
                CategoryWasteStats(
                    location = location,
                    percentage = if (totalWasted > 0) count / totalWasted else 0f
                )
            }

            // Mock daily stats for 7 days
            val dailyStats = (0..6).map { i ->
                val date = LocalDate.now().minusDays(i.toLong())
                DailyFreshnessStats(
                    date = date,
                    freshCount = (1..10).random().toDouble(),
                    expiredCount = (1..5).random().toDouble(),
                    urgentCount = (1..3).random().toDouble(),
                    warningCount = (1..4).random().toDouble()
                )
            }.reversed()

            AnalyticsStats(
                wastedCount = wastedCount,
                consumedCount = consumedCount,
                wastedByCategory = wastedByCategory,
                dailyStats = dailyStats
            )
        }
}
