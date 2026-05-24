package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import com.nhuhuy.algidy.core.presentation.viewmodel.UiEvent

sealed interface SettingsEvent : UiEvent {
    data object NavigateBack : SettingsEvent
    enum class ExportData : SettingsEvent {
        SUCCESS, FAILURE
    }

    sealed interface ImportData : SettingsEvent {
        data object Success : ImportData
        data object Failure : ImportData
        data object PickUri : ImportData
    }
}
