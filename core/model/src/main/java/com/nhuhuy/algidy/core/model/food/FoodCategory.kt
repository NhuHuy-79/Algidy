package com.nhuhuy.algidy.core.model.food

import java.util.UUID

/**
 * Domain model representing a food category.
 */
data class FoodCategory(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = ""
)
