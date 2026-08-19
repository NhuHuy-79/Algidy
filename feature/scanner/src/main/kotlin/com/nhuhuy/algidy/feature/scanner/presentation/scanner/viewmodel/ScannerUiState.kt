package com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.UiResult
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState

@Immutable
data class ScannerUiState(
    val barCodeInput: String = "",
    val isAutoScanned: Boolean = true,
    val isFlashOn: Boolean = false,
    val scanResult: UiResult<FoodItem> = UiResult.Idle,
    val overlay: ScannerOverlay = ScannerOverlay.NONE,
    val labelEvent: LabelEvent = LabelEvent.SCANNING,
) : UiState

enum class LabelEvent {
    NONE, AUTO_OFF, SCANNING, FAILURE, ADD_MANUALLY
}

enum class ScannerOverlay{
    NONE, LOADING_DIALOG, BARCODE_DIALOG, WARNING_DIALOG
}

