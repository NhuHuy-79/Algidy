package com.nhuhuy.algidy.core.data.repository

import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.core.model.food.FoodItem
import kotlinx.coroutines.flow.Flow


interface FoodRepository {
    suspend fun scanFoodBarcode(barcodeString: String): Resource<FoodItem>
    fun getInventory(): Flow<List<FoodItem>>
    suspend fun getFoodById(id: String): FoodItem?
    suspend fun addFoodItem(item: FoodItem): Resource<Unit>
    suspend fun removeFoodItem(id: String)
}