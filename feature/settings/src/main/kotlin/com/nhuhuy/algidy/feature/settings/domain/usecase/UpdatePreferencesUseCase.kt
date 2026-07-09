package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.datastore.model.AppearanceDataStore
import com.nhuhuy.algidy.core.datastore.model.AppearancePreferences
import com.nhuhuy.algidy.core.datastore.model.NotificationDataStore
import com.nhuhuy.algidy.core.datastore.model.NotificationPreferences

class UpdatePreferencesUseCase(
    private val settingsDataStore: SettingsDataStore,
    private val appearanceDataStore: AppearanceDataStore,
    private val notificationDataStore: NotificationDataStore
) {
    suspend fun updateAppearance(newValue: AppearancePreferences) {
        appearanceDataStore.setPreference(newValue)
    }

    suspend fun updateNotification(newValue: NotificationPreferences) {
        notificationDataStore.setPreference(newValue)
    }

    suspend fun updateBiometric(enabled: Boolean) {
        settingsDataStore.setBiometricLock(enabled)
    }
}