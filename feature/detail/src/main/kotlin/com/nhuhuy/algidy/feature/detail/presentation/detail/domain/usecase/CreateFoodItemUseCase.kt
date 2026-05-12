package com.nhuhuy.algidy.feature.detail.presentation.detail.domain.usecase

import com.nhuhuy.algidy.core.data.LocalMediaStorage
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.util.getDataOrNull
import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.core.model.food.FoodItem

class CreateFoodItemUseCase(
    private val foodRepository: FoodRepository,
    private val localMediaStorage: LocalMediaStorage
) {
    suspend operator fun invoke(foodItem: FoodItem): Resource<FoodItem> {
        val oldUri: String? = foodItem.imageUri

        val newUri: String? =
            if (oldUri != null) localMediaStorage.copyImageToInternalStorage(oldUri)
                .getDataOrNull() else oldUri

        return foodRepository.addFoodItem(foodItem.copy(imageUri = newUri))
    }
}
