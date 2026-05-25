package com.nhuhuy.algidy.feature.scanner.presentation.scanner

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.TextFieldDialog
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.dialog.ScannerLoadingDialog
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.AddBarcodeDialogAction
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerAction
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerAction.OnAutoScanChange
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerAction.OnDateDetected
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerAction.OnDismissRequest
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerAction.OnFlashChange
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerAction.OnImageStaged
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerAction.OnResultDetected
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerAction.OnScannerModeChange
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerEvent
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerOverlay
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerUiState
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerRoute(
    onNavigateBack: () -> Unit,
    onNavigateToFoodEntry: (FoodItem) -> Unit,
) {
    val viewModel: ScannerViewModel = koinViewModel()
    val uiState: ScannerUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            is ScannerEvent.OnSuccess -> onNavigateToFoodEntry(event.foodItem)

            is ScannerEvent.OnFailure -> {

            }
        }
    }

    LaunchedEffect(uiState.foodItemResult) {
        if (uiState.foodItemResult.name.isNotEmpty()) {
            onNavigateToFoodEntry(uiState.foodItemResult)
        }
    }

    BoxLayout {
        ScannerScreen(
            uiState = uiState,
            onClosePress = onNavigateBack,
            onFlashPress = { isFlashOn: Boolean ->
                onAction(OnFlashChange(isFlashOn))
            },
            onAutoScanPress = { autoScanned: Boolean ->
                onAction(OnAutoScanChange(autoScanned))
            },
            onResultDetected = { barcodeString: String ->
                onAction(OnResultDetected(barcodeString))
            },
            onDateDetected = { foodDate ->
                onAction(OnDateDetected(foodDate))
            },
            onSwitchMode = { mode ->
                onAction(OnScannerModeChange(mode = mode))
            },
            onImageStaged = { uri ->
                onAction(OnImageStaged(uri))
            },
            onAddBarcodeManually = {
                onAction(ScannerAction.OnBarcodeAddManual)
            }
        )

        when (uiState.overlay) {
            ScannerOverlay.NONE -> Unit
            ScannerOverlay.LOADING_DIALOG -> {
                ScannerLoadingDialog(
                    onDismissRequest = { onAction(OnDismissRequest) }
                )
            }

            ScannerOverlay.BARCODE_DIALOG -> TextFieldDialog(
                value = uiState.barCodeInput,
                title = stringResource(R.string.scanner_barcode_dialog_title),
                label = stringResource(R.string.scanner_barcode_label),
                confirmText = stringResource(R.string.scanner_barcode_confirm),
                onValueChange = { value ->
                    onAction(AddBarcodeDialogAction.OnValueChange(value))
                },
                onDismiss = {
                    onAction(OnDismissRequest)
                },
                onConfirm = {
                    onAction(AddBarcodeDialogAction.OnConfirm)
                }
            )
        }
    }
}
