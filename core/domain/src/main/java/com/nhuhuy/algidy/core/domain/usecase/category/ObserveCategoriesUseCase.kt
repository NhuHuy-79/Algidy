package com.nhuhuy.algidy.core.domain.usecase.category

import com.nhuhuy.algidy.core.domain.repository.CategoryRepository
import com.nhuhuy.algidy.core.model.food.FoodCategory
import kotlinx.coroutines.flow.Flow

class ObserveCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<List<FoodCategory>> {
        return categoryRepository.observeAllCategories()
    }
}