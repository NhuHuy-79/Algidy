package com.nhuhuy.algidy.core.domain.usecase.category

import com.nhuhuy.algidy.core.domain.repository.CategoryRepository
import com.nhuhuy.algidy.core.model.food.FoodCategory

class AddCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(name: String): FoodCategory {
        val newCategory = FoodCategory(name = name)
        categoryRepository.addCategory(newCategory)
        return newCategory
    }
}