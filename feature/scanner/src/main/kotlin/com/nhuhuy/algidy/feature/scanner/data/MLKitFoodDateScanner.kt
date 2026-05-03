package com.nhuhuy.algidy.feature.scanner.data

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.nl.entityextraction.Entity
import com.google.mlkit.nl.entityextraction.EntityExtraction
import com.google.mlkit.nl.entityextraction.EntityExtractionParams
import com.google.mlkit.nl.entityextraction.EntityExtractorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.data.util.safeCall
import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.feature.scanner.domain.FoodDateScanner
import com.nhuhuy.algidy.feature.scanner.domain.model.FoodDate
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Implementation of [FoodDateScanner] using Google ML Kit Text Recognition and Entity Extraction.
 */
class MLKitFoodDateScanner(
    private val context: Context,
    private val dispatchers: AppDispatchers
) : FoodDateScanner {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val entityExtractor = EntityExtraction.getClient(
        EntityExtractorOptions.Builder(EntityExtractorOptions.ENGLISH).build()
    )

    private val productionKeywords =
        listOf("nsx", "mfg", "mfd", "ngay sx", "pro", "pdt", "manufactured")

    @OptIn(ExperimentalGetImage::class)
    override suspend fun scanImage(imageProxy: ImageProxy): Resource<FoodDate?> {
        return safeCall(
            dispatcher = dispatchers.io,
            onFinally = { imageProxy.close() }
        ) {
            val mediaImage = imageProxy.image ?: throw Exception("Image is null")
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val visionText = recognizer.process(image).await()
            extractDates(visionText)
        }
    }

    override suspend fun scanFromUri(uri: Uri): Resource<FoodDate?> {
        return safeCall(dispatcher = dispatchers.io) {
            val image = InputImage.fromFilePath(context, uri)
            val visionText = recognizer.process(image).await()
            extractDates(visionText)
        }
    }

    private suspend fun extractDates(visionText: Text): FoodDate {
        val rawText = visionText.text
        if (rawText.isBlank()) return FoodDate()

        // Ensure model is downloaded
        entityExtractor.downloadModelIfNeeded().await()

        val params = EntityExtractionParams.Builder(rawText).build()
        val annotations = entityExtractor.annotate(params).await()

        val detectedDates = mutableListOf<Date>()

        for (annotation in annotations) {
            for (entity in annotation.entities) {
                if (entity.type == Entity.TYPE_DATE_TIME) {
                    entity.asDateTimeEntity()?.let { dateTimeEntity ->
                        detectedDates.add(Date(dateTimeEntity.timestampMillis))
                    }
                }
            }
        }

        var productionDate: String? = null
        var expiryDate: String? = null

        // If we have dates, try to classify them
        if (detectedDates.isNotEmpty()) {
            val sortedDates = detectedDates.distinct().sortedBy { it.time }

            // Basic heuristic: oldest is production, newest is expiry
            if (sortedDates.size >= 2) {
                productionDate = formatDate(sortedDates.first())
                expiryDate = formatDate(sortedDates.last())
            } else {
                // If only one date, try to see if it's explicitly marked as production
                val isExplicitProd = productionKeywords.any { rawText.lowercase().contains(it) }
                if (isExplicitProd) {
                    productionDate = formatDate(sortedDates.first())
                } else {
                    expiryDate = formatDate(sortedDates.first())
                }
            }
        }

        return FoodDate(productionDate, expiryDate)
    }

    private fun formatDate(date: Date): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
    }
}
