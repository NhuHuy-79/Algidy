package com.nhuhuy.algidy.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * POJO representing a food item with its associated category.
 */
data class FoodItemWithCategory(
    @Embedded
    val foodItem: FoodItemEntity,

    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: CategoryEntity?
)
