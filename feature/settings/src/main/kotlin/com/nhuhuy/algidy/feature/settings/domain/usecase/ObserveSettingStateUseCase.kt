package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.feature.settings.domain.model.SettingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

class ObserveSettingStateUseCase(
    private val settingsDataStore: SettingsDataStore,
) {
    operator fun invoke(): Flow<SettingData> {
        return combine(
            settingsDataStore.darkModeFlow,
            settingsDataStore.notificationsEnabledFlow,
            settingsDataStore.dynamicColorFlow,
            settingsDataStore.biometricLockFlow
        ) { darkMode, notificationsEnabled, dynamicColor, biometricLock ->
            SettingData(
                darkMode = darkMode,
                enableNotifications = notificationsEnabled,
                enableDynamicColor = dynamicColor,
                enableBiometricsLock = biometricLock
            )
        }.flowOn(Dispatchers.IO)
    }
}