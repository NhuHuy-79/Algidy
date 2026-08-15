package com.nhuhuy.algidy.core.data.mapper

import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.core.database.entity.FoodItemWithCategory
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.network.model.FoodApiResponse
import com.nhuhuy.algidy.toGenericNormalized
import java.util.UUID


fun FoodApiResponse.toDomain() = FoodItem(
    id = UUID.randomUUID().toString(),
    name = product?.productName.orEmpty(),
    categoryId = null,
    location = StorageLocation.OTHER,
    purchaseDate = System.currentTimeMillis(),
    expiryDate = -1,
    imageUri = product?.imageUrl,
    note = ""
)

fun FoodItemEntity.toDomain() = FoodItem(
    id = id,
    name = name,
    categoryId = categoryId,
    location = location,
    purchaseDate = purchaseDate,
    expiryDate = expiryDate,
    imageUri = imageUri,
    note = notes,
    status = status,
    resolvedDate = resolvedDate
)

fun FoodItemWithCategory.toDomain() = FoodItem(
    id = foodItem.id,
    name = foodItem.name,
    categoryId = foodItem.categoryId,
    location = foodItem.location,
    purchaseDate = foodItem.purchaseDate,
    expiryDate = foodItem.expiryDate,
    imageUri = foodItem.imageUri,
    note = foodItem.notes,
    status = foodItem.status,
    resolvedDate = foodItem.resolvedDate,
    category = category?.toDomain()
)

fun FoodItem.toEntity() = FoodItemEntity(
    id = id,
    name = name,
    normalizedName = name.toGenericNormalized(),
    categoryId = categoryId?.ifBlank { null },
    location = location,
    purchaseDate = purchaseDate,
    expiryDate = expiryDate,
    imageUri = imageUri,
    notes = note,
    status = status,
    resolvedDate = resolvedDate
)
