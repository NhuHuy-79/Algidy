package com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel

import android.net.Uri
import com.nhuhuy.algidy.core.presentation.viewmodel.UiAction

sealed interface ScannerAction : UiAction {
    data object OnDismissRequest : ScannerAction
    data class OnFlashChange(val isFlashOn: Boolean) : ScannerAction
    data class OnAutoScanChange(val isAutoScanned: Boolean) : ScannerAction
    data class OnBarcodeDetected(val barcode: String) : ScannerAction
    data class OnImageStaged(val uri: Uri?) : ScannerAction
    data object OnBarcodeAddManual : ScannerAction
}

sealed interface WarningDialogAction : ScannerAction {
    data object Confirm : WarningDialogAction
}

sealed interface AddBarcodeDialogAction : ScannerAction {
    data class OnValueChange(val value: String) : AddBarcodeDialogAction
    data object OnConfirm : AddBarcodeDialogAction
}
