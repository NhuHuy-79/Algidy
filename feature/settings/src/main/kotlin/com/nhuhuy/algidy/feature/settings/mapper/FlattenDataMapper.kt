package com.nhuhuy.algidy.feature.settings.mapper

import com.nhuhuy.algidy.core.database.entity.CategoryEntity
import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.core.database.entity.FoodItemWithCategory
import com.nhuhuy.algidy.feature.settings.data.FlattenCategoryData
import com.nhuhuy.algidy.feature.settings.data.FlattenFoodData

fun FoodItemWithCategory.toFlattenFood(): FlattenFoodData {
    return FlattenFoodData(
        id = foodItem.id,
        name = foodItem.name,
        normalizedName = foodItem.normalizedName,
        categoryId = category?.id,
        location = foodItem.location,
        purchaseDate = foodItem.purchaseDate,
        expiryDate = foodItem.expiryDate,
        imageUri = foodItem.imageUri,
        notes = foodItem.notes,
        status = foodItem.status,
        resolvedDate = foodItem.resolvedDate
    )
}


fun FlattenFoodData.toFoodItemEntity(): FoodItemEntity {
    return FoodItemEntity(
        id = id,
        name = name,
        normalizedName = normalizedName,
        categoryId = categoryId,
        location = location,
        purchaseDate = purchaseDate,
        expiryDate = expiryDate,
        imageUri = imageUri,
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
