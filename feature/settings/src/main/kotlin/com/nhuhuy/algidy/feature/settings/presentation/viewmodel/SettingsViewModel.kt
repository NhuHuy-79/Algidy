package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.settings.domain.usecase.ObserveSettingStateUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SelectSettingUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SetToggleSettingUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    observeSettingStateUseCase: ObserveSettingStateUseCase,
    private val setToggleSettingUseCase: SetToggleSettingUseCase,
    private val selectSettingUseCase: SelectSettingUseCase
) : BaseViewModel<SettingsUiState, SettingsEvent, SettingsAction>() {

    override val uiState: StateFlow<SettingsUiState> = observeSettingStateUseCase()
        .map { settingData ->
            SettingsUiState(
                isDynamicColor = settingData.enableDynamicColor,
                darkMode = settingData.darkMode,
                language = settingData.language,
                isNotificationsEnabled = settingData.enableNotifications,
                isBiometricLock = settingData.enableBiometricsLock,
                font = settingData.font
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
                    selectSettingUseCase.selectDarkMode(action.darkMode)
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

            is SettingsAction.ChangeLanguage -> viewModelScope.launch {
                selectSettingUseCase.selectAppLanguage(action.language)
            }

            is SettingsAction.ToggleBiometricLock -> viewModelScope.launch {
                setToggleSettingUseCase.toggleBiometricLock(action.enabled)
            }

            is SettingsAction.ToggleDynamicColor -> viewModelScope.launch {
                setToggleSettingUseCase.toggleDynamicColor(action.enabled)
            }

            is SettingsAction.ChangeFont -> viewModelScope.launch {
                selectSettingUseCase.selectAppFont(action.font)
            }
        }
    }
}
