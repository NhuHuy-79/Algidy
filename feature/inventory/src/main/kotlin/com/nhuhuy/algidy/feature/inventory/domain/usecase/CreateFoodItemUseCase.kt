package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.core.data.LocalMediaStorage
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.util.getDataOrNull
import com.nhuhuy.algidy.core.model.error_handling.Resource
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