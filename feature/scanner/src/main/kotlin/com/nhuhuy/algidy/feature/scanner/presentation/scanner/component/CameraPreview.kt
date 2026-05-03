package com.nhuhuy.algidy.feature.scanner.presentation.scanner.component

import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DocumentScanner
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nhuhuy.algidy.core.designsystem.component.AppLabel
import com.nhuhuy.algidy.feature.scanner.domain.FoodDateScanner
import com.nhuhuy.algidy.feature.scanner.domain.model.FoodDate
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.ScannerMode
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.analyzer.BarcodeAnalyzer
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.analyzer.FoodAnalyzer
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.analyzer.FoodDateAnalyzer
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.LabelEvent
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun CameraPreviewContent(
    modifier: Modifier = Modifier,
    isAutoScanned: Boolean = true,
    mode: ScannerMode,
    imageCapture: ImageCapture,
    onCameraReady: (Camera) -> Unit,
    onResultDetected: (String) -> Unit,
    onDateDetected: (FoodDate) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor = remember(context) { ContextCompat.getMainExecutor(context) }
    val previewView = remember { PreviewView(context) }
    val foodDateScanner = koinInject<FoodDateScanner>()

    val barcodeAnalyzer = remember {
        BarcodeAnalyzer { barcode -> onResultDetected(barcode) }
    }

    remember {
        FoodAnalyzer { label, confidence ->
            if (confidence > 0.75f) onResultDetected(label)
        }
    }

    val foodDateAnalyzer = remember(foodDateScanner) {
        FoodDateAnalyzer(foodDateScanner) { date -> onDateDetected(date) }
    }

    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
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
                    foodDateAnalyzer.analyze(imageProxy)
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
        modifier = modifier.fillMaxSize()
    )
}


@Composable
fun LabelEventContainer(
    event: LabelEvent,
    modifier: Modifier = Modifier,
    duration: Duration = 1.5.seconds,
) {
    var showLabel by remember { mutableStateOf(false) }
    val text = remember(event) {
        when (event) {
            LabelEvent.NONE -> ""
            LabelEvent.AUTO_OFF -> "Auto-scanning is OFF"
            LabelEvent.SCANNING -> "Scanning..."
            LabelEvent.FAILURE -> "Failed to scan"
        }
    }
    val icon = remember(event) {
        when (event) {
            LabelEvent.NONE -> null
            LabelEvent.AUTO_OFF -> Icons.Rounded.PhotoCamera
            LabelEvent.SCANNING -> Icons.Rounded.DocumentScanner
            LabelEvent.FAILURE -> Icons.Rounded.ErrorOutline
        }
    }

    LaunchedEffect(event) {
        if (event != LabelEvent.NONE) {
            showLabel = true
            delay(duration)
            showLabel = false
        }
    }

    if (showLabel) {
        icon?.let {
            AppLabel(
                text = text,
                icon = icon,
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = modifier
            )
        }
    }

}