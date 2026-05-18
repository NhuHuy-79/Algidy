package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.core.data.repository.FoodRepository

class DeleteFoodItemUseCase(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(id: String) {
        return foodRepository.removeFoodItem(id)
    }
}