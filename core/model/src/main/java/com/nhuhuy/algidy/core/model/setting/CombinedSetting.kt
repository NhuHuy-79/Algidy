package com.nhuhuy.algidy.core.model.setting

data class CombinedSetting(
    val enableBiometricsLock: Boolean = false,
    val enableDynamicColor: Boolean = false,
    val darkMode: DarkMode = DarkMode.SYSTEM,
)
