package com.nhuhuy.algidy.feature.settings.mapper

import com.nhuhuy.algidy.core.database.entity.CategoryEntity
import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.feature.settings.data.FlattenCategoryData
import com.nhuhuy.algidy.feature.settings.data.FlattenFoodData

fun FoodItemEntity.toFlattenFood(): FlattenFoodData {
    return FlattenFoodData(
        id = id,
        name = name,
        normalizedName = normalizedName,
        categoryId = categoryId,
        location = location,
        quantity = quantity,
        itemUnit = itemUnit,
        purchaseDate = purchaseDate,
        expiryDate = expiryDate,
        imageUri = imageUri,
        isFavorite = isFavorite,
        notes = notes,
        status = status,
        resolvedDate = resolvedDate
    )
}


fun FlattenFoodData.toFoodItemEntity(): FoodItemEntity {
    return FoodItemEntity(
        id = id,
        name = name,
        normalizedName = normalizedName,
        categoryId = categoryId,
        location = location,
        quantity = quantity,
        itemUnit = itemUnit,
        purchaseDate = purchaseDate,
        expiryDate = expiryDate,
        imageUri = imageUri,
        isFavorite = isFavorite,
        notes = notes,
        status = status,
        resolvedDate = resolvedDate,
    )
}

fun FlattenCategoryData.toCategoryEntity() = CategoryEntity(
    id = id,
    name = name,
    description = description
)

fun CategoryEntity.toFlattenCategoryData() = FlattenCategoryData(
    id = id,
    name = name,
    description = description
)
