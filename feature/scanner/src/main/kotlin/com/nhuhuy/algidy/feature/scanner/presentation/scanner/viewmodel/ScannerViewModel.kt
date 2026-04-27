@file:OptIn(FlowPreview::class)

package com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.util.onFailure
import com.nhuhuy.algidy.core.data.util.onSuspendSuccess
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.model.UiResult
import com.nhuhuy.algidy.core.model.toUiError
import com.nhuhuy.algidy.core.model.toUiStateResult
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.ScannerMode
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds


class ScannerViewModel(
    private val foodRepository: FoodRepository
) : ViewModel() {
    private val _scannerEvent: Channel<ScannerEvent> = Channel(Channel.BUFFERED)
    val scannerEvent = _scannerEvent.receiveAsFlow()
    private val _uiSate = MutableStateFlow(ScannerUiState())
    val uiState = _uiSate.asStateFlow()
    private val stateValue: ScannerUiState get() = uiState.value
    private val _barcodeEvents = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

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

            ScannerAction.OnDismissRequest -> {
                _uiSate.update { state ->
                    state.copy(overlay = ScannerOverlay.NONE)
                }
            }

            is ScannerAction.OnFoodItemSaved -> {
                _uiSate.update { state ->
                    state.copy(foodItemResult = action.foodItem)
                }
            }

            is ScannerAction.OnImageStaged -> {
                if (action.uri != null) {
                    _uiSate.product {
                        copy(
                            stagedImageUri = action.uri,
                            overlay = ScannerOverlay.PROCESSING_DIALOG
                        )
                    }
                }
            }

            is ScannerAction.OnProcessStagedImage -> {

            }
        }
    }

    private fun observeBarcodeEvents() {
        _barcodeEvents
            .filter { barcode ->
                val isIdle = stateValue.overlay == ScannerOverlay.NONE
                val isNewItem = barcode != lastProcessedBarcode
                isIdle && isNewItem
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
            _uiSate.product {
                copy(
                    scanResult = UiResult.Loading,
                    overlay = ScannerOverlay.LOADING_DIALOG
                )
            }
            val result = foodRepository.scanFoodBarcode(barcodeString)
                .onSuspendSuccess { foodItem ->
                    Timber.e("Success: $foodItem")
                    foodRepository.addFoodItem(foodItem)
                    _uiSate.product { copy(overlay = ScannerOverlay.NONE) }
                    _scannerEvent.trySend(ScannerEvent.OnSuccess(foodId = foodItem.id))
                }
                .onFailure { throwable ->
                    val error = throwable.toUiError()
                    _uiSate.product { copy(labelEvent = LabelEvent.FAILURE) }
                    _scannerEvent.trySend(ScannerEvent.OnFailure(error = error))
                }
            delay(1.seconds)
            _uiSate.product {
                copy(
                    scanResult = result.toUiStateResult(),
                    overlay = ScannerOverlay.NONE
                )
            }
        }
    }


    private fun onBarcodeDetect(barcodeString: String) {
        _barcodeEvents.tryEmit(barcodeString)
    }

    private fun onFlashChange(isFlashOn: Boolean) {
        _uiSate.product { copy(isFlashOn = isFlashOn) }
    }

    private fun onAutoScannerChange(isAutoScanned: Boolean) {
        val labelEvent = if (isAutoScanned) LabelEvent.SCANNING else LabelEvent.AUTO_OFF
        _uiSate.product { copy(isAutoScanned = isAutoScanned, labelEvent = labelEvent) }
    }

    private fun onScannerModeChange(mode: ScannerMode) {
        _uiSate.product { copy(scannerMode = mode) }
    }
}