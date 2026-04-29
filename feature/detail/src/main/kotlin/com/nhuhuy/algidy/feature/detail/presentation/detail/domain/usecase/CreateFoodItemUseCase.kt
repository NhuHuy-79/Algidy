package com.nhuhuy.algidy.feature.detail.presentation.detail.domain.usecase

import com.nhuhuy.algidy.core.data.LocalMediaStorage
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.util.getDataOrNull
import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.Resource

class CreateFoodItemUseCase(
    private val foodRepository: FoodRepository,
    private val localMediaStorage: LocalMediaStorage
) {
    suspend operator fun invoke(foodItem: FoodItem): Resource<Unit> {
        val oldUri: String? = foodItem.imageUri

        val newUri: String? =
            if (oldUri != null) localMediaStorage.copyImageToInternalStorage(oldUri)
                .getDataOrNull() else oldUri

        return foodRepository.addFoodItem(foodItem.copy(imageUri = newUri))
    }
}