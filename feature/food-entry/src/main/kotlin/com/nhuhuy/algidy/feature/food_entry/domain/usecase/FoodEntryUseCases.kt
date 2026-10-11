package com.nhuhuy.algidy.feature.food_entry.domain.usecase

import com.nhuhuy.algidy.core.data.util.getDataOrNull
import com.nhuhuy.algidy.core.domain.repository.FoodRepository
import com.nhuhuy.algidy.core.domain.repository.LocalMediaStorage
import com.nhuhuy.algidy.core.model.food.FoodItem

class SaveFoodItemUseCase(
    private val foodRepository: FoodRepository,
    private val localMediaStorage: LocalMediaStorage
) {
    suspend operator fun invoke(foodItem: FoodItem) {
        val newUri = foodItem.imageUri?.let {
            localMediaStorage.copyImageToInternalStorage(uriPath = it).getDataOrNull()
                ?: foodItem.imageUri
        } ?: foodItem.imageUri


        if (foodRepository.getFoodById(foodItem.id) != null) {
            foodRepository.updateFoodItem(
                foodItem.copy(
                    imageUri = newUri
                )
            )
        } else {
            foodRepository.addFoodItem(
                foodItem.copy(
                    imageUri = newUri
                )
            )
        }
    }
}
