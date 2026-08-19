package com.nhuhuy.algidy.core.domain.usecase.food

import com.nhuhuy.algidy.core.domain.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodItem

class GetFoodByIdUseCase(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(id: String): FoodItem? {
        return foodRepository.getFoodById(id)
    }
}