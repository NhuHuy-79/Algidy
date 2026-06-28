package com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.analyzer

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import timber.log.Timber

class BarcodeAnalyzer(
    private val onBarcodeDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()
    private var isReleased = false

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (isReleased) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (!isReleased) {
                    barcodes.firstOrNull()?.rawValue?.let { onBarcodeDetected(it) }
                }
            }
            .addOnFailureListener {
                if (!isReleased) Timber.e(it)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    fun release() {
        isReleased = true
        scanner.close()
    }
}
