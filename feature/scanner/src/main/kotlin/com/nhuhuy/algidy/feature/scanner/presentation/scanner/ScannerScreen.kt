package com.nhuhuy.algidy.feature.scanner.presentation.scanner

import android.net.Uri
import androidx.camera.core.Camera
import androidx.camera.core.TorchState
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cameraswitch
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FlashOff
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.scanner.domain.model.FoodDate
import com.nhuhuy.algidy.feature.scanner.presentation.canvas.ScannerBoundaryCorner
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.CameraPreviewContent
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.LabelEventContainer
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.ScannerControlBar
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    uiState: ScannerUiState,
    onSwitchMode: (ScannerMode) -> Unit,
    onAddBarcodeManually: () -> Unit,
    onFlashPress: (Boolean) -> Unit,
    onAutoScanPress: (Boolean) -> Unit,
    onResultDetected: (String) -> Unit,
    onDateDetected: (FoodDate) -> Unit,
    onImageStaged: (Uri?) -> Unit,
    onClosePress: () -> Unit
) {
    var camera by remember { mutableStateOf<Camera?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(camera) {
        camera?.cameraInfo?.torchState?.observe(lifecycleOwner) { state ->
            onFlashPress(state == TorchState.ON)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text(
                        text = when (uiState.scannerMode) {
                            ScannerMode.BARCODE_SCANNER -> stringResource(R.string.scanner_title_barcode)
                            ScannerMode.FOOD_SCANNER -> stringResource(R.string.scanner_title_food)
                        },
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 48.dp)
                            .basicMarquee(),
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClosePress) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.scanner_action_close),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    FilledTonalIconButton(
                        shape = RoundedCornerShape(
                            topStart = 24.dp,
                            bottomStart = 24.dp,
                            topEnd = 4.dp,
                            bottomEnd = 4.dp
                        ),
                        onClick = {
                            val newMode = when (uiState.scannerMode) {
                                ScannerMode.BARCODE_SCANNER -> ScannerMode.FOOD_SCANNER
                                ScannerMode.FOOD_SCANNER -> ScannerMode.BARCODE_SCANNER
                            }
                            onSwitchMode(newMode)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Cameraswitch,
                            contentDescription = "Switch mode"
                        )
                    }

                    FilledTonalIconButton(
                        shape = RoundedCornerShape(
                            topStart = 4.dp,
                            bottomStart = 4.dp,
                            topEnd = 24.dp,
                            bottomEnd = 24.dp
                        ),
                        onClick = { camera?.cameraControl?.enableTorch(!uiState.isFlashOn) }
                    ) {
                        Icon(
                            imageVector = if (uiState.isFlashOn) Icons.Rounded.FlashOff else Icons.Rounded.FlashOn,
                            contentDescription = stringResource(R.string.scanner_action_flash),
                            tint = if (uiState.isFlashOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        bottomBar = {
            ScannerControlBar(
                modifier = Modifier.safeDrawingPadding(),
                isAutoScanned = uiState.isAutoScanned,
                onAddManualBarcode = onAddBarcodeManually,
                onAutoScanChange = onAutoScanPress,
                onImageStaged = onImageStaged
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
                    mode = uiState.scannerMode,
                    modifier = Modifier.fillMaxSize(),
                    onCameraReady = { cameraInstance ->
                        camera = cameraInstance
                    },
                    onResultDetected = { result ->
                        if (uiState.isAutoScanned) {
                            onResultDetected(result)
                        }
                    },
                    onDateDetected = { foodDate ->
                        if (uiState.isAutoScanned) {
                            onDateDetected(foodDate)
                        }
                    }
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
