package com.nhuhuy.algidy.feature.inventory.domain.usecase.food

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodItem
import kotlinx.coroutines.flow.Flow

class ObserveFoodItemUseCase(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(): Flow<List<FoodItem>> {
        return foodRepository.observeAllActiveFoodItems()
    }
}