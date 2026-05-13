package com.nhuhuy.algidy.feature.scanner.presentation.confirm.viewmodel

import androidx.compose.runtime.Stable

import com.nhuhuy.algidy.core.presentation.viewmodel.UiEvent

@Stable
sealed interface ConfirmEvent : UiEvent {
    data object OnSaveSuccessfully : ConfirmEvent
    data object OnImageChangeFailed : ConfirmEvent
}
