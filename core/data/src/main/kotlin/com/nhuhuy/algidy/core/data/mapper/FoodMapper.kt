package com.nhuhuy.algidy.core.data.mapper

import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.core.model.food.DefaultFoodCategory
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.ItemUnit
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.network.model.FoodApiResponse
import com.nhuhuy.algidy.toGenericNormalized
import java.util.UUID


fun FoodApiResponse.toEntity() = FoodItemEntity(
    id = UUID.randomUUID().toString(),
    name = product?.productName.orEmpty(),
    categoryId = product?.categories?.firstOrNull()?.capitalize().orEmpty(),
    location = StorageLocation.OTHER,
    quantity = 0.0,
    itemUnit = ItemUnit.OTHER,
    purchaseDate = System.currentTimeMillis(),
    expiryDate = -1,
    imageUri = null,
    isFavorite = false,
    notes = "",
    category = DefaultFoodCategory.OTHERS,
)

fun FoodApiResponse.toDomain() = FoodItem(
    id = UUID.randomUUID().toString(),
    name = product?.productName.orEmpty(),
    categoryId = product?.categories?.firstOrNull()?.capitalize().orEmpty(),
    location = StorageLocation.OTHER,
    quantity = 0.0,
    itemUnit = ItemUnit.OTHER,
    purchaseDate = System.currentTimeMillis(),
    expiryDate = -1,
    imageUri = product?.imageUrl,
    isFavorite = false,
    notes = "",
    defaultFoodCategory = DefaultFoodCategory.OTHERS,
)

fun FoodItemEntity.toDomain() = FoodItem(
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
    notes = notes,
    defaultFoodCategory = category,
    status = status,
    resolvedDate = resolvedDate
)

fun FoodItem.toEntity() = FoodItemEntity(
    id = id,
    name = name,
    normalizedName = name.toGenericNormalized(),
    categoryId = categoryId,
    location = location,
    quantity = quantity,
    itemUnit = itemUnit,
    purchaseDate = purchaseDate,
    expiryDate = expiryDate,
    imageUri = imageUri,
    isFavorite = isFavorite,
    notes = notes,
    category = defaultFoodCategory,
    status = status,
    resolvedDate = resolvedDate
)
