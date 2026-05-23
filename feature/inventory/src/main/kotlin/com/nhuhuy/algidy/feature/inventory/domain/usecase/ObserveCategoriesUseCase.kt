package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.core.data.repository.CategoryRepository
import com.nhuhuy.algidy.core.model.food.FoodCategory
import kotlinx.coroutines.flow.Flow

class ObserveCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<List<FoodCategory>> {
        return categoryRepository.getAllCategories()
    }
}
