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
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.analyzer.BarcodeAnalyzer
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.LabelEvent
import kotlinx.coroutines.delay
import java.util.concurrent.Executors
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun CameraPreviewContent(
    modifier: Modifier = Modifier,
    isAutoScanned: Boolean,
    onCameraReady: (Camera) -> Unit,
    onBarcodeDetected: (String) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

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

    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setResolutionSelector(resolutionSelector)
            .build()
    }

    val barcodeAnalyzer = remember { BarcodeAnalyzer(onBarcodeDetected) }

    var surfaceRequest by remember { mutableStateOf<SurfaceRequest?>(null) }

    LaunchedEffect(isAutoScanned) {
        if (isAutoScanned) {
            imageAnalysis.setAnalyzer(analysisExecutor, barcodeAnalyzer)
        } else {
            imageAnalysis.clearAnalyzer()
        }
    }

    LaunchedEffect(lifecycleOwner) {
        val cameraProvider = ProcessCameraProvider.awaitInstance(context)

        val preview = Preview.Builder()
            .setResolutionSelector(resolutionSelector)
            .build()
            .apply {
                setSurfaceProvider { request ->
                    surfaceRequest = request
                }
            }

        val camera = cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            imageAnalysis
        )

        onCameraReady(camera)
    }

    DisposableEffect(Unit) {
        onDispose {
            imageAnalysis.clearAnalyzer()
            barcodeAnalyzer.release()
            analysisExecutor.shutdown()
        }
    }

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
    val algidyIcons = AlgidyIcons.Scanner
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
            LabelEvent.AUTO_OFF -> algidyIcons.AutoOff
            LabelEvent.SCANNING -> algidyIcons.Scanning
            LabelEvent.FAILURE -> algidyIcons.Failure
            LabelEvent.ADD_MANUALLY -> algidyIcons.AddManually
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
                icon = icon.toImageVector(),
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = modifier
            )
        }
    }
}
