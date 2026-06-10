package com.nhuhuy.algidy.feature.food_entry.data

import androidx.datastore.preferences.core.booleanPreferencesKey

object FoodEntryKey {
    val ASK_NOTIFICATION_KEY = booleanPreferencesKey("ask_notification_permission")
    val FIRST_ADD_ITEM = booleanPreferencesKey("first_add_item")
}