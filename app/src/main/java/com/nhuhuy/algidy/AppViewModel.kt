package com.nhuhuy.algidy

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.feature.settings.domain.usecase.ObserveSettingStateUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Immutable
data class AppUiState(
    val language: AppLanguage = AppLanguage.ENGLISH,
    val darkMode: DarkMode = DarkMode.SYSTEM,
    val isDynamicColors: Boolean = false,
    val isBiometricLock: Boolean = false,
    val isSplashScreen: Boolean = true,
)

class AppViewModel(
    observeSettingStateUseCase: ObserveSettingStateUseCase
) : ViewModel() {
    val appUiState: StateFlow<AppUiState> = observeSettingStateUseCase().map { settingData ->
        AppUiState(
            darkMode = settingData.darkMode,
            isDynamicColors = settingData.enableDynamicColor,
            isBiometricLock = settingData.enableBiometricsLock,
            language = settingData.language,
            isSplashScreen = false
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), AppUiState()
    )
}