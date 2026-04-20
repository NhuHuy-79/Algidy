package com.nhuhuy.algidy.feature.scanner.viewmodel

import com.nhuhuy.algidy.feature.scanner.presentation.component.ScannerMode

sealed interface ScannerAction {
    data class OnScannerModeChange(val mode: ScannerMode) : ScannerAction
    data class OnFlashChange(val isFlashOn: Boolean) : ScannerAction
    data class OnAutoScanChange(val isAutoScanned: Boolean) : ScannerAction
    data class OnResultDetected(val barcodeString: String) : ScannerAction
}
