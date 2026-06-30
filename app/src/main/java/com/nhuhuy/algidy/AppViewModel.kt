package com.nhuhuy.algidy

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.model.setting.AppFont
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.feature.settings.domain.usecase.CheckCapabilityUseCase
import com.nhuhuy.algidy.feature.settings.domain.usecase.ObserveSettingStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
data class AppUiState(
    val font: AppFont = AppFont.DEFAULT,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val darkMode: DarkMode = DarkMode.SYSTEM,
    val isDynamicColors: Boolean = false,
    val isBiometricLock: Boolean = false,
    val isSplashScreen: Boolean = true,
)

sealed interface AppAction {
    data class UpdateBiometricSupported(val value: Boolean) : AppAction
    data class UpdateAppUnlock(val unlock: Boolean) : AppAction
    data object TriggerBiometric : AppAction
}

class AppViewModel(
    observeSettingStateUseCase: ObserveSettingStateUseCase,
    private val checkCapabilityUseCase: CheckCapabilityUseCase,
) : ViewModel() {
    private val _isUnLocked = MutableStateFlow(false)
    val isUnlocked = _isUnLocked.asStateFlow()

    private val _biometricEvent = MutableStateFlow(0)
    val biometricTrigger = _biometricEvent.asStateFlow()
    init {
        viewModelScope.launch {
            checkCapabilityUseCase.init()
        }
    }
    val appUiState: StateFlow<AppUiState> = observeSettingStateUseCase().map { settingData ->
        AppUiState(
            darkMode = settingData.darkMode,
            isDynamicColors = settingData.enableDynamicColor,
            isBiometricLock = settingData.enableBiometricsLock,
            language = settingData.language,
            font = settingData.font,
            isSplashScreen = false
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), AppUiState()
    )

    fun onAction(action: AppAction) {
        when (action) {
            is AppAction.UpdateBiometricSupported -> {
                viewModelScope.launch {
                    checkCapabilityUseCase.updateBiometric(action.value)
                }
            }
            is AppAction.TriggerBiometric -> _biometricEvent.update {
                it + 1
            }

            is AppAction.UpdateAppUnlock -> _isUnLocked.update { action.unlock }
        }
    }
}