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
import com.nhuhuy.algidy.feature.scanner.utils.ScannerValidateResult
import com.nhuhuy.algidy.feature.scanner.utils.ScannerValidator
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

const val SCANNER_ERROR_LIMIT = 3

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

    private var warningScannerErrorTime: Int = 0

    private var lastProcessedBarcode: String? = null
    private var lastProcessedFoodDate: FoodDate? = null

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
            is WarningDialogAction -> handleWarningDialogAction(action)
        }
    }

    private fun handleWarningDialogAction(action: WarningDialogAction) {
        when (action) {
            WarningDialogAction.Confirm -> {
                _uiState.product {
                    copy(overlay = ScannerOverlay.BARCODE_DIALOG)
                }
            }

            WarningDialogAction.Open -> {
                _uiState.product {
                    copy(overlay = ScannerOverlay.WARNING_DIALOG)
                }
            }
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
                            _uiState.product {
                                copy(
                                    overlay = ScannerOverlay.NONE,
                                    errorScannerCount = 0
                                )
                            }
                            emitEvent(ScannerEvent.OnSuccess(foodItem = foodItem))
                        }
                        .onFailure {
                            _uiState.product {
                                copy(
                                    overlay = ScannerOverlay.NONE,
                                    labelEvent = LabelEvent.FAILURE,
                                )
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

    /**
     * Observes barcode events and triggers scanning.
     * Fixed the 'debounce' issue: replaced it with a manual filter to ensure
     * the first detection is responsive while preventing rapid re-triggers.
     */
    private fun observeBarcodeEvents() {
        _barcodeEvents
            .filter { barcode ->
                // Only process if auto-scan is on, and we are not currently showing a dialog/overlay
                val isAutoScanned = stateValue.isAutoScanned
                val isIdle = stateValue.overlay == ScannerOverlay.NONE

                // Prevent re-scanning the same barcode immediately
                val isNewItem = barcode != lastProcessedBarcode

                isAutoScanned && isIdle && isNewItem
            }
            .onEach { barcode ->
                // Synchronously set the last processed barcode to prevent race conditions
                // where multiple 'onBarcodeScan' jobs could start for the same item.
                lastProcessedBarcode = barcode
                when (ScannerValidator.validateBarcode(barcode)) {
                    ScannerValidateResult.VALID -> {
                        onBarcodeScan(barcode)
                    }

                    ScannerValidateResult.INVALID -> {
                        if (currentState.errorScannerCount >= SCANNER_ERROR_LIMIT) {
                            _uiState.product {
                                copy(overlay = ScannerOverlay.WARNING_DIALOG, errorScannerCount = 0)
                            }
                        } else {
                            _uiState.product {
                                copy(errorScannerCount = errorScannerCount + 1)
                            }
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Handles the actual scanning logic for a detected barcode.
     */
    private fun onBarcodeScan(barcodeString: String) {
        viewModelScope.launch {
            // Transition to loading state
            _uiState.product {
                copy(
                    scanResult = UiResult.Loading,
                    overlay = ScannerOverlay.LOADING_DIALOG
                )
            }

            scanBarcodeUseCase.fromBarcode(barcodeString)
                .onSuccess { foodItem ->
                    Timber.d("Successfully scanned product: $foodItem")
                    foodRepository.addFoodItem(foodItem)

                    // On success, hide overlay and inform UI
                    _uiState.product { copy(overlay = ScannerOverlay.NONE) }
                    emitEvent(ScannerEvent.OnSuccess(foodItem = foodItem))

                    // We DO NOT reset lastProcessedBarcode here to avoid immediate re-scanning
                    // of the same physical item if the camera is still pointed at it.
                    // It will be reset when a DIFFERENT barcode is seen or manual action occurs.
                }
                .onFailure { throwable ->
                    val error = throwable.toUiError()
                    Timber.e("Failed to scan barcode: $barcodeString, Error: $error")

                    _uiState.product {
                        copy(
                            labelEvent = LabelEvent.FAILURE,
                            overlay = ScannerOverlay.NONE,
                            scanResult = UiResult.Idle
                        )
                    }
                    emitEvent(ScannerEvent.OnFailure(error = error))

                    // Allow re-trying the same barcode after a delay if it failed
                    delay(3000)
                    _uiState.product {
                        copy(labelEvent = if (stateValue.isAutoScanned) LabelEvent.SCANNING else LabelEvent.AUTO_OFF)
                    }
                    // Reset to allow re-scanning the same item after the failure message is gone
                    lastProcessedBarcode = null
                }

            // Ensure we reset scanning result state if not already handled
            _uiState.product {
                copy(scanResult = UiResult.Idle)
            }
        }
    }

    private fun onBarcodeDetect(barcodeString: String) {
        _barcodeEvents.tryEmit(barcodeString)
    }

    /**
     * Observes food date detections from the camera.
     * Uses debounce to ensure the date is stabilized in the camera view
     * before triggering the heavy processing task.
     */
    private fun observeFoodDateEvents() {
        _foodDateEvents
            .debounce(800.milliseconds)
            .filter { foodDate ->
                // Only trigger if in idle state and auto-scanning
                val isAutoScanned = stateValue.isAutoScanned
                val isIdle = stateValue.overlay == ScannerOverlay.NONE

                // Ensure we have at least some date info to process
                val hasData = foodDate.expiryDate != null || foodDate.productionDate != null

                // Prevent re-processing the exact same date result immediately
                val isNewDate = foodDate != lastProcessedFoodDate

                isAutoScanned && isIdle && hasData && isNewDate
            }
            .onEach { foodDate ->
                // Synchronously mark as processed to prevent race conditions during heavy UseCase execution
                lastProcessedFoodDate = foodDate
                Timber.d("New stabilized Food Date detected: $foodDate")
                onFoodDateScan(foodDate)
            }
            .launchIn(viewModelScope)
    }

    /**
     * Processes the detected food date to create a new food item.
     */
    private suspend fun onFoodDateScan(foodDate: FoodDate) {
        _uiState.product {
            copy(overlay = ScannerOverlay.LOADING_DIALOG)
        }

        createFoodItemFromDateUseCase(foodDate = foodDate)
            .onSuccess { foodItem ->
                Timber.d("Successfully created item from date: $foodItem")
                _uiState.product {
                    copy(overlay = ScannerOverlay.NONE)
                }
                emitEvent(event = ScannerEvent.OnSuccess(foodItem = foodItem))

                // Note: lastProcessedFoodDate remains set to prevent immediate re-scan.
            }
            .onFailure {
                Timber.e("Failed to create item from detected date: $foodDate")
                _uiState.product {
                    copy(
                        overlay = ScannerOverlay.NONE,
                        labelEvent = LabelEvent.FAILURE
                    )
                }
                // Cooldown period after failure
                delay(3000)
                _uiState.product {
                    copy(labelEvent = if (stateValue.isAutoScanned) LabelEvent.SCANNING else LabelEvent.AUTO_OFF)
                }
                // Clear the cache on failure to allow a retry
                lastProcessedFoodDate = null
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
