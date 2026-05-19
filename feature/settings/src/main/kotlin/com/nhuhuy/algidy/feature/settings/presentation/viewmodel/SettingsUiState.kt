package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState

@Immutable
data class SettingsUiState(
    val darkMode: DarkMode = DarkMode.SYSTEM,
    val isDarkMode: Boolean = false,
    val isNotificationsEnabled: Boolean = true,
    val language: String = "English",
    val isBiometricLock: Boolean = false,
) : UiState
