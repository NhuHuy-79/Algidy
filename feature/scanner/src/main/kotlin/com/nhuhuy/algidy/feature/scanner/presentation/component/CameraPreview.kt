package com.nhuhuy.algidy.feature.scanner.presentation.component

import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DocumentScanner
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.common.MlKit
import com.nhuhuy.algidy.core.designsystem.component.AppLabel
import com.nhuhuy.algidy.feature.scanner.presentation.component.analyzer.BarcodeAnalyzer
import com.nhuhuy.algidy.feature.scanner.presentation.component.analyzer.FoodAnalyzer
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

const val LABEL_LENGTH : Long = 1000L

@Composable
fun CameraPreviewContent(
    modifier: Modifier = Modifier,
    isAutoScanned: Boolean = true,
    mode: ScannerMode,
    imageCapture: ImageCapture,
    onCameraReady: (Camera) -> Unit,
    onResultDetected: (String) -> Unit,
) {
    var showLabel by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor = remember(context) { ContextCompat.getMainExecutor(context) }
    val previewView = remember { PreviewView(context) }

    val barcodeAnalyzer = remember {
        BarcodeAnalyzer { barcode -> onResultDetected(barcode) }
    }

    val foodAnalyzer = remember {
        FoodAnalyzer { label, confidence ->
            if (confidence > 0.75f) onResultDetected(label)
        }
    }

    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }

    LaunchedEffect(isAutoScanned) {
        showLabel = true
        delay(LABEL_LENGTH)
        showLabel = false
    }

    LaunchedEffect(mode, isAutoScanned) {
        if (!isAutoScanned) {
            imageAnalysis.clearAnalyzer()
            return@LaunchedEffect
        }
        imageAnalysis.setAnalyzer(mainExecutor) { imageProxy ->
            when (mode) {
                ScannerMode.BARCODE_SCANNER -> {
                    barcodeAnalyzer.analyze(imageProxy)
                }
                ScannerMode.FOOD_SCANNER -> {
                    foodAnalyzer.analyze(imageProxy)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().apply {
                surfaceProvider = previewView.surfaceProvider
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                val cameraInstance = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalysis
                )
                onCameraReady(cameraInstance)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, mainExecutor)
    }

    DisposableEffect(Unit) {
        onDispose {
            barcodeAnalyzer.release()
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun CameraLabel(
    key: Boolean,
    modifier: Modifier = Modifier,
    duration: Duration = 1.5.seconds,

){
    var showLabel by remember { mutableStateOf(false) }
    val text = remember(key) {
        if (key) "Scanning..." else "Auto-scanning is OFF"
    }
    val icon = remember(key) {
        if (key) Icons.Rounded.DocumentScanner else Icons.Rounded.PhotoCamera
    }

    LaunchedEffect(key) {
        showLabel = true
        delay(duration)
        showLabel = false
    }

    if (showLabel){
        AppLabel(
            text = text,
            icon = icon,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface,
            modifier = modifier
        )
    }

}