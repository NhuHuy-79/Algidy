package com.nhuhuy.algidy.feature.presentation.scanner.viewmodel

import com.nhuhuy.algidy.core.model.UiError

sealed interface ScannerEvent {
    data class OnSuccess(val foodId: String) : ScannerEvent
    data class OnFailure(val error: UiError) : ScannerEvent
}