package com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.UiResult
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.ScannerMode

@Immutable
data class ScannerUiState(
    val barCodeString: String = "",
    val barCodeInput: String = "",
    val scannerMode: ScannerMode = ScannerMode.BARCODE_SCANNER,
    val isAutoScanned: Boolean = true,
    val isFlashOn: Boolean = false,
    val scanResult: UiResult<FoodItem> = UiResult.Idle,
    val overlay: ScannerOverlay = ScannerOverlay.NONE,
    val labelEvent: LabelEvent = LabelEvent.SCANNING,
    val errorScannerCount: Int = 0,
    val expiryDate: String? = null,
) : UiState

enum class LabelEvent {
    NONE, AUTO_OFF, SCANNING, FAILURE, ADD_MANUALLY
}

enum class ScannerOverlay{
    NONE, LOADING_DIALOG, BARCODE_DIALOG, WARNING_DIALOG
}

