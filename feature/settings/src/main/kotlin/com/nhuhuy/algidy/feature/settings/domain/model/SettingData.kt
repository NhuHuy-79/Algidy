package com.nhuhuy.algidy.feature.settings.domain.model

import com.nhuhuy.algidy.core.datastore.model.AppearancePreferences
import com.nhuhuy.algidy.core.datastore.model.GeneralPreferences
import com.nhuhuy.algidy.core.datastore.model.NotificationPreferences

data class SettingDataPreferences(
    val enableBiometric: Boolean = false,
    val appearancePreferences: AppearancePreferences = AppearancePreferences(),
    val generalPreferences: GeneralPreferences = GeneralPreferences(),
    val notificationPreferences: NotificationPreferences = NotificationPreferences()
)
