package com.nhuhuy.algidy.feature.scanner.domain.usecase

import android.net.Uri
import androidx.camera.core.ImageProxy
import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.feature.scanner.domain.FoodDateScanner
import com.nhuhuy.algidy.feature.scanner.domain.model.FoodDate

class ScanFoodDateUseCase(
    private val scanner: FoodDateScanner
) {
    suspend fun fromImage(imageProxy: ImageProxy): Resource<FoodDate?> {
        return scanner.scanImage(imageProxy)
    }

    suspend fun fromUri(uri: Uri): Resource<FoodDate?> {
        return scanner.scanFromUri(uri)
    }
}
