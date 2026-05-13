package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import com.nhuhuy.algidy.core.presentation.viewmodel.UiState

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val isNotificationsEnabled: Boolean = true,
    val language: String = "English"
) : UiState
