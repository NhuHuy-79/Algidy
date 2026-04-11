package com.nhuhuy.algidy.core.data.repository

import com.nhuhuy.algidy.core.model.FoodItem
import kotlinx.coroutines.flow.Flow


interface FoodRepository {
    fun getInventory(): Flow<List<FoodItem>>
    suspend fun addFoodItem(item: FoodItem)
    suspend fun removeFoodItem(id: String)
}