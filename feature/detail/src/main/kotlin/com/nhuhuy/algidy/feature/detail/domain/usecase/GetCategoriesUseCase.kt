package com.nhuhuy.algidy.feature.detail.domain.usecase

import com.nhuhuy.algidy.core.data.repository.CategoryRepository
import com.nhuhuy.algidy.core.model.food.FoodCategory

class GetCategoriesUseCase(
    private val categoriesRepository: CategoryRepository
) {
    suspend operator fun invoke(): List<FoodCategory> {
        return categoriesRepository.getAllCategories()
    }
}