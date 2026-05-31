package com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.analyzer

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.feature.scanner.domain.FoodDateScanner
import com.nhuhuy.algidy.feature.scanner.domain.model.FoodDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * CameraX Analyzer that uses [FoodDateScanner] to detect dates.
 * Optimised to handle backpressure and prevent coroutine overlapping.
 */
class FoodDateAnalyzer(
    private val foodDateScanner: FoodDateScanner,
    private val onDateDetected: (FoodDate) -> Unit
) : ImageAnalysis.Analyzer {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val isProcessing = AtomicBoolean(false)
    private var isReleased = false

    override fun analyze(imageProxy: ImageProxy) {
        // If still processing previous frame or released, skip this one to save resources
        if (isProcessing.get() || isReleased) {
            imageProxy.close()
            return
        }

        isProcessing.set(true)
        scope.launch {
            try {
                if (!isReleased) {
                    foodDateScanner.scanImage(imageProxy)
                        .onSuccess { foodDate ->
                            if (foodDate != null && !isReleased) {
                                onDateDetected(foodDate)
                            }
                        }
                } else {
                    imageProxy.close()
                }
            } catch (e: Exception) {
                imageProxy.close()
            } finally {
                // Ensure imageProxy is closed and flag is reset even if scan fails
                isProcessing.set(false)
            }
        }
    }

    fun release() {
        isReleased = true
        scope.cancel()
    }
}
