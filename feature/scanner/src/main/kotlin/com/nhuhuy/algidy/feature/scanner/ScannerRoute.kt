package com.nhuhuy.algidy.feature.scanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.designsystem.presentation.ObserveEffect
import com.nhuhuy.algidy.feature.confirm.presentation.component.ScanResultBottomSheet
import com.nhuhuy.algidy.feature.scanner.presentation.ScannerMode
import com.nhuhuy.algidy.feature.scanner.presentation.ScannerScreen
import com.nhuhuy.algidy.feature.scanner.presentation.component.ScannerLoadingDialog
import com.nhuhuy.algidy.feature.scanner.viewmodel.ScannerAction
import com.nhuhuy.algidy.feature.scanner.viewmodel.ScannerEvent
import com.nhuhuy.algidy.feature.scanner.viewmodel.ScannerOverlay
import com.nhuhuy.algidy.feature.scanner.viewmodel.ScannerUiState
import com.nhuhuy.algidy.feature.scanner.viewmodel.ScannerViewModel
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
            onScannerModePress = { mode: ScannerMode ->
                onAction(ScannerAction.OnScannerModeChange(mode))
            },
            onResultDetected = { barcodeString: String ->
                onAction(ScannerAction.OnResultDetected(barcodeString))
            },
            onImageStaged = { uri ->
                onAction(ScannerAction.OnImageStaged(uri))
            },
            onProcessImageClick = { uri ->
                onAction(ScannerAction.OnProcessStagedImage(uri))
            }
        )



        when (uiState.overlay) {
            ScannerOverlay.NONE -> Unit
            ScannerOverlay.SUCCESS_SHEET -> {
                ScanResultBottomSheet(
                    foodItem = uiState.foodItemResult,
                    onSave = { foodItem ->
                        onAction(ScannerAction.OnFoodItemSaved(foodItem))
                    },
                    onDismiss = {
                        onAction(ScannerAction.OnDismissRequest)
                    },
                )
            }

            ScannerOverlay.LOADING_DIALOG -> {
                ScannerLoadingDialog(
                    onDismissRequest = { onAction(ScannerAction.OnDismissRequest) }
                )
            }

            ScannerOverlay.ERROR_DIALOG -> {

            }
        }
    }
}