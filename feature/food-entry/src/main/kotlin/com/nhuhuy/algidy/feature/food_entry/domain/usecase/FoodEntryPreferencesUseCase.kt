package com.nhuhuy.algidy.feature.food_entry.domain.usecase

import com.nhuhuy.algidy.core.datastore.DeviceCapableDataStore
import com.nhuhuy.algidy.feature.food_entry.data.FoodEntryDataStore
import com.nhuhuy.algidy.feature.food_entry.domain.model.FoodEntryPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class FoodEntryPreferencesUseCase(
    private val foodEntryDataStore: FoodEntryDataStore,
    private val capableDataStore: DeviceCapableDataStore,
) {
    suspend fun askNotificationPermission(granted: Boolean) {
        foodEntryDataStore.updateAskNotificationPermission(true)
        capableDataStore.setNotificationGranted(granted)
    }

    suspend fun addItemFirst(value: Boolean) {
        foodEntryDataStore.updateAddItemFirst(value)
    }

    fun observe(): Flow<FoodEntryPreferences> {
        return combine(
            foodEntryDataStore.hasAskNotificationPermission,
            foodEntryDataStore.addItemFirst
        ) { askPermission, addItemFirst ->
            FoodEntryPreferences(
                hasAskNotificationPermission = askPermission,
                addItemFirst = addItemFirst
            )
        }
    }
}