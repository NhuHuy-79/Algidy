package com.nhuhuy.algidy.feature.inventory.domain.usecase.category

import com.nhuhuy.algidy.core.data.repository.CategoryRepository
import com.nhuhuy.algidy.core.model.food.FoodCategory

class EditCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: FoodCategory){
        categoryRepository.updateCategory(category = category)
    }
}