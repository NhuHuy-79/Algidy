@file:OptIn(FlowPreview::class)

package com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.util.onFailure
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.UiResult
import com.nhuhuy.algidy.core.presentation.toUiError
import com.nhuhuy.algidy.feature.scanner.domain.model.FoodDate
import com.nhuhuy.algidy.feature.scanner.domain.usecase.CreateFoodItemFromDateUseCase
import com.nhuhuy.algidy.feature.scanner.domain.usecase.ScanBarcodeUseCase
import com.nhuhuy.algidy.feature.scanner.domain.usecase.ScanFoodDateUseCase
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
    private val scanBarcodeUseCase: ScanBarcodeUseCase,
    private val scanFoodDateUseCase: ScanFoodDateUseCase,
    private val createFoodItemFromDateUseCase: CreateFoodItemFromDateUseCase,
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

    private val _foodDateEvents = MutableSharedFlow<FoodDate>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var lastProcessedBarcode: String? = null

    init {
        observeBarcodeEvents()
        observeFoodDateEvents()
    }

    fun onAction(action: ScannerAction) {
        when (action) {
            is ScannerAction.OnScannerModeChange -> onScannerModeChange(action.mode)
            is ScannerAction.OnAutoScanChange -> onAutoScanChange(action.isAutoScanned)
            is ScannerAction.OnFlashChange -> onFlashChange(action.isFlashOn)
            is ScannerAction.OnResultDetected -> {
                val mode: ScannerMode = stateValue.scannerMode
                when (mode) {
                    ScannerMode.BARCODE_SCANNER -> onBarcodeDetect(action.barcodeString)
                    ScannerMode.FOOD_SCANNER -> {

                    }
                }
            }

            is ScannerAction.OnDateDetected -> {
                if (stateValue.scannerMode == ScannerMode.FOOD_SCANNER) {
                    onDateDetected(action.foodDate)
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
                    _uiState.product {
                        copy(
                            stagedImageUri = action.uri,
                            overlay = ScannerOverlay.PROCESSING_DIALOG
                        )
                    }
                }
            }

            is ScannerAction.OnProcessStagedImage -> {
                when (stateValue.scannerMode) {
                    ScannerMode.BARCODE_SCANNER -> analyzeBarcodeFromUri(action.uri)
                    ScannerMode.FOOD_SCANNER -> analyzeFoodDateFromUri(action.uri)
                }
            }
        }
    }

    private fun analyzeFoodDateFromUri(uri: Uri) {
        viewModelScope.launch {
            _uiState.product {
                copy(
                    labelEvent = LabelEvent.SCANNING,
                    scanResult = UiResult.Loading
                )
            }

            scanFoodDateUseCase.fromUri(uri)
                .onSuccess { foodDate ->
                    _uiState.product {
                        copy(
                            labelEvent = LabelEvent.NONE,
                            overlay = ScannerOverlay.NONE,
                            scanResult = UiResult.Idle
                        )
                    }
                    if (foodDate != null) {
                        onFoodDateScan(foodDate)
                    }

                }
                .onFailure {
                    _uiState.product {
                        copy(
                            labelEvent = LabelEvent.FAILURE,
                            scanResult = UiResult.Idle
                        )
                    }
                }
        }
    }

    private fun analyzeBarcodeFromUri(uri: Uri) {
        viewModelScope.launch {
            _uiState.product {
                copy(
                    labelEvent = LabelEvent.SCANNING,
                    scanResult = UiResult.Loading
                )
            }

            scanBarcodeUseCase.fromUri(uri)
                .onSuccess { foodItem ->
                    _uiState.product {
                        copy(
                            labelEvent = LabelEvent.NONE,
                            overlay = ScannerOverlay.NONE,
                            scanResult = UiResult.Idle
                        )
                    }
                    _scannerEvent.trySend(ScannerEvent.OnSuccess(foodId = foodItem.id))
                }
                .onFailure { throwable ->
                    val error = throwable.toUiError()
                    _uiState.product {
                        copy(
                            labelEvent = LabelEvent.FAILURE,
                            scanResult = UiResult.Idle
                        )
                    }
                    _scannerEvent.trySend(ScannerEvent.OnFailure(error))
                }
        }
    }

    private fun observeBarcodeEvents() {
        _barcodeEvents
            .debounce(500.milliseconds)
            .filter { barcode ->
                val isAutoScanned = stateValue.isAutoScanned
                val isIdle = stateValue.overlay == ScannerOverlay.NONE
                val isNewItem = barcode != lastProcessedBarcode
                isAutoScanned && isIdle && isNewItem
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

            scanBarcodeUseCase.fromBarcode(barcodeString)
                .onSuccess { foodItem ->
                    Timber.d("Success: $foodItem")
                    foodRepository.addFoodItem(foodItem)
                    _uiState.product { copy(overlay = ScannerOverlay.NONE) }
                    _scannerEvent.trySend(ScannerEvent.OnSuccess(foodId = foodItem.id))
                }
                .onFailure { throwable ->
                    val error = throwable.toUiError()
                    _uiState.product {
                        copy(
                            labelEvent = LabelEvent.FAILURE,
                            overlay = ScannerOverlay.NONE
                        )
                    }
                    _scannerEvent.trySend(ScannerEvent.OnFailure(error = error))
                    delay(2000)
                    _uiState.product {
                        copy(labelEvent = if (stateValue.isAutoScanned) LabelEvent.SCANNING else LabelEvent.AUTO_OFF)
                    }
                    lastProcessedBarcode = null
                }

            _uiState.product {
                copy(
                    scanResult = UiResult.Idle, // Reset to idle after success/failure since it's handled by events
                    overlay = ScannerOverlay.NONE
                )
            }
        }
    }

    private fun onBarcodeDetect(barcodeString: String) {
        _barcodeEvents.tryEmit(barcodeString)
    }

    private fun observeFoodDateEvents() {
        _foodDateEvents
            .debounce(800.milliseconds)
            .filter {
                stateValue.isAutoScanned && stateValue.overlay == ScannerOverlay.NONE
            }
            .onEach { foodDate ->
                Timber.d("Food Date detected and stabilized: $foodDate")
                onFoodDateScan(foodDate)
            }
            .launchIn(viewModelScope)
    }

    private suspend fun onFoodDateScan(foodDate: FoodDate) {
        _uiState.product {
            copy(overlay = ScannerOverlay.LOADING_DIALOG)
        }
        createFoodItemFromDateUseCase(foodDate = foodDate)
            .onSuccess { foodItem ->
                _uiState.product {
                    copy(overlay = ScannerOverlay.NONE)
                }
                _scannerEvent.trySend(element = ScannerEvent.OnSuccess(foodId = foodItem.id))
            }
            .onFailure {
                _uiState.product {
                    copy(
                        overlay = ScannerOverlay.NONE,
                        labelEvent = LabelEvent.FAILURE
                    )
                }
                delay(2000)
                _uiState.product {
                    copy(labelEvent = if (stateValue.isAutoScanned) LabelEvent.SCANNING else LabelEvent.AUTO_OFF)
                }
            }
    }

    private fun onDateDetected(foodDate: FoodDate) {
        _foodDateEvents.tryEmit(foodDate)
    }

    private fun onFlashChange(isFlashOn: Boolean) {
        _uiState.product { copy(isFlashOn = isFlashOn) }
    }

    private fun onAutoScanChange(isAutoScanned: Boolean) {
        val labelEvent = if (isAutoScanned) LabelEvent.SCANNING else LabelEvent.AUTO_OFF
        _uiState.product { copy(isAutoScanned = isAutoScanned, labelEvent = labelEvent) }
    }

    private fun onScannerModeChange(mode: ScannerMode) {
        _uiState.product { copy(scannerMode = mode) }
    }
}
