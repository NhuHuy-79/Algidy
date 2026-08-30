package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.AppNewFeaturesReader
import com.nhuhuy.algidy.core.data.util.onFailure
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.domain.usecase.CheckUpdateUseCase
import com.nhuhuy.algidy.core.notifications.worker.WorkerScheduler
import com.nhuhuy.algidy.core.presentation.UiResult
import com.nhuhuy.algidy.core.presentation.navigation.Destination.Setting
import com.nhuhuy.algidy.core.presentation.navigation.Navigator
import com.nhuhuy.algidy.core.presentation.navigation.SettingDestination
import com.nhuhuy.algidy.core.presentation.toUiResult
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.settings.data.WidgetExceptionLogger
import com.nhuhuy.algidy.feature.settings.domain.usecase.CheckCapabilityUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.DeleteAllDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ManageDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ObserveSettingStateUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.UpdatePreferencesUseCase
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingClickableUiModel
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingSliderType
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingToggleType
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsEvent.ShowSnackBar
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsOverlay.LanguageSheet
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsOverlay.NewFeatureSheet
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsOverlay.None
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsOverlay.WidgetDebugSheet
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
    private val widgetExceptionLogger: WidgetExceptionLogger,
    private val navigator: Navigator,
    private val manageDataUseCase: ManageDataUseCase,
    private val checkCapabilityUseCase: CheckCapabilityUseCase,
    private val deleteDataUseCase: DeleteAllDataUseCase,
    private val updatePreferencesUseCase: UpdatePreferencesUseCase,
    private val workerScheduler: WorkerScheduler,
    private val checkUpdateUseCase: CheckUpdateUseCase,
) : BaseViewModel<SettingsUiState, SettingsEvent, SettingsAction>() {
    private val _uiState = MutableStateFlow(
        SettingsUiState(
            versionFeatures = appNewFeaturesReader.getWhatsNewContent(),
            versionName = appNewFeaturesReader.currentVersionName
        )
    )
    override val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    val combineState: StateFlow<SettingsCombineState> = combine(
        observeSettingStateUseCase.observe(),
        checkCapabilityUseCase.observe(),
        widgetExceptionLogger.latestLog(),
    ) { settingPrefs, capabilities, log ->
        SettingsCombineState(
            notificationGranted = capabilities.notificationGranted,
            biometricSupported = capabilities.biometricSupported,
            biometricEnabled = settingPrefs.enableBiometric,
            dynamicColorSupported = capabilities.dynamicColorSupported,
            appearancePreferences = settingPrefs.appearancePreferences,
            notificationPreferences = settingPrefs.notificationPreferences,
            generalPreferences = settingPrefs.generalPreferences,
            exceptionLog = log
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
                copy(overlay = None, checkUpdateResult = UiResult.Idle)
            }

            SettingsAction.OnBackClick -> navigator.navigateBack()

            is SettingsAction.SetDarkMode -> viewModelScope.launch {
                updatePreferencesUseCase.updateAppearance(
                    currentCombineState.appearancePreferences.copy(
                        themeMode = action.themeMode
                    )
                )
            }

            is SettingsAction.ChangeLanguage -> viewModelScope.launch {
                updatePreferencesUseCase.updateAppearance(
                    currentCombineState.appearancePreferences.copy(
                        appLanguage = action.language
                    )
                )
                navigator.navigateBack()
            }

            is SettingsAction.ChangeFont -> viewModelScope.launch {
            }

            is SettingsAction.SetWarningDays -> viewModelScope.launch {
                updatePreferencesUseCase.updateNotification(
                    currentCombineState.notificationPreferences.copy(
                        warningFoodThresholdDays = action.days
                    )
                )
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
                updatePreferencesUseCase.updateNotification(
                    currentCombineState.notificationPreferences.copy(
                        enableNotification = action.granted
                    )
                )
                workerScheduler.scheduleCheckExpiryWorker(forceReplace = true)
                if (!action.granted) {
                    emitEvent(ShowSnackBar("Permission denied. Notifications disabled."))
                }
            }

            SettingsAction.DeleteAlertDialog.Confirm -> viewModelScope.launch {
                _uiState.product { copy(overlay = None) }
                deleteDataUseCase()
                    .onSuccess { emitEvent(DeleteAll.Success) }
                    .onFailure { emitEvent(DeleteAll.Failure) }
            }

            is SettingsAction.SetDeleteThresholdDays -> viewModelScope.launch {
                updatePreferencesUseCase.updateNotification(
                    currentCombineState.notificationPreferences.copy(
                        deleteFoodThresholdDayInWeek = action.thresholdDays
                    )
                )
            }

            is SettingsAction.SliderAction -> onSliderAction(action)
            is SettingsAction.SetSeedColorPreset -> viewModelScope.launch {
                updatePreferencesUseCase.updateAppearance(
                    currentCombineState.appearancePreferences.copy(
                        seedColor = action.seedColor
                    )
                )
            }

            SettingsAction.ClearLog -> viewModelScope.launch {
                widgetExceptionLogger.clear()
            }

            SettingsAction.OnGithubClick -> emitEvent(SettingsEvent.NavigateToGithub)
        }
    }

    private fun onClickableAction(action: SettingsAction.ClickableAction) {
        viewModelScope.launch {
            when (action.type) {
                SettingClickableUiModel.EXPORT -> {
                    manageDataUseCase.exportData()
                        .onSuccess { emitEvent(SettingsEvent.ExportData.SUCCESS) }
                        .onFailure { emitEvent(SettingsEvent.ExportData.FAILURE) }
                }

                SettingClickableUiModel.IMPORT -> emitEvent(SettingsEvent.ImportData.PickUri)
                SettingClickableUiModel.DELETE_ALL -> _uiState.product { copy(overlay = SettingsOverlay.DeleteAlertDialog) }
                SettingClickableUiModel.ABOUT -> {
                    navigator.navigateTo(Setting(SettingDestination.AboutApp))
                }

                SettingClickableUiModel.DAILY_REMINDER -> {
                    _uiState.product { copy(overlay = SettingsOverlay.TimePicker) }
                }

                SettingClickableUiModel.NEW_FEATURES -> {
                    currentState.versionFeatures?.let {
                        _uiState.product {
                            copy(overlay = NewFeatureSheet(it))
                        }
                    }
                }

                SettingClickableUiModel.COPYRIGHT -> _uiState.product {
                    copy(overlay = SettingsOverlay.CopyrightSheet)
                }

                SettingClickableUiModel.FEEDBACK -> {
                    emitEvent(SettingsEvent.SendFeedBackEmail)
                }

                SettingClickableUiModel.OPEN_SOURCE -> navigator.navigateTo(
                    Setting(
                        SettingDestination.OpenSource
                    )
                )

                SettingClickableUiModel.PRIVACY_POLICY -> _uiState.product {
                    copy(overlay = SettingsOverlay.PolicySheet)
                }

                SettingClickableUiModel.LANGUAGE -> _uiState.product {
                    copy(
                        overlay = LanguageSheet(currentLanguage = currentCombineState.appearancePreferences.appLanguage)
                    )
                }

                SettingClickableUiModel.DEBUG -> _uiState.product {
                    copy(overlay = WidgetDebugSheet)
                }

                SettingClickableUiModel.APPEARANCE -> navigator.navigateTo(
                    Setting(
                        SettingDestination.Appearance
                    )
                )

                SettingClickableUiModel.NOTIFICATION -> navigator.navigateTo(
                    Setting(
                        SettingDestination.Notification
                    )
                )

                SettingClickableUiModel.YOUR_DATA -> navigator.navigateTo(Setting(SettingDestination.YourData))
                SettingClickableUiModel.OTHER_SETTING -> navigator.navigateTo(
                    Setting(
                        SettingDestination.OtherSettings
                    )
                )

                SettingClickableUiModel.CHECK_UPDATE -> {
                    _uiState.product {
                        copy(
                            overlay = SettingsOverlay.CheckUpdateDialog,
                            checkUpdateResult = UiResult.Loading
                        )
                    }
                    val uiResult = checkUpdateUseCase()
                    _uiState.product {
                        copy(checkUpdateResult = uiResult.toUiResult())
                    }
                }
            }
        }
    }

    private fun onToggleAction(action: SettingsAction.ToggleAction) {
        viewModelScope.launch {
            when (action.type) {
                SettingToggleType.BIOMETRIC_AUTH -> {
                    updatePreferencesUseCase.updateBiometric(action.enabled)
                }

                SettingToggleType.DYNAMIC_COLOR -> {
                    updatePreferencesUseCase.updateAppearance(
                        currentCombineState.appearancePreferences.copy(
                            enableDynamicColor = action.enabled
                        )
                    )
                }

                SettingToggleType.CATEGORY_GROUP -> {
                    updatePreferencesUseCase.updateAppearance(
                        currentCombineState.appearancePreferences.copy(
                            enableCategoryGroup = action.enabled
                        )
                    )
                }

                SettingToggleType.NOTIFICATION -> {
                    if (action.enabled && !currentCombineState.notificationGranted) {
                        emitEvent(SettingsEvent.RequestNotificationPermission)
                    } else {
                        updatePreferencesUseCase.updateNotification(
                            currentCombineState.notificationPreferences.copy(
                                enableNotification = action.enabled
                            )
                        )
                        workerScheduler.scheduleCheckExpiryWorker(forceReplace = true)
                    }
                }

                SettingToggleType.WEEKLY_REPORT -> {
                    updatePreferencesUseCase.updateNotification(
                        currentCombineState.notificationPreferences.copy(
                            enableWeeklyReport = action.enabled
                        )
                    )
                }
            }
        }
    }

    private fun onSliderAction(action: SettingsAction.SliderAction) {
        viewModelScope.launch {
            when (action.type) {
                SettingSliderType.EXPIRED_DELETE_THRESHOLD -> {
                    updatePreferencesUseCase.updateNotification(
                        currentCombineState.notificationPreferences.copy(
                            //Threshold Weeks
                            deleteFoodThresholdDayInWeek = action.value * 7
                        )
                    )
                }

                SettingSliderType.EXPIRY_WARNING_THRESHOLD -> {
                    updatePreferencesUseCase.updateNotification(
                        currentCombineState.notificationPreferences.copy(
                            warningFoodThresholdDays = action.value
                        )
                    )
                }
            }
        }

    }

    private fun onSetNotifyTimeAction(action: SettingsAction.SetNotifyTime) {
        when (action) {
            SettingsAction.SetNotifyTime.OpenPicker -> _uiState.product { copy(overlay = SettingsOverlay.TimePicker) }
            is SettingsAction.SetNotifyTime.SetHourAndMinutes -> viewModelScope.launch {
                _uiState.product { copy(overlay = None) }
                updatePreferencesUseCase.updateNotification(
                    currentCombineState.notificationPreferences.copy(
                        hour = action.hour,
                        minutes = action.minutes
                    )
                )
                workerScheduler.scheduleCheckExpiryWorker(forceReplace = true)
                emitEvent(NotifyTimerEvent.Success)
            }
        }
    }
}
