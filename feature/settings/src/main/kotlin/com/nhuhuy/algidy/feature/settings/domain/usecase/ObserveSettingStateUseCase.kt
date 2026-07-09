package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.datastore.model.AppearanceDataStore
import com.nhuhuy.algidy.core.datastore.model.NotificationDataStore
import com.nhuhuy.algidy.feature.settings.domain.model.SettingDataPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObserveSettingStateUseCase(
    private val settingsDataStore: SettingsDataStore,
    private val notificationDataStore: NotificationDataStore,
    private val appearanceDataStore: AppearanceDataStore,
) {
    fun observe(): Flow<SettingDataPreferences> {
        return combine(
            settingsDataStore.biometricLockFlow,
            notificationDataStore.preferencesFlow,
            appearanceDataStore.preferencesFlow,
        ) { enableBiometric, notification, appearance ->
            SettingDataPreferences(
                enableBiometric = enableBiometric,
                notificationPreferences = notification,
                appearancePreferences = appearance
            )
        }
    }
}