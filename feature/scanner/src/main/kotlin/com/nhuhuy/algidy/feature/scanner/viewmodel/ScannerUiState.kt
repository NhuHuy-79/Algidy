package com.nhuhuy.algidy.feature.scanner.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.data.UiStateResult
import com.nhuhuy.algidy.feature.scanner.presentation.component.ScannerMode

@Immutable
data class ScannerUiState(
    val barCodeString: String = "",
    val scannerMode: ScannerMode = ScannerMode.BARCODE_SCANNER,
    val isAutoScanned: Boolean = false,
    val isFlashOn: Boolean = false,
    val scanResult: UiStateResult<String> = UiStateResult.Idle
)
