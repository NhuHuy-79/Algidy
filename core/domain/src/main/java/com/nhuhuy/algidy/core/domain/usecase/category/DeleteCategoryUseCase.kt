package com.nhuhuy.algidy.core.domain.usecase.category

import com.nhuhuy.algidy.core.domain.repository.CategoryRepository

class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryId: String){
        categoryRepository.deleteCategory(categoryId)
    }
}