package com.nhuhuy.algidy.feature.settings.data

import com.nhuhuy.algidy.core.model.food.DefaultFoodCategory
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation
import kotlinx.serialization.Serializable

@Serializable
data class FlattenExportData(
    val foodDataVersion: Int,
    val foodData: List<FlattenFoodData> = emptyList(),
    val category: List<FlattenCategoryData> = emptyList()
)

@Serializable
data class FlattenCategoryData(
    val id: String,
    val name: String,
    val description: String,
)

@Serializable
data class FlattenFoodData(
    val id: String,
    val name: String,
    val normalizedName: String,
    val categoryId: String,
    val location: StorageLocation,
    val quantity: Double,
    val itemUnit: ItemUnit,
    val purchaseDate: Long,
    val expiryDate: Long,
    val imageUri: String?,
    val isFavorite: Boolean,
    val notes: String,
    val category: DefaultFoodCategory,
    val status: FoodStatus,
    val resolvedData: Long? = null
)

