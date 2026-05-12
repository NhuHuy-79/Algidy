package com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel

import com.nhuhuy.algidy.core.presentation.UiError

sealed interface ScannerEvent {
    data class OnSuccess(val foodId: String) : ScannerEvent
    data class OnFailure(val error: UiError) : ScannerEvent
}
