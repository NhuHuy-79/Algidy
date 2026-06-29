package com.nhuhuy.algidy.feature.analytics.data.repository

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.feature.analytics.domain.model.CategoryWasteStats
import com.nhuhuy.algidy.feature.analytics.domain.model.DailyFreshnessStats
import com.nhuhuy.algidy.feature.analytics.domain.model.SpoilageHistory
import com.nhuhuy.algidy.feature.analytics.domain.model.SummaryStats
import com.nhuhuy.algidy.feature.analytics.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class AnalyticsRepositoryImpl(
    private val foodRepository: FoodRepository
) : AnalyticsRepository {

    /**
     * Helper to convert epoch millis to LocalDate.
     */
    private fun Long.toLocalDate(): LocalDate {
        return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    override fun getSummaryStats(): Flow<SummaryStats> =
        foodRepository.observeAllFoodItems().map { items ->
            // Calculate current week boundaries
            val now = LocalDate.now()
            val weekStart = now.with(java.time.DayOfWeek.MONDAY)
            val weekEnd = weekStart.plusDays(6)

            val wastedInWeek = items.filter {
                it.status == FoodStatus.WASTED &&
                        it.resolvedDate?.toLocalDate()?.let { date ->
                            !date.isBefore(weekStart) && !date.isAfter(weekEnd)
                        } == true
            }
            val consumedInWeek = items.filter {
                it.status == FoodStatus.CONSUMED &&
                        it.resolvedDate?.toLocalDate()?.let { date ->
                            !date.isBefore(weekStart) && !date.isAfter(weekEnd)
                        } == true
            }
            val activeItems = items.filter { it.status == FoodStatus.ACTIVE }

            // Total products "active" in the current context: resolved this week OR still active
            val itemsInCurrentWeekCount = wastedInWeek.size + consumedInWeek.size + activeItems.size

            SummaryStats(
                weeklyCount = itemsInCurrentWeekCount,
                wastedCount = wastedInWeek.size,
                consumedCount = consumedInWeek.size,
                otherCount = activeItems.size
            )
        }

    override fun getWastedByCategory(): Flow<List<CategoryWasteStats>> =
        foodRepository.observeAllFoodItems().map { items ->
            val now = LocalDate.now()
            val weekStart = now.with(java.time.DayOfWeek.MONDAY)
            val weekEnd = weekStart.plusDays(6)

            val wastedInWeek = items.filter {
                it.status == FoodStatus.WASTED &&
                        it.resolvedDate?.toLocalDate()?.let { date ->
                            !date.isBefore(weekStart) && !date.isAfter(weekEnd)
                        } == true
            }
            val totalWasted = wastedInWeek.size.toFloat()

            StorageLocation.entries.map { location ->
                val count = wastedInWeek.count { it.location == location }
                CategoryWasteStats(
                    location = location,
                    percentage = if (totalWasted > 0) count / totalWasted else 0f
                )
            }
        }

    override fun getDailyFreshnessStats(): Flow<List<DailyFreshnessStats>> =
        foodRepository.observeAllFoodItems().map { items ->
            val activeItems = items.filter { it.status == FoodStatus.ACTIVE }
            
            // Generate stats for the next 7 days (including today)
            (0..6).map { i ->
                val targetDate = LocalDate.now().plusDays(i.toLong())
                val itemsOnDay = activeItems.filter { it.expiryDate.toLocalDate() == targetDate }
                
                DailyFreshnessStats(
                    date = targetDate,
                    freshCount = itemsOnDay.count { it.getFreshnessStatus() == Freshness.FRESH }.toDouble(),
                    expiredCount = itemsOnDay.count { it.getFreshnessStatus() == Freshness.EXPIRED }.toDouble(),
                    urgentCount = itemsOnDay.count { it.getFreshnessStatus() == Freshness.URGENT }.toDouble(),
                    warningCount = itemsOnDay.count { it.getFreshnessStatus() == Freshness.WARNING }.toDouble()
                )
            }
        }

    override fun getWeeklySpoilageHistory(): Flow<SpoilageHistory> =
        foodRepository.observeAllFoodItems().map { items ->
            val now = LocalDate.now()
            val weekStart = now.with(java.time.DayOfWeek.MONDAY)
            
            // Daily breakdown for the CURRENT week (Mon-Sun)
            val wastedValues = mutableListOf<Double>()
            val consumedValues = mutableListOf<Double>()
            val labels = mutableListOf<String>()

            (0..6).forEach { i ->
                val day = weekStart.plusDays(i.toLong())
                
                val wastedOnDay = items.count {
                    it.status == FoodStatus.WASTED && it.resolvedDate?.toLocalDate() == day
                }.toDouble()

                val consumedOnDay = items.count {
                    it.status == FoodStatus.CONSUMED && it.resolvedDate?.toLocalDate() == day
                }.toDouble()

                wastedValues.add(wastedOnDay)
                consumedValues.add(consumedOnDay)
                labels.add(day.dayOfWeek.name.take(3)) // e.g., MON, TUE
            }

            SpoilageHistory(
                wastedValues = wastedValues,
                consumedValues = consumedValues,
                labels = labels
            )
        }
}
