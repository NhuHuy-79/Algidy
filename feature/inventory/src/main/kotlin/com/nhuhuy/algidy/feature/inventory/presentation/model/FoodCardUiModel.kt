package com.nhuhuy.algidy.feature.inventory.presentation.model

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.model.toUiModel

@Immutable
data class FoodCardUiModel(
    val id: String = "",
    val imageUri: String? = null,
    val categoryId: String? = null,
    val name: String = "",
    val remainDays: Int = 0,
    val expiryDate: Long = -1L,
    val purchaseDate: Long = -1L,
    val freshness: Freshness = Freshness.FRESH,
    val location: StorageLocation = StorageLocation.OTHER,
    val note: String = "",
    val categoryUiModel: CategoryUiModel = CategoryUiModel.Uncategorized
)

fun FoodItem.toFoodCardUiModel(): FoodCardUiModel {
    return FoodCardUiModel(
        id = id,
        categoryId = categoryId,
        imageUri = imageUri,
        categoryUiModel = category.toUiModel(),
        name = name,
        remainDays = getRemainingDays(),
        expiryDate = expiryDate,
        purchaseDate = purchaseDate,
        freshness = getFreshnessStatus(),
        location = location,
        note = note
    )
}

fun List<FoodItem>.toFoodCardUiModel(): List<FoodCardUiModel> {
    return map { it.toFoodCardUiModel() }
}


