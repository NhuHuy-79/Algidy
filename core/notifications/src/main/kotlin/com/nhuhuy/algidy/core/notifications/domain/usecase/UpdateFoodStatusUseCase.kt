package com.nhuhuy.algidy.core.notifications.domain.usecase

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodStatus

class UpdateFoodStatusUseCase(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(foodId: String, status: FoodStatus) {
        foodRepository.updateFoodStatus(foodId, status)
    }
}
