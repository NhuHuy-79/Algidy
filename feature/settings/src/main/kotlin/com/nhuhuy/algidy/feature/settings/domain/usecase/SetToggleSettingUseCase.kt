package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore

class SetToggleSettingUseCase(
    private val settingsDataStore: SettingsDataStore
) {
    suspend fun toggleBiometricLock(enable: Boolean) {
        settingsDataStore.setBiometricLock(enable)
    }

    suspend fun toggleNotifications(enable: Boolean) {
        settingsDataStore.setNotificationsEnabled(enable)
    }

    suspend fun toggleDynamicColor(enable: Boolean) {
        settingsDataStore.setDynamicColor(enable)
    }

    suspend fun toggleCategoryGroup(enabled: Boolean) {
        settingsDataStore.setCategoryGroup(enabled)
    }
}