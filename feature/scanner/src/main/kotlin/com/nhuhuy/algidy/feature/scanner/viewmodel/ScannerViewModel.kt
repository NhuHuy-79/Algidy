@file:OptIn(FlowPreview::class)

package com.nhuhuy.algidy.feature.scanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.UiStateResult
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.toUiStateResult
import com.nhuhuy.algidy.core.data.util.onFailure
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.feature.scanner.presentation.component.ScannerMode
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber


class ScannerViewModel(
    private val foodRepository: FoodRepository
) : ViewModel() {
    private val _uiSate = MutableStateFlow(ScannerUiState())
    val uiState = _uiSate.asStateFlow()
    private val stateValue : ScannerUiState get() = uiState.value
    private val _barcodeEvents = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val barcodeEvents = _barcodeEvents.asSharedFlow()

    private var lastProcessedBarcode: String? = null

    init {
        observeBarcodeEvents()
    }

    fun onAction(action: ScannerAction) {
        when (action) {
            is ScannerAction.OnScannerModeChange -> onScannerModeChange(action.mode)
            is ScannerAction.OnAutoScanChange -> onAutoScannerChange(action.isAutoScanned)
            is ScannerAction.OnFlashChange -> onFlashChange(action.isFlashOn)
            is ScannerAction.OnResultDetected -> {
                val mode: ScannerMode = stateValue.scannerMode
                when (mode) {
                    ScannerMode.BARCODE_SCANNER -> onBarcodeDetect(action.barcodeString)
                    ScannerMode.FOOD_SCANNER -> { // onFood
                    }
                }
            }
        }
    }

    private fun observeBarcodeEvents(){
        _barcodeEvents
            .filter { barcode ->
                val isNotLoading = stateValue.scanResult !is UiStateResult.Loading
                val isNewItem = barcode != lastProcessedBarcode
                isNotLoading && isNewItem
            }
            .distinctUntilChanged()
            .onEach { barcode ->
                Timber.d("Barcode detected: $barcode")
                onBarcodeScan(barcode)
            }
            .launchIn(viewModelScope)
    }

    private fun onBarcodeScan(barcodeString: String) {
        viewModelScope.launch {
            lastProcessedBarcode = barcodeString
            _uiSate.update { it.copy(scanResult = UiStateResult.Loading) }
            val result = foodRepository.scanFoodBarcode(barcodeString)
                .onSuccess {  }
                .onFailure {}
            _uiSate.update { it.copy(scanResult = result.toUiStateResult()) }
        }
    }


    private fun onBarcodeDetect(barcodeString: String){
        _barcodeEvents.tryEmit(barcodeString)
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