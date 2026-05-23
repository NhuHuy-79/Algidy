package com.nhuhuy.algidy.core.data.mapper

import com.nhuhuy.algidy.core.database.entity.CategoryEntity
import com.nhuhuy.algidy.core.model.food.FoodCategory

fun CategoryEntity.toDomain(): FoodCategory {
    return FoodCategory(
        id = id,
        name = name,
        description = description
    )
}

fun FoodCategory.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        description = description
    )
}
