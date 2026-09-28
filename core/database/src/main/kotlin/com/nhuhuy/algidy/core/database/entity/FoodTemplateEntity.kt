package com.nhuhuy.algidy.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.nhuhuy.algidy.core.model.food.StorageLocation

@Entity("food_templates")
data class FoodTemplateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val categoryId: String? = null,
    val defaultExpiryDays: Int,
    val storageLocation: StorageLocation
)

data class FoodTemplateWithCategory(
    @Embedded val template: FoodTemplateEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity?
)
