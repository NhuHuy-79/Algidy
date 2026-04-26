package com.nhuhuy.algidy.feature.scanner.viewmodel

sealed interface ScannerEvent {
    data class OnSuccess(val foodId: String) : ScannerEvent
}