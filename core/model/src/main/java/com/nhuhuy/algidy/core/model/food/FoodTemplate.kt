package com.nhuhuy.algidy.core.model.food

data class FoodTemplate(
    val id: Long? = null,
    val name: String,
    val defaultExpiryDays: Int,
    val storageLocation: StorageLocation,
    val category: FoodCategory? = null,
)