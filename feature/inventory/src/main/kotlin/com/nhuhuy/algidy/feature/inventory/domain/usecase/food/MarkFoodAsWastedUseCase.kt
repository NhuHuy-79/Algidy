package com.nhuhuy.algidy.feature.inventory.domain.usecase.food

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodStatus

class MarkFoodAsWastedUseCase(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(foodId: String) {
        foodRepository.updateFoodStatus(id = foodId, newStatus = FoodStatus.WASTED)
    }

    suspend fun executeWithList(foodIds: List<String>) {
        foodRepository.updateFoodStatusList(ids = foodIds, newStatus = FoodStatus.WASTED)
    }
}
