package com.nhuhuy.algidy.core.domain.usecase.category

import com.nhuhuy.algidy.core.domain.repository.CategoryRepository
import com.nhuhuy.algidy.core.model.food.FoodCategory

class EditCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: FoodCategory){
        categoryRepository.updateCategory(category = category)
    }
}