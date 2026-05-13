package com.nhuhuy.algidy.core.notifications.domain.usecase

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodItem
import java.util.concurrent.TimeUnit

class GetExpiryFoodUseCase(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(
        dayWarnings: Int = 3
    ): List<FoodItem> {
        val allFoods = foodRepository.getAllFoodItems()

        val currentTime = System.currentTimeMillis()

        val warningPeriodMs = TimeUnit.DAYS.toMillis(dayWarnings.toLong())

        return allFoods.filter { foodItem ->
            val timeUnitExpiry = foodItem.expiryDate - currentTime
            timeUnitExpiry in 0..warningPeriodMs
        }
    }
}