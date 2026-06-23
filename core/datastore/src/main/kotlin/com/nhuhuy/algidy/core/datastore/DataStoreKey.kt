package com.nhuhuy.algidy.core.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {
    val DARK_MODE = stringPreferencesKey("dark_mode")
    val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    val LANGUAGE = stringPreferencesKey("language")
    val BIOMETRIC_LOCK = booleanPreferencesKey("biometric_lock")
    val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
    val FONT = stringPreferencesKey("font")
    val CATEGORY = booleanPreferencesKey("category")
    val HOUR = intPreferencesKey("hour")
    val MINUTE = intPreferencesKey("minute")

    val WARNING_DAYS = intPreferencesKey("warning_days")
    val WEEKLY_REPORT = booleanPreferencesKey("weekly_report")
}


