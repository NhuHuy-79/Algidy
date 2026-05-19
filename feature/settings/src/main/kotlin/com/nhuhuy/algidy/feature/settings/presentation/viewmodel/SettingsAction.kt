package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction

sealed interface SettingsAction : UiAction {
    data class SetDarkMode(val darkMode: DarkMode) : SettingsAction
    data class ToggleNotifications(val enabled: Boolean) : SettingsAction
    data class ToggleBiometricLock(val enabled: Boolean) : SettingsAction
    data class ToggleDynamicColor(val enabled: Boolean) : SettingsAction
    data class ChangeLanguage(val language: AppLanguage) : SettingsAction
    data object OnBackClick : SettingsAction
}
