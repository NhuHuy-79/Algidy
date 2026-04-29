package com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.UiResult
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.ScannerMode

@Immutable
data class ScannerUiState(
    val barCodeString: String = "",
    val scannerMode: ScannerMode = ScannerMode.BARCODE_SCANNER,
    val isAutoScanned: Boolean = true,
    val isFlashOn: Boolean = false,
    val scanResult: UiResult<FoodItem> = UiResult.Idle,
    val foodItemResult: FoodItem = FoodItem(),
    val overlay: ScannerOverlay = ScannerOverlay.NONE,
    val labelEvent: LabelEvent = LabelEvent.SCANNING,
    val stagedImageUri: Uri? = null,
)

enum class LabelEvent {
    NONE, AUTO_OFF, SCANNING, FAILURE
}

enum class ScannerOverlay{
    NONE, LOADING_DIALOG, PROCESSING_DIALOG
}

