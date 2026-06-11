package com.nhuhuy.algidy.feature.scanner.presentation.scanner.component

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
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
import androidx.compose.ui.platform.LocalResources
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nhuhuy.algidy.core.designsystem.component.AppLabel
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.scanner.domain.FoodDateScanner
import com.nhuhuy.algidy.feature.scanner.domain.model.FoodDate
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.ScannerMode
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.analyzer.BarcodeAnalyzer
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.analyzer.FoodDateAnalyzer
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.LabelEvent
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import java.util.concurrent.Executors
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun CameraPreviewContent(
    modifier: Modifier = Modifier,
    isAutoScanned: Boolean = true,
    mode: ScannerMode,
    onCameraReady: (Camera) -> Unit,
    onResultDetected: (String) -> Unit,
    onDateDetected: (FoodDate) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Dedicated background executor for image analysis to avoid UI jank
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    val resolutionSelector = remember {
        ResolutionSelector.Builder()
            .setAspectRatioStrategy(
                AspectRatioStrategy(
                    AspectRatio.RATIO_4_3,
                    AspectRatioStrategy.FALLBACK_RULE_AUTO
                )
            )
            .build()
    }
    
    // Modern SurfaceRequest handling for Compose
    var surfaceRequest by remember { mutableStateOf<SurfaceRequest?>(null) }
    
    val foodDateScanner = koinInject<FoodDateScanner>()

    val barcodeAnalyzer = remember {
        BarcodeAnalyzer { barcode -> onResultDetected(barcode) }
    }

    val foodDateAnalyzer = remember(foodDateScanner) {
        FoodDateAnalyzer(foodDateScanner) { date -> onDateDetected(date) }
    }

    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setResolutionSelector(resolutionSelector)
            .build()
    }

    // Update analyzer based on mode and auto-scan setting
    LaunchedEffect(mode, isAutoScanned) {
        if (!isAutoScanned) {
            imageAnalysis.clearAnalyzer()
            return@LaunchedEffect
        }
        imageAnalysis.setAnalyzer(analysisExecutor) { imageProxy ->
            when (mode) {
                ScannerMode.BARCODE_SCANNER -> barcodeAnalyzer.analyze(imageProxy)
                ScannerMode.FOOD_SCANNER -> foodDateAnalyzer.analyze(imageProxy)
            }
        }
    }

    // Camera Provider and Binding
    LaunchedEffect(lifecycleOwner) {
        val cameraProvider = ProcessCameraProvider.awaitInstance(context)

        val preview = Preview.Builder()
            .setResolutionSelector(resolutionSelector)
            .build().apply {
            setSurfaceProvider { request ->
                surfaceRequest = request
            }
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            val cameraInstance = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
            
            onCameraReady(cameraInstance)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            barcodeAnalyzer.release()
            foodDateAnalyzer.release()
            imageAnalysis.clearAnalyzer()
            analysisExecutor.shutdown()
        }
    }

    // Render using the modern CameraXViewfinder
    surfaceRequest?.let { request ->
        CameraXViewfinder(
            surfaceRequest = request,
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
fun LabelEventContainer(
    event: LabelEvent,
    modifier: Modifier = Modifier,
    duration: Duration = 1.5.seconds,
) {
    var showLabel by remember { mutableStateOf(false) }
    val resource = LocalResources.current
    val text = remember(event) {
        when (event) {
            LabelEvent.NONE -> ""
            LabelEvent.AUTO_OFF -> resource.getString(R.string.scanner_label_auto_off)
            LabelEvent.SCANNING -> resource.getString(R.string.scanner_label_scanning)
            LabelEvent.FAILURE -> resource.getString(R.string.scanner_label_failure)
            LabelEvent.ADD_MANUALLY -> resource.getString(R.string.scanner_add_manually_tooltip)
        }
    }
    val icon = remember(event) {
        when (event) {
            LabelEvent.NONE -> null
            LabelEvent.AUTO_OFF -> Icons.Rounded.PhotoCamera
            LabelEvent.SCANNING -> Icons.Rounded.DocumentScanner
            LabelEvent.FAILURE -> Icons.Rounded.ErrorOutline
            LabelEvent.ADD_MANUALLY -> Icons.Outlined.Add
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
