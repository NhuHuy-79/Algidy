package com.nhuhuy.algidy.core.notifications.domain.usecase

import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.model.food.FoodItem
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class GetExpiryFoodUseCase(
    private val foodRepository: FoodRepository,
    private val settingsDataStore: SettingsDataStore,
) {
    suspend operator fun invoke(): List<FoodItem> {
        val allFoods = foodRepository.getAllFoodItems()
        val dayWarnings = settingsDataStore.warningDayFlow.first()
        val currentTime = System.currentTimeMillis()

        val warningPeriodMs = TimeUnit.DAYS.toMillis(dayWarnings.toLong())

        return allFoods.filter { foodItem ->
            val timeUnitExpiry = foodItem.expiryDate - currentTime
            timeUnitExpiry in 0..warningPeriodMs
        }
    }

}