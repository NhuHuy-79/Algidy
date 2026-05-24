package com.nhuhuy.algidy.feature.food_entry.domain.usecase

import com.nhuhuy.algidy.core.data.repository.CategoryRepository
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodCategory
import com.nhuhuy.algidy.core.model.food.FoodItem
import kotlinx.coroutines.flow.Flow

class ObserveCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<List<FoodCategory>> = categoryRepository.observeAllCategories()
}

class AddCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: FoodCategory) = categoryRepository.addCategory(category)
}

class SaveFoodItemUseCase(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(foodItem: FoodItem) {
        if (foodRepository.getFoodById(foodItem.id) != null) {
            foodRepository.updateFoodItem(foodItem)
        } else {
            foodRepository.addFoodItem(foodItem)
        }
    }
}
