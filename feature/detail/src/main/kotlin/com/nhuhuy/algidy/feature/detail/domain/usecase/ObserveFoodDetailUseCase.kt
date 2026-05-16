package com.nhuhuy.algidy.feature.detail.domain.usecase

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodItem
import kotlinx.coroutines.flow.Flow

class ObserveFoodDetailUseCase(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(foodId: String): Flow<FoodItem> {
        return foodRepository.observeFoodItemById(foodId)
    }
}