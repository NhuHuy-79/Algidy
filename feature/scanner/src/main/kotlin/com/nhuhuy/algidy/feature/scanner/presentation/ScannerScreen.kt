package com.nhuhuy.algidy.feature.scanner.presentation

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.camera.core.Camera
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.TorchState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FlashOff
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nhuhuy.algidy.feature.scanner.presentation.component.AutoScanButton
import com.nhuhuy.algidy.feature.scanner.presentation.component.CameraLabel
import com.nhuhuy.algidy.feature.scanner.presentation.component.CameraPreviewContent
import com.nhuhuy.algidy.feature.scanner.presentation.component.CaptureButton
import com.nhuhuy.algidy.feature.scanner.presentation.component.SelectImageButton
import com.nhuhuy.algidy.feature.scanner.viewmodel.ScannerUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    uiState: ScannerUiState,
    onFlashPress: (Boolean) -> Unit,
    onAutoScanPress: (Boolean) -> Unit,
    onResultDetected: (String) -> Unit,
    onScannerModePress: (ScannerMode) -> Unit,
    onClosePress: () -> Unit
) {
    var camera by remember { mutableStateOf<Camera?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember { ImageCapture.Builder().build() }

    LaunchedEffect(camera) {
        camera?.cameraInfo?.torchState?.observe(lifecycleOwner) { state ->
            onFlashPress(state == TorchState.ON)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = when (uiState.scannerMode) {
                            ScannerMode.BARCODE_SCANNER -> "Barcode Scanner"
                            ScannerMode.FOOD_SCANNER -> "Food Scanner"
                        },
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onClosePress,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { camera?.cameraControl?.enableTorch(!uiState.isFlashOn) }
                    ) {
                        Icon(
                            imageVector = if (uiState.isFlashOn) Icons.Rounded.FlashOff else Icons.Rounded.FlashOn,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(0.8f),
                contentAlignment = Alignment.Center
            ) {
                CameraPreviewContent(
                    isAutoScanned = uiState.isAutoScanned,
                    mode = uiState.scannerMode,
                    modifier = Modifier.fillMaxSize(),
                    imageCapture = imageCapture,
                    onCameraReady = { cameraInstance ->
                        camera = cameraInstance
                    },
                    onResultDetected = onResultDetected
                )

                CameraLabel(
                    key = uiState.isAutoScanned,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.BottomCenter)
                        .padding(vertical = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SelectImageButton(
                    modifier = Modifier.size(56.dp),
                    onClick = {
                        //openImage
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )

                CaptureButton(
                    modifier = Modifier.size(96.dp),
                    onCapturePress = {
                        //capture Image
                    },
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )

                AutoScanButton(
                    autoScanning = uiState.isAutoScanned,
                    onClick = { autoScanned: Boolean ->
                        onAutoScanPress(autoScanned)
                    },
                    enableContainerColor = MaterialTheme.colorScheme.primary,
                    disableContainerColor = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onImageCaptured: (Uri) -> Unit
) {
    val name = "Algidy_${System.currentTimeMillis()}.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
    }

    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                outputFileResults.savedUri?.let { onImageCaptured(it) }
            }

            override fun onError(exception: ImageCaptureException) {
            }
        }
    )
}