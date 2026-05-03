package com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.analyzer

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.nhuhuy.algidy.core.data.util.onSuccess
import com.nhuhuy.algidy.feature.scanner.domain.FoodDateScanner
import com.nhuhuy.algidy.feature.scanner.domain.model.FoodDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * CameraX Analyzer that uses [FoodDateScanner] to detect dates.
 */
class FoodDateAnalyzer(
    private val foodDateScanner: FoodDateScanner,
    private val onDateDetected: (FoodDate) -> Unit
) : ImageAnalysis.Analyzer {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            foodDateScanner.scanImage(imageProxy)
                .onSuccess { foodDate ->
                    if (foodDate != null) {
                        onDateDetected(foodDate)
                    }
                }
        }
    }
}
