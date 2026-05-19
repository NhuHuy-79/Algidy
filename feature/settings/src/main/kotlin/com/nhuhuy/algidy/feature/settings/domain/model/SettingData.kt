package com.nhuhuy.algidy.feature.settings.domain.model

import com.nhuhuy.algidy.core.model.setting.DarkMode

data class SettingData(
    val enableNotifications: Boolean = true,
    val enableBiometricsLock: Boolean = false,
    val enableDynamicColor: Boolean = false,
    val darkMode: DarkMode = DarkMode.SYSTEM,
)
