package com.nhuhuy.algidy.core.datastore.migration

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.nhuhuy.algidy.core.datastore.UserPreferencesKeys
import com.nhuhuy.algidy.core.datastore.model.NotificationDataStore
import com.nhuhuy.algidy.core.datastore.model.NotificationPreferences
import kotlinx.coroutines.flow.first

class NotificationMigrator(
    private val dataStore: DataStore<Preferences>,
    private val notificationDataStore: NotificationDataStore,
) : BaseMigrator {
    override suspend fun shouldMigrate(): Boolean {
        val prefs = dataStore.data.first()

        return prefs.contains(UserPreferencesKeys.HOUR) ||
                prefs.contains(UserPreferencesKeys.MINUTE) ||
                prefs.contains(UserPreferencesKeys.WARNING_DAYS) ||
                prefs.contains(UserPreferencesKeys.DELETE_THRESHOLD) ||
                prefs.contains(UserPreferencesKeys.NOTIFICATIONS_ENABLED) ||
                prefs.contains(UserPreferencesKeys.WEEKLY_REPORT)
    }

    override suspend fun migrate() {
        val currentData = notificationDataStore.getPreferences()
        val oldPrefs = dataStore.data.first()
        val newData = NotificationPreferences(
            enableNotification = oldPrefs[UserPreferencesKeys.NOTIFICATIONS_ENABLED]
                ?: currentData.enableNotification,
            enableWeeklyReport = oldPrefs[UserPreferencesKeys.WEEKLY_REPORT]
                ?: currentData.enableWeeklyReport,
            warningFoodThreshold = oldPrefs[UserPreferencesKeys.WARNING_DAYS]
                ?: currentData.warningFoodThreshold,
            deleteFoodThreshold = oldPrefs[UserPreferencesKeys.DELETE_THRESHOLD]
                ?: currentData.deleteFoodThreshold,
            hour = oldPrefs[UserPreferencesKeys.HOUR] ?: currentData.hour,
            minutes = oldPrefs[UserPreferencesKeys.MINUTE] ?: currentData.minutes
        )
        notificationDataStore.setPreference(newData)
    }

    override suspend fun cleanUp() {
        dataStore.edit {
            it.remove(UserPreferencesKeys.HOUR)
            it.remove(UserPreferencesKeys.MINUTE)
            it.remove(UserPreferencesKeys.WARNING_DAYS)
            it.remove(UserPreferencesKeys.DELETE_THRESHOLD)
            it.remove(UserPreferencesKeys.NOTIFICATIONS_ENABLED)
            it.remove(UserPreferencesKeys.WEEKLY_REPORT)
        }
    }
}