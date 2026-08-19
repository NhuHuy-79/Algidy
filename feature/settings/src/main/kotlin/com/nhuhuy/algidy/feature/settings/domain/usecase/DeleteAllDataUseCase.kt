package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.core.domain.repository.FoodRepository
import com.nhuhuy.algidy.core.domain.repository.LocalMediaStorage
import com.nhuhuy.algidy.core.model.error_handling.Resource

class DeleteAllDataUseCase(
    private val foodRepository: FoodRepository,
    private val localMediaStorage: LocalMediaStorage,
) {
    suspend operator fun invoke(): Resource<Unit> {
        return foodRepository.deleteAllFoodItems()
            .onSuccess {
                localMediaStorage.deleteAllFromInternalStorage()
            }
    }
}