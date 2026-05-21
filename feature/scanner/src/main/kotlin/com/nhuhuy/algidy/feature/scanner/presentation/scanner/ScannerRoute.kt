package com.nhuhuy.algidy.feature.scanner.presentation.scanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
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
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerRoute(
    onNavigateBack: () -> Unit,
    onNavigateToConfirm: (foodId: String) -> Unit,
) {
    val viewModel: ScannerViewModel = koinViewModel()
    val uiState: ScannerUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            is ScannerEvent.OnSuccess -> {
                onNavigateToConfirm(event.foodId)
            }

            is ScannerEvent.OnFailure -> {

            }
        }
    }

    BoxLayout {
        if (cameraPermissionState.status.isGranted) {
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
        } else {
            // Có thể hiển thị màn hình yêu cầu quyền ở đây nếu muốn
        }

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
