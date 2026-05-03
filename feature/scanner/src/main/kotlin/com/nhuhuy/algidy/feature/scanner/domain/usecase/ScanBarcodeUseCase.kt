package com.nhuhuy.algidy.feature.scanner.domain.usecase

import android.net.Uri
import com.nhuhuy.algidy.core.data.repository.FoodRepository
import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.feature.scanner.domain.BarcodeScanner

class ScanBarcodeUseCase(
    private val repository: FoodRepository,
    private val barcodeScanner: BarcodeScanner
) {
    suspend fun fromBarcode(barcode: String): Resource<FoodItem> {
        return repository.scanFoodBarcode(barcode)
    }

    suspend fun fromUri(uri: Uri): Resource<FoodItem> {
        val barcode = barcodeScanner.scanFromImage(uri)
        return if (barcode != null) {
            repository.scanFoodBarcode(barcode)
        } else {
            Resource.Failure(Exception("No barcode found in image"))
        }
    }
}
