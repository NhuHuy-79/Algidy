package com.nhuhuy.algidy.feature.settings.domain.usecase

import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.model.setting.DarkMode

class SetDarkModeUseCase(
    private val settingsDataStore: SettingsDataStore
) {
    suspend operator fun invoke(darkMode: DarkMode) {
        settingsDataStore.setDarkMode(darkMode)
    }
}