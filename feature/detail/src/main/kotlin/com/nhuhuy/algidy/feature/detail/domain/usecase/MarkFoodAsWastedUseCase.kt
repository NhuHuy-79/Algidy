package com.nhuhuy.algidy.feature.detail.domain.usecase

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodStatus

class MarkFoodAsWastedUseCase(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(foodId: String) {
        foodRepository.updateFoodStatus(id = foodId, newStatus = FoodStatus.WASTED)
    }
}
