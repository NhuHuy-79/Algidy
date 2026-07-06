package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.datastore.model.AppearanceDataStore
import com.nhuhuy.algidy.core.datastore.model.NotificationDataStore
import com.nhuhuy.algidy.feature.settings.domain.model.SettingData
import com.nhuhuy.algidy.feature.settings.domain.model.SettingDataPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObserveSettingStateUseCase(
    private val settingsDataStore: SettingsDataStore,
    private val notificationDataStore: NotificationDataStore,
    private val appearanceDataStore: AppearanceDataStore,
) {
    operator fun invoke(): Flow<SettingData> {
        return combine(
            settingsDataStore.biometricLockFlow,
            notificationDataStore.preferencesFlow,
            appearanceDataStore.preferencesFlow
        ) { biometric, notification, appearance ->
            SettingData(
                darkMode = appearance.darkMode,
                language = appearance.appLanguage,
                enableDynamicColor = appearance.enableDynamicColor,
                enableBiometricsLock = biometric,
                enableNotifications = notification.enableNotification,
                enabledCategoryGroup = appearance.enableCategoryGroup,
                hour = notification.hour,
                minute = notification.minutes,
                warningDay = notification.warningFoodThreshold,
                weeklyReport = notification.enableWeeklyReport,
                deleteThresholdDays = notification.deleteFoodThreshold,
            )
        }
    }

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