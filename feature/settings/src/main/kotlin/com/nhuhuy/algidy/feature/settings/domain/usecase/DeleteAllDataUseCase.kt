package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.data.repository.FoodRepository

class DeleteAllDataUseCase(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke() {
        foodRepository.deleteAllFoodItems()
    }
}