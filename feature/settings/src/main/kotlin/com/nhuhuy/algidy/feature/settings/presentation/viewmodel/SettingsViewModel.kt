package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.AppNewFeaturesReader
import com.nhuhuy.algidy.core.data.util.onFailure
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.navigation.Destination
import com.nhuhuy.algidy.core.presentation.navigation.Navigator
import com.nhuhuy.algidy.core.presentation.navigation.SettingDestination
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
import kotlinx.coroutines.launch

class SettingsViewModel(
    appNewFeaturesReader: AppNewFeaturesReader,
    observeSettingStateUseCase: ObserveSettingStateUseCase,
    private val navigator: Navigator,
    private val setToggleSettingUseCase: SetToggleSettingUseCase,
    private val selectSettingUseCase: SelectSettingUseCase,
    private val manageDataUseCase: ManageDataUseCase,
    private val checkCapabilityUseCase: CheckCapabilityUseCase,
    private val deleteDataUseCase: DeleteAllDataUseCase,
) : BaseViewModel<SettingsUiState, SettingsEvent, SettingsAction>() {
    private val _uiState = MutableStateFlow(
        SettingsUiState(
            versionFeatures = appNewFeaturesReader.getWhatsNewContent(),
            versionName = appNewFeaturesReader.currentVersionName
        )
    )
    override val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    val combineState: StateFlow<SettingsCombineState> = combine(
        observeSettingStateUseCase(),
        checkCapabilityUseCase.observe(),
    ) { settingData, capabilities ->
        SettingsCombineState(
            notificationGranted = capabilities.notificationGranted,
            biometricSupported = capabilities.biometricSupported,
            dynamicColorSupported = capabilities.dynamicColorSupported,
            dynamicColorEnabled = settingData.enableDynamicColor,
            darkMode = settingData.darkMode,
            language = settingData.language,
            notificationsEnabled = settingData.enableNotifications,
            biometricEnabled = settingData.enableBiometricsLock,
            font = settingData.font,
            categoryEnabled = settingData.enabledCategoryGroup,
            hour = settingData.hour,
            minutes = settingData.minute,
            warningDays = settingData.warningDay,
            weeklyReport = settingData.weeklyReport
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsCombineState()
        )

    val currentCombineState get() = combineState.value


    override fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnDismiss,
            SettingsAction.DeleteAlertDialog.Dismiss -> _uiState.product {
                copy(overlay = SettingsOverlay.None)
            }

            SettingsAction.OnBackClick -> navigator.navigateBack()

            is SettingsAction.SetDarkMode -> viewModelScope.launch {
                selectSettingUseCase.selectDarkMode(action.darkMode)
            }

            is SettingsAction.ChangeLanguage -> viewModelScope.launch {
                selectSettingUseCase.selectAppLanguage(action.language)
                navigator.navigateBack()
            }

            is SettingsAction.ChangeFont -> viewModelScope.launch {
                selectSettingUseCase.selectAppFont(action.font)
            }

            is SettingsAction.SetWarningDays -> viewModelScope.launch {
                selectSettingUseCase.selectDayWarning(action.days)
            }

            is SettingsAction.ToggleAction -> onToggleAction(action)
            is SettingsAction.ClickableAction -> onClickableAction(action)

            is SettingsAction.ImportData -> viewModelScope.launch {
                manageDataUseCase.importData(action.uri.toString())
                    .onSuccess { emitEvent(SettingsEvent.ImportData.Success) }
                    .onFailure { emitEvent(SettingsEvent.ImportData.Failure) }
            }

            is SettingsAction.SetNotifyTime -> onSetNotifyTimeAction(action)

            is SettingsAction.OnNotificationGranted -> viewModelScope.launch {
                checkCapabilityUseCase.updateNotification(action.granted)
                setToggleSettingUseCase.toggleNotifications(action.granted)
                if (!action.granted) {
                    emitEvent(SettingsEvent.ShowSnackBar("Permission denied. Notifications disabled."))
                }
            }

            SettingsAction.DeleteAlertDialog.Confirm -> viewModelScope.launch {
                _uiState.product { copy(overlay = SettingsOverlay.None) }
                deleteDataUseCase()
                emitEvent(DeleteAll.Success)
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

                is ClickableType.Import -> emitEvent(SettingsEvent.ImportData.PickUri)
                ClickableType.DeleteAll -> _uiState.product { copy(overlay = SettingsOverlay.DeleteAlertDialog) }
                ClickableType.AboutApp -> {
                    currentState.versionFeatures?.let { newFeatures ->
                        _uiState.product {
                            copy(overlay = SettingsOverlay.NewFeatureSheet(newFeatures))
                        }
                    }
                }

                ClickableType.DailyReminder -> {
                    _uiState.product { copy(overlay = SettingsOverlay.TimePicker) }
                }

                ClickableType.NewFeatures -> {
                    currentState.versionFeatures?.let {
                        _uiState.product {
                            copy(overlay = SettingsOverlay.NewFeatureSheet(it))
                        }
                    }
                }

                ClickableType.CopyRight -> _uiState.product {
                    copy(overlay = SettingsOverlay.CopyrightSheet)
                }

                ClickableType.Feedback -> {
                    emitEvent(SettingsEvent.SendFeedBackEmail)
                }

                ClickableType.OpenSource -> navigator.navigateTo(
                    Destination.Setting(
                        SettingDestination.OpenSource
                    )
                )

                ClickableType.PrivacyPolicy -> _uiState.product {
                    copy(overlay = SettingsOverlay.PolicySheet)
                }
            }
        }
    }

    private fun onToggleAction(action: SettingsAction.ToggleAction) {
        viewModelScope.launch {
            when (action.type) {
                ToggleType.BIOMETRIC_AUTH -> setToggleSettingUseCase.toggleBiometricLock(action.enabled)
                ToggleType.DYNAMIC_COLOR -> setToggleSettingUseCase.toggleDynamicColor(action.enabled)
                ToggleType.CATEGORY_GROUP -> setToggleSettingUseCase.toggleCategoryGroup(action.enabled)
                ToggleType.NOTIFICATION -> {
                    if (action.enabled && !currentCombineState.notificationGranted) {
                        emitEvent(SettingsEvent.RequestNotificationPermission)
                    } else {
                        setToggleSettingUseCase.toggleNotifications(action.enabled)
                    }
                }

                ToggleType.WEEKLY_REPORT -> setToggleSettingUseCase.toggleWeeklyReport(action.enabled)
            }
        }
    }

    private fun onSetNotifyTimeAction(action: SettingsAction.SetNotifyTime) {
        when (action) {
            SettingsAction.SetNotifyTime.OpenPicker -> _uiState.product { copy(overlay = SettingsOverlay.TimePicker) }
            is SettingsAction.SetNotifyTime.SetHourAndMinutes -> viewModelScope.launch {
                _uiState.product { copy(overlay = SettingsOverlay.None) }
                selectSettingUseCase.selectNotifyTime(action.hour, action.minutes)
                emitEvent(NotifyTimerEvent.Success)
            }
        }
    }
}
