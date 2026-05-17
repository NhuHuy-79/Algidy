package com.nhuhuy.algidy.feature.analytics.data.repository

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.feature.analytics.domain.model.AnalyticsStats
import com.nhuhuy.algidy.feature.analytics.domain.model.CategoryWasteStats
import com.nhuhuy.algidy.feature.analytics.domain.model.DailyFreshnessStats
import com.nhuhuy.algidy.feature.analytics.domain.model.SpoilageHistory
import com.nhuhuy.algidy.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class AnalyticsRepositoryImpl(
    private val foodRepository: FoodRepository
) : AnalyticsRepository {
    override fun getAnalyticsStats(): Flow<AnalyticsStats> =
        foodRepository.observeAllFoodItems().map { items ->
            val activeItems = items.filter { it.status == FoodStatus.ACTIVE }
            val wastedItems = items.filter { it.status == FoodStatus.WASTED }
            val consumedItems = items.filter { it.status == FoodStatus.CONSUMED }

            // 1. Overall Stats
            val wastedCount = wastedItems.size
            val consumedCount = consumedItems.size
            val otherCount = activeItems.size

            // 2. Wasted by Category (Location)
            val totalWasted = wastedCount.toFloat()
            val wastedByCategory = StorageLocation.entries.map { location ->
                val count = wastedItems.count { it.location == location }
                CategoryWasteStats(
                    location = location,
                    percentage = if (totalWasted > 0) count / totalWasted else 0f
                )
            }

            // 3. Daily Freshness Stats (Current active items distribution)
            val dailyStats = (0..6).map { i ->
                val targetDate = LocalDate.now().plusDays(i.toLong())
                val itemsOnDay = activeItems.filter {
                    val expiryDate = it.expiryDate.toLocalDate()
                    expiryDate == targetDate
                }
                DailyFreshnessStats(
                    date = targetDate,
                    freshCount = itemsOnDay.count { it.getFreshnessStatus() == Freshness.FRESH }.toDouble(),
                    expiredCount = itemsOnDay.count { it.getFreshnessStatus() == Freshness.EXPIRED }.toDouble(),
                    urgentCount = itemsOnDay.count { it.getFreshnessStatus() == Freshness.URGENT }.toDouble(),
                    warningCount = itemsOnDay.count { it.getFreshnessStatus() == Freshness.WARNING }.toDouble()
                )
            }

            // 4. Spoilage History (Last 4 weeks)
            val now = LocalDate.now()
            val weekStartOfNow = now.with(java.time.DayOfWeek.MONDAY)
            val weekEndOfNow = weekStartOfNow.plusDays(6)

            val itemsInCurrentWeek = items.filter {
                val resolvedDate = it.resolvedDate?.toLocalDate()
                val isResolvedInWeek = resolvedDate != null && !resolvedDate.isBefore(weekStartOfNow) && !resolvedDate.isAfter(weekEndOfNow)
                val isActive = it.status == FoodStatus.ACTIVE
                isResolvedInWeek || isActive
            }
            val weeklyCount = itemsInCurrentWeek.size

            val spoilageHistory = (0..3).map { i ->
                val weekStart = now.minusWeeks(i.toLong()).with(java.time.DayOfWeek.MONDAY)
                val weekEnd = weekStart.plusDays(6)
                
                val wastedInWeek = wastedItems.count {
                    val resolvedDate = it.resolvedDate?.toLocalDate()
                    resolvedDate != null && !resolvedDate.isBefore(weekStart) && !resolvedDate.isAfter(weekEnd)
                }.toDouble()

                val consumedInWeek = consumedItems.count {
                    val resolvedDate = it.resolvedDate?.toLocalDate()
                    resolvedDate != null && !resolvedDate.isBefore(weekStart) && !resolvedDate.isAfter(weekEnd)
                }.toDouble()

                Triple("WEEK ${4 - i}", wastedInWeek, consumedInWeek)
            }.reversed()

            AnalyticsStats(
                weeklyCount = weeklyCount,
                wastedCount = wastedCount,
                consumedCount = consumedCount,
                otherCount = otherCount,
                wastedByCategory = wastedByCategory,
                dailyStats = dailyStats,
                spoilageHistory = SpoilageHistory(
                    wastedByWeek = spoilageHistory.map { it.second },
                    consumedByWeek = spoilageHistory.map { it.third },
                    weekLabels = spoilageHistory.map { it.first }
                )
            )
        }

    private fun Long.toLocalDate(): LocalDate {
        return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
    }
}
