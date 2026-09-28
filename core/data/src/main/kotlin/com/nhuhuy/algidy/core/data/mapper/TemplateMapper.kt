package com.nhuhuy.algidy.core.data.mapper

import com.nhuhuy.algidy.core.database.entity.FoodTemplateEntity
import com.nhuhuy.algidy.core.model.food.FoodTemplate

fun FoodTemplate.toEntity(): FoodTemplateEntity {
    return FoodTemplateEntity(
        id = id ?: 0,
        name = name,
        categoryId = categoryId,
        defaultExpiryDays = defaultExpiryDays
    )
}

fun FoodTemplateEntity.toDomain(): FoodTemplate {
    return FoodTemplate(
        id = id,
        name = name,
        categoryId = categoryId,
        defaultExpiryDays = defaultExpiryDays
    )
}