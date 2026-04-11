package com.nhuhuy.algidy.core.data.mapper

import com.nhuhuy.algidy.core.database.entity.FoodEntity
import com.nhuhuy.algidy.core.model.FoodItem

fun FoodEntity.asExternalModel() = FoodItem(
    id = id,
    name = name,
    categoryId = categoryId,
    location = location,
    quantity = quantity,
    itemUnit = itemUnit,
    purchaseDate = purchaseDate,
    expiryDate = expiryDate,
    imageUri = imageUri,
    isFavorite = isFavorite,
    notes = notes
)

fun FoodItem.asEntity() = FoodEntity(
    id = id,
    name = name,
    categoryId = categoryId,
    location = location,
    quantity = quantity,
    itemUnit = itemUnit,
    purchaseDate = purchaseDate,
    expiryDate = expiryDate,
    imageUri = imageUri,
    isFavorite = isFavorite,
    notes = notes
)