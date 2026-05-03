package com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel

import android.net.Uri
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.feature.scanner.domain.model.FoodDate
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.ScannerMode

sealed interface ScannerAction {
    data object OnDismissRequest : ScannerAction
    data class OnScannerModeChange(val mode: ScannerMode) : ScannerAction
    data class OnFlashChange(val isFlashOn: Boolean) : ScannerAction
    data class OnAutoScanChange(val isAutoScanned: Boolean) : ScannerAction
    data class OnResultDetected(val barcodeString: String) : ScannerAction
    data class OnDateDetected(val foodDate: FoodDate) : ScannerAction
    data class OnFoodItemSaved(val foodItem: FoodItem) : ScannerAction
    data class OnImageStaged(val uri: Uri?) : ScannerAction
    data class OnProcessStagedImage(val uri: Uri) : ScannerAction
}
