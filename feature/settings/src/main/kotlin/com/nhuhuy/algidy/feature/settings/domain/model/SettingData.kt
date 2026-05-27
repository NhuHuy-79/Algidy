package com.nhuhuy.algidy.feature.settings.domain.model

import com.nhuhuy.algidy.core.model.setting.AppFont
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode

data class SettingData(
    val enableNotifications: Boolean = true,
    val enableBiometricsLock: Boolean = false,
    val enableDynamicColor: Boolean = false,
    val darkMode: DarkMode = DarkMode.SYSTEM,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val font: AppFont = AppFont.DEFAULT,
    val enabledCategoryGroup: Boolean = false,
    val hour: Int = 7,
    val minute: Int = 30,
)
