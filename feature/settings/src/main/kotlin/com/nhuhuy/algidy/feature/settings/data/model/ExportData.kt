package com.nhuhuy.algidy.feature.settings.data.model

import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.core.model.food.StorageLocation
import kotlinx.serialization.Serializable

@Serializable
data class ExportData(
    val schemaVersion: Int,
    val foods: List<FoodExportData> = emptyList(),
    val categories: List<CategoryExportData> = emptyList()
)

@Serializable
data class CategoryExportData(
    val id: String,
    val name: String,
)

@Serializable
data class FoodExportData(
    val id: String,
    val name: String,
    val normalizedName: String,
    val categoryId: String?,
    val category: CategoryExportData?,
    val location: StorageLocation,
    val purchaseDate: Long,
    val expiryDate: Long,
    val imageUri: String?,
    val status: FoodStatus,
    val resolvedDate: Long? = null
)

