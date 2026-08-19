package com.nhuhuy.algidy.feature.inventory.domain.usecase.food

import com.nhuhuy.algidy.core.domain.repository.FoodRepository

class DeleteFoodItemUseCase(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(id: String) {
        return foodRepository.removeFoodItem(id)
    }
}