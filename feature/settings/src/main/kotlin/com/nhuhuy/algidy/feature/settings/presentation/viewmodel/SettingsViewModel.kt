package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.onFailure
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.settings.domain.usecase.CheckCapabilityUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.DeleteAllDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ManageDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ObserveSettingStateUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SelectSettingUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.SetToggleSettingUseCase
import com.nhuhuy.algidy.feature.settings.presentation.model.ClickableType
import com.nhuhuy.algidy.feature.settings.presentation.model.ToggleType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    observeSettingStateUseCase: ObserveSettingStateUseCase,
    checkCapabilityUseCase: CheckCapabilityUseCase,
    private val setToggleSettingUseCase: SetToggleSettingUseCase,
    private val selectSettingUseCase: SelectSettingUseCase,
    private val manageDataUseCase: ManageDataUseCase,
    private val deleteDataUseCase: DeleteAllDataUseCase,
) : BaseViewModel<SettingsUiState, SettingsEvent, SettingsAction>() {
    private val _overlay = MutableStateFlow(SettingsOverlay.NONE)
    val overlay = _overlay.asStateFlow()

    override val uiState: StateFlow<SettingsUiState> = combine(
        observeSettingStateUseCase(),
        checkCapabilityUseCase.observe(),
    ) { settingData, capabilities ->
        SettingsUiState(
            notificationGranted = capabilities.notificationGranted,
            biometricSupported = capabilities.biometricSupported,
            dynamicColorSupported = capabilities.dynamicColorSupported,
            dynamicColorEnabled = settingData.enableDynamicColor,
            darkMode = settingData.darkMode,
            language = settingData.language,
            notificationsEnabled = settingData.enableNotifications,
            biometricEnabled = settingData.enableBiometricsLock,
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

            SettingsAction.OnBackClick -> {
                emitEvent(SettingsEvent.NavigateBack)
            }

            is SettingsAction.ChangeLanguage -> viewModelScope.launch {
                selectSettingUseCase.selectAppLanguage(action.language)
            }


            is SettingsAction.ChangeFont -> viewModelScope.launch {
                selectSettingUseCase.selectAppFont(action.font)
            }

            SettingsAction.DeleteAlertDialog.Confirm -> viewModelScope.launch {
                _overlay.update { SettingsOverlay.NONE }
                deleteDataUseCase()
            }

            SettingsAction.DeleteAlertDialog.Dismiss -> _overlay.update { SettingsOverlay.NONE }

            is SettingsAction.ToggleAction -> onToggleAction(action)
            is SettingsAction.ClickableAction -> onClickableAction(action)

            is SettingsAction.ImportData -> viewModelScope.launch {
                val uri = action.uri
                manageDataUseCase.importDate(uri.toString())
                    .onSuccess { emitEvent(SettingsEvent.ImportData.Success) }
                    .onFailure { emitEvent(SettingsEvent.ImportData.Failure) }
            }
        }
    }

    private fun onClickableAction(action: SettingsAction.ClickableAction) {
        viewModelScope.launch {
            when (action.type) {
                ClickableType.Export -> {
                    manageDataUseCase.exportData()
                        .onSuccess { emitEvent(SettingsEvent.ExportData.SUCCESS) }
                        .onFailure { emitEvent(SettingsEvent.ExportData.FAILURE) }
                }

                is ClickableType.Import -> {
                    emitEvent(SettingsEvent.ImportData.PickUri)
                }

                ClickableType.AboutApp -> {
                    // About app

                }

                ClickableType.DeleteAll -> _overlay.update { SettingsOverlay.DELETE_ALERT_DIALOG }
            }
        }
    }

    private fun onToggleAction(action: SettingsAction.ToggleAction) {
        viewModelScope.launch {
            when (action.type) {
                ToggleType.BIOMETRIC_AUTH -> setToggleSettingUseCase.toggleBiometricLock(action.enabled)
                ToggleType.DYNAMIC_COLOR -> setToggleSettingUseCase.toggleDynamicColor(action.enabled)
                ToggleType.NOTIFICATION -> setToggleSettingUseCase.toggleNotifications(action.enabled)
                ToggleType.CATEGORY_GROUP -> setToggleSettingUseCase.toggleCategoryGroup(action.enabled)
            }
        }
    }
}
