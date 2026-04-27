package com.nhuhuy.algidy.feature.scanner.presentation.confirm.viewmodel

import androidx.compose.runtime.Stable

@Stable
sealed interface ConfirmEvent {
    data object OnSaveSuccessfully : ConfirmEvent
}