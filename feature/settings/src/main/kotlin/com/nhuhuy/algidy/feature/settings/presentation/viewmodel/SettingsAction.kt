package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction

sealed interface SettingsAction : UiAction {
    data class ToggleDarkMode(val enabled: Boolean) : SettingsAction
    data class ToggleNotifications(val enabled: Boolean) : SettingsAction
    data class ChangeLanguage(val language: String) : SettingsAction
    data object OnBackClick : SettingsAction
}
