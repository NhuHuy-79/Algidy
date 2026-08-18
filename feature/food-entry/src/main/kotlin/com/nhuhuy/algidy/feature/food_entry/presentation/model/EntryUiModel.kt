package com.nhuhuy.algidy.feature.food_entry.presentation.model

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.model.CategoryUiModel
import com.nhuhuy.algidy.core.presentation.model.toFoodCategory
import com.nhuhuy.algidy.core.presentation.model.toUiModel

@Immutable
data class EntryUiModel(
    val id: String = "",
    val name: String = "",
    val categoryUiModel: CategoryUiModel = CategoryUiModel.Uncategorized,
    val location: StorageLocation = StorageLocation.FRIDGE,
    val purchaseDate: Long = System.currentTimeMillis(),
    val expiryDate: Long = -1,
    val imageUri: String? = null,
    val notes: String = "",
)

fun FoodItem.toEntryUiModel(): EntryUiModel {
    return EntryUiModel(
        id = id,
        name = name,
        categoryUiModel = category.toUiModel(),
        location = location,
        purchaseDate = purchaseDate,
        expiryDate = expiryDate,
        imageUri = imageUri,
        notes = note
    )
}

fun EntryUiModel.toFoodItem(): FoodItem {
    return FoodItem(
        id = id,
        name = name,
        location = location,
        category = categoryUiModel.toFoodCategory(),
        categoryId = categoryUiModel.toFoodCategory()?.id,
        purchaseDate = purchaseDate,
        expiryDate = expiryDate,
        imageUri = imageUri,
        note = notes
    )
}
