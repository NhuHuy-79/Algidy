package com.nhuhuy.algidy.feature.detail.domain.usecase

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodItem

class GetFoodDetailUseCase(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(foodId: String): FoodItem? {
        return foodRepository.getFoodById(id = foodId)
    }
}
