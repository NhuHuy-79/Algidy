package com.nhuhuy.algidy.feature.inventory.domain.usecase.category

import com.nhuhuy.algidy.core.data.repository.CategoryRepository

class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryId: String){
        categoryRepository.deleteCategory(categoryId)
    }
}