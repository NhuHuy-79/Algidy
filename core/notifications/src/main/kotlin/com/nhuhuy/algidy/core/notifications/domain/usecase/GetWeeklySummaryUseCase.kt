package com.nhuhuy.algidy.core.notifications.domain.usecase

import com.nhuhuy.algidy.core.domain.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodStatus
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

class GetWeeklySummaryUseCase(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(): Pair<Int, Int> {
        val allFoods = foodRepository.getAllFoodItems()
        val startOfWeek = LocalDate.now()
            .with(DayOfWeek.MONDAY)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val weeklyResolvedFoods = allFoods.filter { food ->
            val resolvedAt = food.resolvedDate ?: 0L
            resolvedAt >= startOfWeek
        }

        val consumedInWeek = weeklyResolvedFoods.count { it.status == FoodStatus.CONSUMED }
        val wastedInWeek = weeklyResolvedFoods.count { it.status == FoodStatus.WASTED }

        return Pair(consumedInWeek, wastedInWeek)
    }
}
