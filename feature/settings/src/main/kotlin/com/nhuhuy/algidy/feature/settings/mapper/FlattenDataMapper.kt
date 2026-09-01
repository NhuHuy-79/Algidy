package com.nhuhuy.algidy.feature.settings.mapper

import com.nhuhuy.algidy.core.database.entity.CategoryEntity
import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.core.database.entity.FoodItemWithCategory
import com.nhuhuy.algidy.feature.settings.data.model.CategoryExportData
import com.nhuhuy.algidy.feature.settings.data.model.FoodExportData

fun FoodItemWithCategory.toFlattenFood(): FoodExportData {
    return FoodExportData(
        id = foodItem.id,
        name = foodItem.name,
        normalizedName = foodItem.normalizedName,
        categoryId = category?.id,
        location = foodItem.location,
        purchaseDate = foodItem.purchaseDate,
        expiryDate = foodItem.expiryDate,
        imageUri = foodItem.imageUri,
        status = foodItem.status,
        resolvedDate = foodItem.resolvedDate,
        category = category?.toFlattenCategoryData()
    )
}


fun FoodExportData.toFoodItemEntity(): FoodItemEntity {
    return FoodItemEntity(
        id = id,
        name = name,
        normalizedName = normalizedName,
        categoryId = categoryId,
        location = location,
        purchaseDate = purchaseDate,
        expiryDate = expiryDate,
        imageUri = imageUri,
        notes = "",
        status = status,
        resolvedDate = resolvedDate,
    )
}

fun CategoryExportData.toCategoryEntity() = CategoryEntity(
    id = id,
    name = name,
)

fun CategoryEntity.toFlattenCategoryData() = CategoryExportData(
    id = id,
    name = name,
)
