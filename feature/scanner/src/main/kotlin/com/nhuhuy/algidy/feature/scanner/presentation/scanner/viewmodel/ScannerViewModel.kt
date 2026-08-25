@file:OptIn(FlowPreview::class)

package com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.nhuhuy.algidy.core.data.util.onFailure
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.core.data.util.product
import com.nhuhuy.algidy.core.presentation.UiError
import com.nhuhuy.algidy.core.presentation.UiResult
import com.nhuhuy.algidy.core.presentation.toUiError
import com.nhuhuy.algidy.core.presentation.viewmodel.BaseViewModel
import com.nhuhuy.algidy.feature.scanner.domain.usecase.ScanBarcodeUseCase
import com.nhuhuy.algidy.feature.scanner.utils.ScannerValidateResult
import com.nhuhuy.algidy.feature.scanner.utils.ScannerValidator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

class ScannerViewModel(
    private val scanBarcodeUseCase: ScanBarcodeUseCase
) : BaseViewModel<ScannerUiState, ScannerEvent, ScannerAction>() {
    private val _uiState = MutableStateFlow(ScannerUiState())
    override val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    private val stateValue: ScannerUiState
        get() = _uiState.value

    private var timeoutJob: Job? = null
    private var lastProcessedBarcode: String? = null

    override fun onAction(action: ScannerAction) {
        when (action) {
            is ScannerAction.OnAutoScanChange ->
                onAutoScanChange(action.isAutoScanned)

            is ScannerAction.OnFlashChange -> {
                _uiState.product { copy(isFlashOn = action.isFlashOn) }
            }

            is ScannerAction.OnBarcodeDetected ->
                onBarcodeDetected(action.barcode)

            ScannerAction.OnDismissRequest ->
                _uiState.product {
                    copy(overlay = ScannerOverlay.NONE)
                }

            is ScannerAction.OnImageStaged ->
                action.uri?.let(::processUriImage)

            ScannerAction.OnBarcodeAddManual ->
                showBarcodeDialog()

            is AddBarcodeDialogAction ->
                onBarcodeDialogAction(action)

            is WarningDialogAction ->
                handleWarningDialogAction(action)
        }
    }

    private fun startTimeoutWatchdog() {
        timeoutJob?.cancel()

        timeoutJob = viewModelScope.launch {
            delay(5.seconds)

            _uiState.product {
                copy(labelEvent = LabelEvent.ADD_MANUALLY)
            }
        }
    }

    private fun onBarcodeDetected(barcode: String) {
        if (!stateValue.isAutoScanned) return
        if (stateValue.overlay != ScannerOverlay.NONE) return
        if (barcode == lastProcessedBarcode) return

        lastProcessedBarcode = barcode

        Timber.d("Barcode detected: $barcode")

        when (ScannerValidator.validateBarcode(barcode)) {
            ScannerValidateResult.VALID -> {
                startTimeoutWatchdog()
                onBarcodeScan(barcode)
            }

            ScannerValidateResult.INVALID -> {
                showScanFailure()
            }
        }
    }

    private fun showScanFailure() {
        viewModelScope.launch {
            _uiState.product {
                copy(labelEvent = LabelEvent.FAILURE)
            }

            delay(2.seconds)

            lastProcessedBarcode = null

            _uiState.product {
                copy(
                    labelEvent = if (isAutoScanned) LabelEvent.SCANNING else LabelEvent.AUTO_OFF
                )
            }
        }
    }

    private fun onBarcodeScan(barcode: String) {
        viewModelScope.launch {
            _uiState.product {
                copy(
                    scanResult = UiResult.Loading,
                    overlay = ScannerOverlay.LoadingDialog
                )
            }

            scanBarcodeUseCase
                .fromBarcode(barcode)
                .onSuccess { foodItem ->
                    Timber.d("Successfully scanned product: $foodItem")

                    _uiState.product {
                        copy(
                            overlay = ScannerOverlay.SuccessBottomSheet(foodItem),
                            scanResult = UiResult.Idle
                        )
                    }

                    emitEvent(event = ScannerEvent.OnSuccess(foodItem = foodItem))
                }
                .onFailure { throwable ->
                    val error = throwable.toUiError()
                    Timber.e(throwable, "Failed to scan barcode: $barcode")
                    _uiState.product {
                        copy(
                            labelEvent = LabelEvent.FAILURE,
                            overlay = ScannerOverlay.NONE,
                            scanResult = UiResult.Idle
                        )
                    }

                    emitEvent(event = ScannerEvent.OnFailure(error = error))

                    delay(3.seconds)

                    lastProcessedBarcode = null

                    _uiState.product {
                        copy(labelEvent = if (isAutoScanned) LabelEvent.SCANNING else LabelEvent.AUTO_OFF)
                    }
                }
        }
    }

    private fun processUriImage(uri: Uri) {
        viewModelScope.launch {
            _uiState.product {
                copy(
                    overlay = ScannerOverlay.LoadingDialog,
                    isAutoScanned = false,
                    labelEvent = LabelEvent.SCANNING,
                    scanResult = UiResult.Loading
                )
            }

            scanBarcodeUseCase
                .fromUri(uri)
                .onSuccess { foodItem ->
                    _uiState.product {
                        copy(
                            labelEvent = LabelEvent.NONE,
                            overlay = ScannerOverlay.SuccessBottomSheet(foodItem),
                            scanResult = UiResult.Idle
                        )
                    }

                    emitEvent(event = ScannerEvent.OnSuccess(foodItem = foodItem))
                }
                .onFailure { throwable ->
                    handleUriScanFailure(error = throwable.toUiError())
                }
        }
    }

    private fun handleUriScanFailure(error: UiError) {
        _uiState.product {
            copy(
                labelEvent = LabelEvent.FAILURE,
                overlay = ScannerOverlay.NONE,
                scanResult = UiResult.Idle
            )
        }
        emitEvent(event = ScannerEvent.OnFailure(error))
    }

    private fun onBarcodeDialogAction(
        action: AddBarcodeDialogAction
    ) {
        when (action) {
            AddBarcodeDialogAction.OnConfirm -> {
                val barcode = stateValue.barCodeInput

                if (barcode.isBlank()) return

                viewModelScope.launch {
                    _uiState.product {
                        copy(
                            overlay = ScannerOverlay.LoadingDialog
                        )
                    }

                    scanBarcodeUseCase
                        .fromBarcode(barcode)
                        .onSuccess { foodItem ->
                            _uiState.product {
                                copy(
                                    overlay = ScannerOverlay.SuccessBottomSheet(foodItem),
                                    barCodeInput = ""
                                )
                            }

                            emitEvent(event = ScannerEvent.OnSuccess(foodItem = foodItem))
                        }
                        .onFailure { throwable ->
                            val error = throwable.toUiError()

                            _uiState.product {
                                copy(
                                    overlay = ScannerOverlay.NONE,
                                    labelEvent = LabelEvent.FAILURE
                                )
                            }

                            emitEvent(event = ScannerEvent.OnFailure(error))
                        }
                }
            }

            is AddBarcodeDialogAction.OnValueChange -> {
                _uiState.product {
                    copy(barCodeInput = action.value)
                }
            }
        }
    }

    private fun showBarcodeDialog() {
        _uiState.product {
            copy(
                labelEvent = LabelEvent.NONE,
                overlay = ScannerOverlay.BarcodeScanningDialog,
                isAutoScanned = false
            )
        }
    }

    private fun handleWarningDialogAction(
        action: WarningDialogAction
    ) {
        when (action) {
            WarningDialogAction.Confirm -> {
                _uiState.product {
                    copy(overlay = ScannerOverlay.BarcodeScanningDialog)
                }
            }
        }
    }

    private fun onAutoScanChange(isAutoScanned: Boolean) {
        _uiState.product {
            copy(
                isAutoScanned = isAutoScanned,
                labelEvent = if (isAutoScanned) {
                    LabelEvent.SCANNING
                } else {
                    LabelEvent.AUTO_OFF
                }
            )
        }
    }

    override fun onCleared() {
        timeoutJob?.cancel()
        super.onCleared()
    }
}