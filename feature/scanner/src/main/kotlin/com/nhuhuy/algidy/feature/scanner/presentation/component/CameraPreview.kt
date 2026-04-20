package com.nhuhuy.algidy.feature.scanner.presentation.component

import android.content.Context
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nhuhuy.algidy.feature.scanner.presentation.component.analyzer.BarcodeAnalyzer
import com.nhuhuy.algidy.feature.scanner.presentation.component.analyzer.FoodAnalyzer

@Composable
fun CameraPreviewContent(
    context: Context, // Giữ nguyên theo yêu cầu của bạn
    lifecycleOwner: LifecycleOwner, // Giữ nguyên theo yêu cầu của bạn
    mode: ScannerMode,
    imageCapture: ImageCapture,
    onCameraReady: (Camera) -> Unit,
    onResultDetected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mainExecutor = remember(context) { ContextCompat.getMainExecutor(context) }
    val previewView = remember { PreviewView(context) }

    // 1. Chỉ khởi tạo ImageAnalysis duy nhất một lần
    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            // Bạn có thể set ResolutionSelector ở đây nếu cần độ phân giải cụ thể
            .build()
    }

    // 2. Cập nhật logic xử lý (Analyzer) khi mode thay đổi
    // Việc này không làm bind lại Camera, chỉ thay đổi logic xử lý frame hình
    LaunchedEffect(mode) {
        imageAnalysis.setAnalyzer(mainExecutor) { imageProxy ->
            when (mode) {
                ScannerMode.BARCODE_SCANNER -> {
                    // Logic BarcodeAnalyzer của bạn
                    val analyzer = BarcodeAnalyzer { barcode ->
                        onResultDetected(barcode)
                    }
                    analyzer.analyze(imageProxy)
                }
                ScannerMode.FOOD_SCANNER -> {
                    // Logic FoodAnalyzer hoặc Gemini-pre-scan
                    val analyzer = FoodAnalyzer { label, confidence ->
                        if (confidence > 0.75f) onResultDetected(label)
                    }
                    analyzer.analyze(imageProxy)
                }
            }
        }
    }

    // 3. Khởi tạo và Bind Camera vào Lifecycle
    // Chạy 1 lần duy nhất khi Composable được mount
    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().apply {
                surfaceProvider = previewView.surfaceProvider
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Hủy các ràng buộc cũ trước khi bind mới
                cameraProvider.unbindAll()

                // Bind tất cả use cases: Preview, Capture (cho Gemini), và Analysis (cho Barcode)
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

    // Hiển thị Camera lên UI
    AndroidView(
        factory = { previewView },
        modifier = modifier.fillMaxSize()
    )
}