package com.nhuhuy.algidy.feature.scanner.presentation.component.analyzer

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(
    private val onBarcodeDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()
    private var lastScannedCode: String? = null
    private var lastScanTimestamp: Long = 0L

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                val barcode = barcodes.firstOrNull()?.rawValue ?: return@addOnSuccessListener
                val currentTime = System.currentTimeMillis()
                if (barcode != lastScannedCode || currentTime - lastScanTimestamp > 3000) {
                    lastScannedCode = barcode
                    lastScanTimestamp = currentTime
                    onBarcodeDetected(barcode)
                }
            }
            .addOnCompleteListener { imageProxy.close() }
    }
}