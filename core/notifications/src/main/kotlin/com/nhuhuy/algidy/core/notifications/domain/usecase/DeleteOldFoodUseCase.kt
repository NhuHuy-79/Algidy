package com.nhuhuy.algidy.core.notifications.domain.usecase

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.model.error_handling.Resource
import kotlinx.coroutines.flow.first
import java.util.Calendar

class DeleteOldFoodUseCase(
    private val foodRepository: FoodRepository,
    private val settingsDataStore: SettingsDataStore,
) {
    suspend operator fun invoke(): Resource<Unit> {
        val daysThreshold = settingsDataStore.deleteThreshold.first()

        if (daysThreshold <= 0) return Resource.Success(Unit)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.DAY_OF_YEAR, -daysThreshold)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val thresholdTimestamp = calendar.timeInMillis

        return foodRepository.deleteFoodAfterDay(thresholdTimestamp)
    }
}