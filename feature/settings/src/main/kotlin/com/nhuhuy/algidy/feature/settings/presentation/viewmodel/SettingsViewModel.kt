package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.datastore.SettingsDataStore
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDataStore: SettingsDataStore
) : BaseViewModel<SettingsUiState, SettingsEvent, SettingsAction>() {

    override val uiState: StateFlow<SettingsUiState> = combine(
        settingsDataStore.darkModeFlow,
        settingsDataStore.notificationsEnabledFlow
    ) { darkMode, notifications ->
        SettingsUiState(
            isDarkMode = darkMode,
            isNotificationsEnabled = notifications
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    override fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.ToggleDarkMode -> {
                viewModelScope.launch {
                    settingsDataStore.setDarkMode(action.enabled)
                }
            }

            is SettingsAction.ToggleNotifications -> {
                viewModelScope.launch {
                    settingsDataStore.setNotificationsEnabled(action.enabled)
                }
            }

            SettingsAction.OnBackClick -> {
                emitEvent(SettingsEvent.NavigateBack)
            }

            is SettingsAction.ChangeLanguage -> {
                // TODO: Implement language change
            }
        }
    }
}
