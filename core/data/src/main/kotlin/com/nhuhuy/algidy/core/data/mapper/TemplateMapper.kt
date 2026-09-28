package com.nhuhuy.algidy.core.data.mapper

import com.nhuhuy.algidy.core.database.entity.FoodTemplateEntity
import com.nhuhuy.algidy.core.database.entity.FoodTemplateWithCategory
import com.nhuhuy.algidy.core.model.food.FoodTemplate

fun FoodTemplate.toEntity(): FoodTemplateEntity {
    return FoodTemplateEntity(
        id = id ?: 0,
        name = name,
        categoryId = category?.id,
        defaultExpiryDays = defaultExpiryDays,
        storageLocation = storageLocation
    )
}

fun FoodTemplateWithCategory.toDomain(): FoodTemplate {
    return FoodTemplate(
        id = template.id,
        name = template.name,
        defaultExpiryDays = template.defaultExpiryDays,
        category = category?.toDomain(),
        storageLocation = template.storageLocation
    )
}
