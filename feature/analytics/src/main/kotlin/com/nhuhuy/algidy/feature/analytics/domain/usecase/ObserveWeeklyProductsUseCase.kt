package com.nhuhuy.algidy.feature.analytics.domain.usecase

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.getStartOfWeekMillis
import kotlinx.coroutines.flow.Flow

class ObserveWeeklyProductsUseCase(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(): Flow<List<FoodItem>> {
        val startOfWeekMillis : Long = getStartOfWeekMillis()
        return foodRepository.observeFoodItemBeforeTime(startOfWeekMillis)
    }

}