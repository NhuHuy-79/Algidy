package com.nhuhuy.algidy.feature.scanner.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.data.UiStateResult
import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.feature.scanner.presentation.ScannerMode

@Immutable
data class ScannerUiState(
    val barCodeString: String = "",
    val scannerMode: ScannerMode = ScannerMode.BARCODE_SCANNER,
    val isAutoScanned: Boolean = true,
    val isFlashOn: Boolean = false,
    val scanResult: UiStateResult<FoodItem> = UiStateResult.Idle,
    val foodItemResult: FoodItem = FoodItem(),
    val overlay: ScannerOverlay = ScannerOverlay.NONE,
)


enum class ScannerOverlay{
    NONE, SUCCESS_SHEET, LOADING_DIALOG, ERROR_DIALOG
}

@Stable
sealed interface ScannerSideEffect