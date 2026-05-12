package com.nhuhuy.algidy.feature.scanner.presentation.scanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.UiResult
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.dialog.ImageProcessingDialog
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.dialog.ScannerLoadingDialog
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerAction
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerEvent
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerOverlay
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerUiState
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun ScannerRoute(
    viewModel: ScannerViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToConfirm: (foodId: String) -> Unit,
) {
    val uiState: ScannerUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction
    val event: Flow<ScannerEvent> = viewModel.scannerEvent

    ObserveEffect(event) { event ->
        when (event) {
            is ScannerEvent.OnSuccess -> {
                onNavigateToConfirm(event.foodId)
            }

            is ScannerEvent.OnFailure -> {

            }
        }
    }

    BoxLayout {
        ScannerScreen(
            uiState = uiState,
            onClosePress = onNavigateBack,
            onFlashPress = { isFlashOn: Boolean ->
                onAction(ScannerAction.OnFlashChange(isFlashOn))
            },
            onAutoScanPress = { autoScanned: Boolean ->
                onAction(ScannerAction.OnAutoScanChange(autoScanned))
            },
            onResultDetected = { barcodeString: String ->
                onAction(ScannerAction.OnResultDetected(barcodeString))
            },
            onDateDetected = { foodDate ->
                onAction(ScannerAction.OnDateDetected(foodDate))
            },
            onSwitchMode = { mode ->
                onAction(ScannerAction.OnScannerModeChange(mode = mode))
            },
            onImageStaged = { uri ->
                onAction(ScannerAction.OnImageStaged(uri))
            }
        )

        when (uiState.overlay) {
            ScannerOverlay.NONE -> Unit
            ScannerOverlay.LOADING_DIALOG -> {
                ScannerLoadingDialog(
                    onDismissRequest = { onAction(ScannerAction.OnDismissRequest) }
                )
            }
            ScannerOverlay.PROCESSING_DIALOG -> {
                ImageProcessingDialog(
                    imageUri = uiState.stagedImageUri!!,
                    isProcessing = uiState.scanResult == UiResult.Loading,
                    onDismiss = {
                        onAction(ScannerAction.OnDismissRequest)
                    },
                    onScanClick = {
                        onAction(ScannerAction.OnProcessStagedImage(uiState.stagedImageUri!!))
                    }
                )
            }
        }
    }
}
