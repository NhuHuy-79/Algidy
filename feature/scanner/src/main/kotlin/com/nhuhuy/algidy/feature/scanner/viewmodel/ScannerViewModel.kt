package com.nhuhuy.algidy.feature.scanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.feature.scanner.presentation.component.ScannerMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScannerViewModel(
    private val foodRepository: FoodRepository
) : ViewModel() {
    private val _uiSate = MutableStateFlow(ScannerUiState())
    val uiState = _uiSate.asStateFlow()
    private val stateValue : ScannerUiState get() = uiState.value

    fun onAction(action: ScannerAction) {
        when (action) {
            is ScannerAction.OnScannerModeChange -> onScannerModeChange(action.mode)
            is ScannerAction.OnAutoScanChange -> onAutoScannerChange(action.isAutoScanned)
            is ScannerAction.OnFlashChange -> onFlashChange(action.isFlashOn)
            is ScannerAction.OnResultDetected -> {
                val mode: ScannerMode = stateValue.scannerMode
                when (mode) {
                    ScannerMode.BARCODE_SCANNER -> onResultDetected(action.barcodeString)
                    ScannerMode.FOOD_SCANNER -> { // onFood
                    }
                }
            }
        }
    }

    private fun onResultDetected(barcodeString: String) {
        viewModelScope.launch {

        }
    }


    private fun onFlashChange(isFlashOn: Boolean) {
        _uiSate.update { it.copy(isFlashOn = isFlashOn) }
    }

    private fun onAutoScannerChange(isAutoScanned: Boolean) {
        _uiSate.update { it.copy(isAutoScanned = isAutoScanned) }
    }

    private fun onScannerModeChange(mode: ScannerMode) {
        _uiSate.update {
            it.copy(scannerMode = mode)
        }
    }
}