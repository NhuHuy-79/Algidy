package com.nhuhuy.algidy.feature.detail.domain.usecase

import com.nhuhuy.algidy.core.data.LocalMediaStorage
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.util.getDataOrNull
import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.core.model.food.FoodItem

class UpdateFoodDetailUseCase(
    private val foodRepository: FoodRepository,
    private val localMediaStorage: LocalMediaStorage,
) {
    suspend operator fun invoke(
        newItem: FoodItem,
        newImageUri: String?
    ): Resource<Unit> {
        val finalImageUri = newImageUri?.let { uri ->
            localMediaStorage.copyImageToInternalStorage(uri).getDataOrNull()
        }
        return foodRepository.updateFoodItem(
            newItem.copy(
                imageUri = finalImageUri ?: newItem.imageUri
            )
        )
    }
}