package com.nhuhuy.algidy.feature.scanner.domain

import android.net.Uri
import androidx.camera.core.ImageProxy
import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.feature.scanner.domain.model.FoodDate

/**
 * Interface for scanning production and expiry dates from food products.
 */
interface FoodDateScanner {
    /**
     * Scans an [ImageProxy] for food dates.
     * Returns a [Resource] containing [FoodDate] if successful.
     */
    suspend fun scanImage(imageProxy: ImageProxy): Resource<FoodDate?>

    /**
     * Scans an image from a [Uri] for food dates.
     * Returns a [Resource] containing [FoodDate] if successful.
     */
    suspend fun scanFromUri(uri: Uri): Resource<FoodDate?>
}
