package com.nhuhuy.algidy.feature.presentation.scanner.viewmodel

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.UiResult
import com.nhuhuy.algidy.feature.presentation.scanner.ScannerMode

@Immutable
data class ScannerUiState(
    val barCodeString: String = "",
    val scannerMode: ScannerMode = ScannerMode.BARCODE_SCANNER,
    val isAutoScanned: Boolean = true,
    val isFlashOn: Boolean = false,
    val scanResult: UiResult<FoodItem> = UiResult.Idle,
    val foodItemResult: FoodItem = FoodItem(),
    val overlay: ScannerOverlay = ScannerOverlay.NONE,
    val labelEvent: LabelEvent = LabelEvent.NONE,
    val stagedImageUri: Uri? = null,
) {
    val isWaitingForConfirmation: Boolean get() = stagedImageUri != null
}

enum class LabelEvent {
    NONE, AUTO_OFF, SCANNING, FAILURE
}

enum class ScannerOverlay{
    NONE, SUCCESS_SHEET, LOADING_DIALOG, ERROR_DIALOG, PROCESSING_DIALOG
}

