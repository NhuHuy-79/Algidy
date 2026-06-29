package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.error_handling.Resource

class DeleteAllDataUseCase(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return foodRepository.deleteAllFoodItems()
    }
}