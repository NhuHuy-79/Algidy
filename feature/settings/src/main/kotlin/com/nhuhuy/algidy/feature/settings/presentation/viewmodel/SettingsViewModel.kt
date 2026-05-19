package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.settings.domain.usecase.ObserveSettingStateUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SetDarkModeUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SetToggleSettingUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    observeSettingStateUseCase: ObserveSettingStateUseCase,
    private val setToggleSettingUseCase: SetToggleSettingUseCase,
    private val setDarkModeUseCase: SetDarkModeUseCase
) : BaseViewModel<SettingsUiState, SettingsEvent, SettingsAction>() {

    override val uiState: StateFlow<SettingsUiState> = observeSettingStateUseCase()
        .map { settingData ->
            SettingsUiState(
                isDynamicColor = settingData.enableDynamicColor,
                darkMode = settingData.darkMode,
                isNotificationsEnabled = settingData.enableNotifications,
                isBiometricLock = settingData.enableBiometricsLock,
            )
        }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    override fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.SetDarkMode -> {
                viewModelScope.launch {
                    setDarkModeUseCase(action.darkMode)
                }
            }

            is SettingsAction.ToggleNotifications -> {
                viewModelScope.launch {
                    setToggleSettingUseCase.toggleNotifications(action.enabled)
                }
            }

            SettingsAction.OnBackClick -> {
                emitEvent(SettingsEvent.NavigateBack)
            }

            is SettingsAction.ChangeLanguage -> {
                // TODO: Implement language change
            }

            is SettingsAction.ToggleBiometricLock -> viewModelScope.launch {
                setToggleSettingUseCase.toggleBiometricLock(action.enabled)
            }

            is SettingsAction.ToggleDynamicColor -> viewModelScope.launch {
                setToggleSettingUseCase.toggleDynamicColor(action.enabled)
            }
        }
    }
}
