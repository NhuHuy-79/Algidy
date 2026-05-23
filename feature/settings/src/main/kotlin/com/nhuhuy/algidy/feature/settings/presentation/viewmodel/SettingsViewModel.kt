package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.onFailure
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.settings.domain.usecase.DeleteAllDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ManageDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ObserveSettingStateUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SelectSettingUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SetToggleSettingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    observeSettingStateUseCase: ObserveSettingStateUseCase,
    private val setToggleSettingUseCase: SetToggleSettingUseCase,
    private val selectSettingUseCase: SelectSettingUseCase,
    private val manageDataUseCase: ManageDataUseCase,
    private val deleteDataUseCase: DeleteAllDataUseCase,
) : BaseViewModel<SettingsUiState, SettingsEvent, SettingsAction>() {

    private val _overlay = MutableStateFlow(SettingsOverlay.NONE)
    val overlay = _overlay.asStateFlow()

    override val uiState: StateFlow<SettingsUiState> = observeSettingStateUseCase()
        .map { settingData ->
            SettingsUiState(
                isDynamicColor = settingData.enableDynamicColor,
                darkMode = settingData.darkMode,
                language = settingData.language,
                isNotificationsEnabled = settingData.enableNotifications,
                isBiometricLock = settingData.enableBiometricsLock,
                font = settingData.font,
                categoryEnabled = settingData.enabledCategoryGroup
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

            is SettingsAction.ExportData -> viewModelScope.launch {
                manageDataUseCase.exportData()
                    .onSuccess { emitEvent(SettingsEvent.ExportData.SUCCESS) }
                    .onFailure { emitEvent(SettingsEvent.ExportData.FAILURE) }
            }

            is SettingsAction.ImportData -> viewModelScope.launch {
                manageDataUseCase.importDate(action.uri.toString())
                    .onSuccess { emitEvent(SettingsEvent.ImportData.SUCCESS) }
                    .onFailure { emitEvent(SettingsEvent.ImportData.FAILURE) }
            }

            SettingsAction.ClearData -> _overlay.update { SettingsOverlay.DELETE_ALERT_DIALOG }

            SettingsAction.DeleteAlertDialog.Confirm -> viewModelScope.launch {
                _overlay.update { SettingsOverlay.NONE }
                deleteDataUseCase()
            }

            SettingsAction.DeleteAlertDialog.Dismiss -> _overlay.update { SettingsOverlay.NONE }
            is SettingsAction.ToggleCategoryGroup -> viewModelScope.launch {
                setToggleSettingUseCase.toggleCategoryGroup(action.enabled)
            }
        }
    }
}
