package com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.analyzer

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class FoodAnalyzer(
    private val onFoodDetected: (String, Float) -> Unit
) : ImageAnalysis.Analyzer {
    private val options = ImageLabelerOptions.Builder()
        .setConfidenceThreshold(0.7f)
        .build()

    private val labeler = ImageLabeling.getClient(options)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            labeler.process(image)
                .addOnSuccessListener { labels ->
                    // Lấy nhãn có độ tin tưởng cao nhất
                    labels.firstOrNull()?.let { label ->
                        onFoodDetected(label.text, label.confidence)
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
