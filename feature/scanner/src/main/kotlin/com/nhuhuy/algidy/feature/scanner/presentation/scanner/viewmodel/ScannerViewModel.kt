@file:OptIn(FlowPreview::class)

package com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.util.onFailure
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.core.data.util.onSuspendSuccess
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.UiResult
import com.nhuhuy.algidy.core.presentation.toUiError
import com.nhuhuy.algidy.core.presentation.toUiStateResult
import com.nhuhuy.algidy.feature.scanner.domain.BarcodeScanner
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.ScannerMode
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds


class ScannerViewModel(
    private val barcodeScanner: BarcodeScanner,
    private val foodRepository: FoodRepository
) : ViewModel() {
    private val _scannerEvent = Channel<ScannerEvent>(Channel.BUFFERED)
    val scannerEvent = _scannerEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState = _uiState.asStateFlow()
    private val stateValue: ScannerUiState get() = _uiState.value

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
                _uiState.product { copy(overlay = ScannerOverlay.NONE) }
            }

            is ScannerAction.OnFoodItemSaved -> {
                _uiState.product { copy(foodItemResult = action.foodItem) }
            }

            is ScannerAction.OnImageStaged -> {
                if (action.uri != null) {
                    analyzeBarcodeFromUri(action.uri)
                }
            }

            is ScannerAction.OnProcessStagedImage -> analyzeBarcodeFromUri(action.uri)
        }
    }

    private fun analyzeBarcodeFromUri(uri: Uri) {
        viewModelScope.launch {
            _uiState.product { copy(labelEvent = LabelEvent.SCANNING) }

            val barcodeString = barcodeScanner.scanFromImage(uri)
            if (barcodeString != null) {
                foodRepository.scanFoodBarcode(barcodeString)
                    .onSuccess { foodItem ->
                        _uiState.product { copy(labelEvent = LabelEvent.NONE) }
                        _scannerEvent.trySend(ScannerEvent.OnSuccess(foodId = foodItem.id))
                    }
                    .onFailure { throwable ->
                        val error = throwable.toUiError()
                        _uiState.product { copy(labelEvent = LabelEvent.FAILURE) }
                        _scannerEvent.trySend(ScannerEvent.OnFailure(error))
                    }
            } else {
                _uiState.product {
                    copy(
                        overlay = ScannerOverlay.NONE,
                        labelEvent = LabelEvent.FAILURE
                    )
                }
            }
        }
    }

    private fun observeBarcodeEvents() {
        _barcodeEvents
            .debounce(500.milliseconds)
            .filter { barcode ->
                val isIdle = stateValue.overlay == ScannerOverlay.NONE
                val isNewItem = barcode != lastProcessedBarcode
                isIdle && isNewItem
            }
            .onEach { barcode ->
                Timber.d("Barcode detected: $barcode")
                onBarcodeScan(barcode)
            }
            .launchIn(viewModelScope)
    }

    private fun onBarcodeScan(barcodeString: String) {
        viewModelScope.launch {
            lastProcessedBarcode = barcodeString
            _uiState.product {
                copy(
                    scanResult = UiResult.Loading,
                    overlay = ScannerOverlay.LOADING_DIALOG
                )
            }

            val result = foodRepository.scanFoodBarcode(barcodeString)
                .onSuspendSuccess { foodItem ->
                    Timber.d("Success: $foodItem")
                    foodRepository.addFoodItem(foodItem)
                    _uiState.product { copy(overlay = ScannerOverlay.NONE) }
                    _scannerEvent.trySend(ScannerEvent.OnSuccess(foodId = foodItem.id))
                }
                .onFailure { throwable ->
                    val error = throwable.toUiError()
                    _uiState.product { copy(labelEvent = LabelEvent.FAILURE) }
                    _scannerEvent.trySend(ScannerEvent.OnFailure(error = error))
                    delay(2000)
                    lastProcessedBarcode = null
                }

            _uiState.product {
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
        _uiState.product { copy(isFlashOn = isFlashOn) }
    }

    private fun onAutoScannerChange(isAutoScanned: Boolean) {
        val labelEvent = if (isAutoScanned) LabelEvent.SCANNING else LabelEvent.AUTO_OFF
        _uiState.product { copy(isAutoScanned = isAutoScanned, labelEvent = labelEvent) }
    }

    private fun onScannerModeChange(mode: ScannerMode) {
        _uiState.product { copy(scannerMode = mode) }
    }
}