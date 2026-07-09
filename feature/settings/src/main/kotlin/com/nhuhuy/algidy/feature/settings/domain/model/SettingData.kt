package com.nhuhuy.algidy.feature.settings.domain.model

import com.nhuhuy.algidy.core.datastore.model.AppearancePreferences
import com.nhuhuy.algidy.core.datastore.model.GeneralPreferences
import com.nhuhuy.algidy.core.datastore.model.NotificationPreferences
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
    val warningDay: Int = 3,
    val weeklyReport: Boolean = false,
    val deleteThresholdDays: Int = 0,
)

data class SettingDataPreferences(
    val enableBiometric: Boolean = false,
    val appearancePreferences: AppearancePreferences = AppearancePreferences(),
    val generalPreferences: GeneralPreferences = GeneralPreferences(),
    val notificationPreferences: NotificationPreferences = NotificationPreferences()
)
