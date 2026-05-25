@file:OptIn(FlowPreview::class)

package com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.data.util.onFailure
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.UiResult
import com.nhuhuy.algidy.core.presentation.toUiError
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.scanner.domain.model.FoodDate
import com.nhuhuy.algidy.feature.scanner.domain.usecase.CreateFoodItemFromDateUseCase
import com.nhuhuy.algidy.feature.scanner.domain.usecase.ScanBarcodeUseCase
import com.nhuhuy.algidy.feature.scanner.domain.usecase.ScanFoodDateUseCase
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.ScannerMode
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds


class ScannerViewModel(
    private val scanBarcodeUseCase: ScanBarcodeUseCase,
    private val scanFoodDateUseCase: ScanFoodDateUseCase,
    private val createFoodItemFromDateUseCase: CreateFoodItemFromDateUseCase,
    private val foodRepository: FoodRepository
) : BaseViewModel<ScannerUiState, ScannerEvent, ScannerAction>() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    override val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()
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

    override fun onAction(action: ScannerAction) {
        when (action) {
            is ScannerAction.OnScannerModeChange -> onScannerModeChange(action.mode)
            is ScannerAction.OnAutoScanChange -> onAutoScanChange(action.isAutoScanned)
            is ScannerAction.OnFlashChange -> onFlashChange(action.isFlashOn)
            is ScannerAction.OnResultDetected -> {
                if (stateValue.scannerMode == ScannerMode.BARCODE_SCANNER) {
                    onBarcodeDetect(action.barcodeString)
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
                action.uri?.let { uri ->
                    processUriImage(uri)
                }
            }

            is ScannerAction.OnProcessStagedImage -> {
                processUriImage(action.uri)
            }

            is ScannerAction.OnFoodItemFound -> {
                viewModelScope.launch {
                    val item = foodRepository.getFoodById(action.foodId)
                    item?.let {
                        _uiState.product { copy(foodItemResult = it) }
                    }
                }
            }

            ScannerAction.OnBarcodeAddManual -> {
                _uiState.product {
                    copy(
                        overlay = ScannerOverlay.BARCODE_DIALOG,
                        isAutoScanned = false
                    )
                }
            }

            is AddBarcodeDialogAction -> onBarcodeDialogAction(action)
        }
    }

    private fun processUriImage(uri: Uri) {
        viewModelScope.launch {
            _uiState.product {
                copy(
                    overlay = ScannerOverlay.LOADING_DIALOG,
                    isAutoScanned = false,
                    labelEvent = LabelEvent.SCANNING
                )
            }

            when (stateValue.scannerMode) {
                ScannerMode.BARCODE_SCANNER -> analyzeBarcodeFromUri(uri)
                ScannerMode.FOOD_SCANNER -> analyzeFoodDateFromUri(uri)
            }
        }
    }

    private fun onBarcodeDialogAction(action: AddBarcodeDialogAction) {
        viewModelScope.launch {
            when (action) {
                AddBarcodeDialogAction.OnConfirm -> {
                    val input = currentState.barCodeInput
                    _uiState.product { copy(overlay = ScannerOverlay.LOADING_DIALOG) }
                    
                    scanBarcodeUseCase.fromBarcode(input)
                        .onSuccess { foodItem ->
                            _uiState.product { copy(overlay = ScannerOverlay.NONE) }
                            emitEvent(ScannerEvent.OnSuccess(foodItem = foodItem))
                        }
                        .onFailure {
                            _uiState.product {
                                copy(overlay = ScannerOverlay.NONE, labelEvent = LabelEvent.FAILURE)
                            }
                        }
                }

                is AddBarcodeDialogAction.OnValueChange -> {
                    _uiState.product { copy(barCodeInput = action.value) }
                }
            }
        }
    }

    private suspend fun analyzeFoodDateFromUri(uri: Uri) {
        _uiState.product { copy(scanResult = UiResult.Loading) }

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
                } else {
                    handleUriScanFailure()
                }
            }
            .onFailure {
                handleUriScanFailure()
            }
    }

    private suspend fun analyzeBarcodeFromUri(uri: Uri) {
        _uiState.product { copy(scanResult = UiResult.Loading) }

        scanBarcodeUseCase.fromUri(uri)
            .onSuccess { foodItem ->
                _uiState.product {
                    copy(
                        labelEvent = LabelEvent.NONE,
                        overlay = ScannerOverlay.NONE,
                        scanResult = UiResult.Idle
                    )
                }
                emitEvent(ScannerEvent.OnSuccess(foodItem = foodItem))
            }
            .onFailure { throwable ->
                handleUriScanFailure(throwable.toUiError())
            }
    }

    private fun handleUriScanFailure(error: com.nhuhuy.algidy.core.presentation.UiError? = null) {
        _uiState.product {
            copy(
                labelEvent = LabelEvent.FAILURE,
                overlay = ScannerOverlay.NONE,
                scanResult = UiResult.Idle
            )
        }
        if (error != null) {
            emitEvent(ScannerEvent.OnFailure(error))
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
                    emitEvent(ScannerEvent.OnSuccess(foodItem = foodItem))
                }
                .onFailure { throwable ->
                    val error = throwable.toUiError()
                    _uiState.product {
                        copy(
                            labelEvent = LabelEvent.FAILURE,
                            overlay = ScannerOverlay.NONE
                        )
                    }
                    emitEvent(ScannerEvent.OnFailure(error = error))
                    delay(2000)
                    _uiState.product {
                        copy(labelEvent = if (stateValue.isAutoScanned) LabelEvent.SCANNING else LabelEvent.AUTO_OFF)
                    }
                    lastProcessedBarcode = null
                }

            _uiState.product {
                copy(
                    scanResult = UiResult.Idle,
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
                emitEvent(event = ScannerEvent.OnSuccess(foodItem = foodItem))
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
