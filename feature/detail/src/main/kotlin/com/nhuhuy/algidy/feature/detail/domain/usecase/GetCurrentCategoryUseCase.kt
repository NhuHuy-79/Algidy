package com.nhuhuy.algidy.feature.detail.domain.usecase

import com.nhuhuy.algidy.core.data.repository.CategoryRepository
import com.nhuhuy.algidy.core.model.food.FoodCategory

class GetCurrentCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: String): FoodCategory? = categoryRepository.getCategoryById(id)
}
