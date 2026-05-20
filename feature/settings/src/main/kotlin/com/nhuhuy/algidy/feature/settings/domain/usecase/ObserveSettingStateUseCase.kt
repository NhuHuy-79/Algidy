package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.model.setting.AppFont
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.feature.settings.domain.model.SettingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObserveSettingStateUseCase(
    private val settingsDataStore: SettingsDataStore,
) {
    operator fun invoke(): Flow<SettingData> {
        val flows = listOf<Flow<Any>>(
            settingsDataStore.darkModeFlow,
            settingsDataStore.notificationsEnabledFlow,
            settingsDataStore.dynamicColorFlow,
            settingsDataStore.biometricLockFlow,
            settingsDataStore.appLanguageFlow,
            settingsDataStore.appFontFlow
        )

        return combine(flows) { array ->
            SettingData(
                darkMode = array[0] as DarkMode,
                enableNotifications = array[1] as Boolean,
                enableDynamicColor = array[2] as Boolean,
                enableBiometricsLock = array[3] as Boolean,
                language = array[4] as AppLanguage,
                font = array[5] as AppFont
            )
        }
    }
}