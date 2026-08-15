package com.nhuhuy.algidy.feature.inventory.presentation.model

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.StorageLocation

@Immutable
data class FoodDetailUiModel(
    val id: String,
    val name: String,
    val categoryName: String?,
    val storageLocation: StorageLocation,
    val imageUri: String?,
    val remainDays: Int,
    val location: StorageLocation,
)

fun FoodItem.toFoodDetailUiModel(): FoodDetailUiModel {
    return FoodDetailUiModel(
        id = id,
        name = name,
        categoryName = category?.name,
        storageLocation = location,
        imageUri = imageUri,
        remainDays = getRemainingDays(),
        location = location
    )
}