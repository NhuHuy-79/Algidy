package com.nhuhuy.algidy.feature.scanner.presentation.scanner

import androidx.camera.core.Camera
import androidx.camera.core.TorchState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nhuhuy.algidy.feature.scanner.presentation.canvas.ScannerBoundaryCorner
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.CameraPreviewContent
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.LabelEventContainer
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.ScannerBottomBar
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.ScannerTopBar
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerAction
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    uiState: ScannerUiState,
    onAction: (ScannerAction) -> Unit,
    onClosePress: () -> Unit
) {
    var camera by remember { mutableStateOf<Camera?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(camera) {
        camera?.cameraInfo?.torchState?.observe(lifecycleOwner) { state ->
            onAction(ScannerAction.OnFlashChange(state == TorchState.ON))
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            ScannerTopBar(
                modifier = Modifier.fillMaxWidth(),
                isFlashOn = uiState.isFlashOn,
                onCloseClick = onClosePress,
                onFlashSwitch = {
                    camera?.let { camera ->
                        if (camera.cameraInfo.hasFlashUnit()) {
                            camera.cameraControl.enableTorch(!uiState.isFlashOn)
                        }
                    }
                }
            )
        },
        bottomBar = {
            ScannerBottomBar(
                modifier = Modifier.safeDrawingPadding(),
                isAutoScanned = uiState.isAutoScanned,
                onAddManualBarcode = { onAction(ScannerAction.OnBarcodeAddManual) },
                onAutoScanChange = { auto ->
                    onAction(ScannerAction.OnAutoScanChange(auto))
                },
                onImageStaged = { uri ->
                    onAction(ScannerAction.OnImageStaged(uri))
                },
                onLaunch = {
                    onAction(ScannerAction.OnAutoScanChange(isAutoScanned = false))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                val screenWidth = LocalWindowInfo.current.containerDpSize.width
                CameraPreviewContent(
                    isAutoScanned = uiState.isAutoScanned,
                    modifier = Modifier.fillMaxSize(),
                    onCameraReady = { cameraInstance -> camera = cameraInstance },
                    onBarcodeDetected = { result ->
                        if (uiState.isAutoScanned) {
                            onAction(ScannerAction.OnBarcodeDetected(result))
                        }
                    },
                )

                if (uiState.isAutoScanned) {
                    ScannerBoundaryCorner(
                        modifier = Modifier.size(screenWidth * 0.8f),
                        cornerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        scanLineColor = MaterialTheme.colorScheme.secondary,
                        cornerSpacing = screenWidth * 0.5f,
                        cornerCap = 8.dp,
                        cornerRadius = 24.dp,
                        scanHeight = screenWidth * 0.3f
                    )
                }

                LabelEventContainer(
                    event = uiState.labelEvent,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                )
            }
        }
    }
}
