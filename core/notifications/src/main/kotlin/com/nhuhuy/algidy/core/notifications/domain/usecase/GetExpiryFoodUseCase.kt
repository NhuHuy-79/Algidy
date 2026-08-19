package com.nhuhuy.algidy.core.notifications.domain.usecase

import com.nhuhuy.algidy.core.datastore.model.NotificationDataStore
import com.nhuhuy.algidy.core.domain.repository.FoodRepository
import com.nhuhuy.algidy.core.model.food.FoodItem
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class GetExpiryFoodUseCase(
    private val foodRepository: FoodRepository,
    private val notificationDataStore: NotificationDataStore,
) {
    suspend operator fun invoke(): List<FoodItem> {
        val allFoods = foodRepository.getAllFoodItems()
        val prefs = notificationDataStore.preferencesFlow.first()
        val warningDays = prefs.warningFoodThresholdDays

        val today = LocalDate.now()

        val expiryFoods = allFoods.filter { foodItem ->
            val expiryDate = Instant
                .ofEpochMilli(foodItem.expiryDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            val daysLeft = ChronoUnit.DAYS.between(
                today,
                expiryDate
            )
            Timber.d(
                "Food=${foodItem.name}, expiry=$expiryDate, today=$today, daysLeft=$daysLeft"
            )
            daysLeft in 0..warningDays.toLong()
        }

        Timber.d("Filtered foods count=${expiryFoods.size}")

        return expiryFoods.sortedBy { it.expiryDate }
    }
}