package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode

class SelectSettingUseCase(
    private val settingsDataStore: SettingsDataStore
) {
    suspend fun selectDarkMode(darkMode: DarkMode) {
        settingsDataStore.setDarkMode(darkMode)
    }

    suspend fun selectAppLanguage(language: AppLanguage) {
        settingsDataStore.setLanguage(language)
    }
}