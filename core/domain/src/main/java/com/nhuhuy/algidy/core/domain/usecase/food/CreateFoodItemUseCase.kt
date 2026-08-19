package com.nhuhuy.algidy.core.domain.usecase.food

import com.nhuhuy.algidy.core.domain.repository.FoodRepository
import com.nhuhuy.algidy.core.domain.repository.LocalMediaStorage
import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.core.model.error_handling.getDataOrNull
import com.nhuhuy.algidy.core.model.food.FoodItem

class CreateFoodItemUseCase(
    private val foodRepository: FoodRepository,
    private val localMediaStorage: LocalMediaStorage,
) {
    suspend operator fun invoke(
        foodItem: FoodItem
    ): Resource<FoodItem> {
        val itemToSave = foodItem.imageUri?.let { uri ->
            val newUri = localMediaStorage.copyImageToInternalStorage(uri).getDataOrNull()
            foodItem.copy(imageUri = newUri)
        } ?: foodItem

        return foodRepository.addFoodItem(itemToSave)
    }
}