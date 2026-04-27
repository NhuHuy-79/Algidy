// feature/scanner/presentation/ScannerScreen.kt
package com.nhuhuy.algidy.feature.scanner.presentation

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.TorchState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nhuhuy.algidy.feature.scanner.presentation.component.CameraLabel
import com.nhuhuy.algidy.feature.scanner.presentation.component.CameraPreviewContent
import com.nhuhuy.algidy.feature.scanner.presentation.component.ScannerControlBar
import com.nhuhuy.algidy.feature.scanner.viewmodel.ScannerUiState
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    uiState: ScannerUiState,
    onFlashPress: (Boolean) -> Unit,
    onAutoScanPress: (Boolean) -> Unit,
    onResultDetected: (String) -> Unit,
    onImageStaged: (Uri?) -> Unit, // Action mới: Khi có ảnh chờ
    onProcessImageClick: (Uri) -> Unit, // Action mới: Khi bấm vào thumbnail để scan
    onScannerModePress: (ScannerMode) -> Unit,
    onClosePress: () -> Unit
) {
    val context = LocalContext.current
    var camera by remember { mutableStateOf<Camera?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current
    // ImageCapture dùng chung cho logic chụp ảnh
    val imageCapture = remember { ImageCapture.Builder().build() }

    // Launcher để chọn ảnh từ Gallery (Photo Picker - M3 Style)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                Timber.d("Photo picked: $uri")
                onImageStaged(uri) // Đưa vào trạng thái chờ
            }
        }
    )

    LaunchedEffect(camera) {
        camera?.cameraInfo?.torchState?.observe(lifecycleOwner) { state ->
            onFlashPress(state == TorchState.ON)
        }
    }
    val shouldShowLabel = remember(uiState.isAutoScanned, uiState.stagedImageUri) {
        !uiState.isAutoScanned && uiState.stagedImageUri == null
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text(
                        text = when (uiState.scannerMode) {
                            ScannerMode.BARCODE_SCANNER -> "Barcode Scanner"
                            ScannerMode.FOOD_SCANNER -> "Food Scanner"
                        },
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 48.dp), // Bù trừ cho nút back để title căn giữa hơn
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClosePress) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close Scanner",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { camera?.cameraControl?.enableTorch(!uiState.isFlashOn) }
                    ) {
                        Icon(
                            imageVector = if (uiState.isFlashOn) Icons.Rounded.FlashOff else Icons.Rounded.FlashOn,
                            contentDescription = "Toggle Flash",
                            tint = if (uiState.isFlashOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            ScannerControlBar(
                isAutoScanned = uiState.isAutoScanned,
                stagedImageUri = uiState.stagedImageUri,
                onSelectImageClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onCaptureClick = {
                    takePhoto(
                        context = context,
                        imageCapture = imageCapture,
                        onImageCaptured = { uri ->
                            Timber.d("Photo captured: $uri")
                            onImageStaged(uri)
                        }
                    )
                },
                onAutoScanChange = onAutoScanPress,
                onProcessImageClick = onProcessImageClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // innerPadding lúc này chỉ chứa TopBar padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(1f),
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
                    onResultDetected = { result ->
                        if (uiState.isAutoScanned) {
                            onResultDetected(result)
                        }
                    }
                )

                if (shouldShowLabel) {
                    CameraLabel(
                        key = uiState.scannerMode == ScannerMode.BARCODE_SCANNER,
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp)
                    )
                }

                // UX bổ sung: Nếu đang chờ scan hình ảnh tĩnh, có thể thêm một overlay mờ hoặc khung ngắm đặc biệt ở đây
            }
        }
    }
}

// Giữ nguyên hàm takePhoto như của bạn, chỉ thêm Timber log cho chuyên nghiệp
private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onImageCaptured: (Uri) -> Unit
) {
    val name = "Algidy_${System.currentTimeMillis()}.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        // Từ Android Q trở lên, dùng RELATIVE_PATH
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/Algidy-Scans")
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
                outputFileResults.savedUri?.let { uri ->
                    Timber.d("Image saved successfully to: $uri")
                    onImageCaptured(uri)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Timber.e(exception, "Photo capture failed")
            }
        }
    )
}
