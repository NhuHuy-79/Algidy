package com.nhuhuy.algidy.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("food_templates")
data class FoodTemplateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val categoryId: String? = null,
    val defaultExpiryDays: Int,
)