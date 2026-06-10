package com.nhuhuy.algidy.feature.food_entry.data

import android.content.Context
import com.nhuhuy.algidy.core.datastore.dataStore
import com.nhuhuy.algidy.core.datastore.utils.get
import com.nhuhuy.algidy.core.datastore.utils.set
import kotlinx.coroutines.flow.Flow

class FoodEntryDataStore(
    private val context: Context
) {
    val hasAskNotificationPermission: Flow<Boolean>
        get() = context.dataStore.data.get(
            key = FoodEntryKey.ASK_NOTIFICATION_KEY, false
        )

    val addItemFirst: Flow<Boolean>
        get() = context.dataStore.data.get(
            key = FoodEntryKey.FIRST_ADD_ITEM, false
        )

    suspend fun updateAskNotificationPermission(value: Boolean) {
        context.dataStore.set(
            key = FoodEntryKey.ASK_NOTIFICATION_KEY,
            value = value
        )
    }

    suspend fun updateAddItemFirst(value: Boolean) {
        context.dataStore.set(
            key = FoodEntryKey.FIRST_ADD_ITEM,
            value = value
        )
    }
}