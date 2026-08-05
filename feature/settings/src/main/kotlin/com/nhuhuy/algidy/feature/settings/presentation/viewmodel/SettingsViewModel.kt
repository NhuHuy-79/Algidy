package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.AppNewFeaturesReader
import com.nhuhuy.algidy.core.data.util.onFailure
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.navigation.Destination.Setting
import com.nhuhuy.algidy.core.presentation.navigation.Navigator
import com.nhuhuy.algidy.core.presentation.navigation.SettingDestination
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.settings.domain.usecase.CheckCapabilityUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.DeleteAllDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ManageDataUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ObserveSettingStateUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.UpdatePreferencesUseCase
import com.nhuhuy.algidy.feature.settings.presentation.model.ClickableType
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingSliderItem
import com.nhuhuy.algidy.feature.settings.presentation.model.ToggleType
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsEvent.ShowSnackBar
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
    private val manageDataUseCase: ManageDataUseCase,
    private val checkCapabilityUseCase: CheckCapabilityUseCase,
    private val deleteDataUseCase: DeleteAllDataUseCase,
    //refactor
    private val updatePreferencesUseCase: UpdatePreferencesUseCase,
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
    ) { settingPrefs, capabilities ->
        SettingsCombineState(
            notificationGranted = capabilities.notificationGranted,
            biometricSupported = capabilities.biometricSupported,
            biometricEnabled = settingPrefs.enableBiometric,
            dynamicColorSupported = capabilities.dynamicColorSupported,
            appearancePreferences = settingPrefs.appearancePreferences,
            notificationPreferences = settingPrefs.notificationPreferences,
            generalPreferences = settingPrefs.generalPreferences
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
                updatePreferencesUseCase.updateAppearance(
                    currentCombineState.appearancePreferences.copy(
                        darkMode = action.darkMode
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
                if (!action.granted) {
                    emitEvent(ShowSnackBar("Permission denied. Notifications disabled."))
                }
            }

            SettingsAction.DeleteAlertDialog.Confirm -> viewModelScope.launch {
                _uiState.product { copy(overlay = SettingsOverlay.None) }
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
                    Setting(
                        SettingDestination.OpenSource
                    )
                )

                ClickableType.PrivacyPolicy -> _uiState.product {
                    copy(overlay = SettingsOverlay.PolicySheet)
                }

                is ClickableType.Language -> _uiState.product {
                    copy(
                        overlay = SettingsOverlay.LanguageSheet(
                            currentLanguage = action.type.currentLanguage
                        )
                    )
                }
            }
        }
    }

    private fun onToggleAction(action: SettingsAction.ToggleAction) {
        viewModelScope.launch {
            when (action.type) {
                ToggleType.BIOMETRIC_AUTH -> {
                    updatePreferencesUseCase.updateBiometric(action.enabled)
                }

                ToggleType.DYNAMIC_COLOR -> {
                    updatePreferencesUseCase.updateAppearance(
                        currentCombineState.appearancePreferences.copy(
                            enableDynamicColor = action.enabled
                        )
                    )
                }

                ToggleType.CATEGORY_GROUP -> {
                    updatePreferencesUseCase.updateAppearance(
                        currentCombineState.appearancePreferences.copy(
                            enableCategoryGroup = action.enabled
                        )
                    )
                }
                ToggleType.NOTIFICATION -> {
                    if (action.enabled && !currentCombineState.notificationGranted) {
                        emitEvent(SettingsEvent.RequestNotificationPermission)
                    } else {
                        updatePreferencesUseCase.updateNotification(
                            currentCombineState.notificationPreferences.copy(
                                enableNotification = action.enabled
                            )
                        )
                    }
                }

                ToggleType.WEEKLY_REPORT -> {
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
                is SettingSliderItem.ExpiredDeleteThreshold -> {
                    updatePreferencesUseCase.updateNotification(
                        currentCombineState.notificationPreferences.copy(
                            //Threshold Weeks
                            deleteFoodThresholdDayInWeek = action.value * 7
                        )
                    )
                }

                is SettingSliderItem.ExpiryWarningThreshold -> {
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
                _uiState.product { copy(overlay = SettingsOverlay.None) }
                updatePreferencesUseCase.updateNotification(
                    currentCombineState.notificationPreferences.copy(
                        hour = action.hour,
                        minutes = action.minutes
                    )
                )
                emitEvent(NotifyTimerEvent.Success)
            }
        }
    }
}
