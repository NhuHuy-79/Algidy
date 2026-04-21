package com.nhuhuy.algidy.core.data.repository

import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.NetworkResult
import kotlinx.coroutines.flow.Flow


interface FoodRepository {
    suspend fun scanFoodBarcode(barcodeString: String) : NetworkResult<FoodItem>
    fun getInventory(): Flow<List<FoodItem>>
    suspend fun addFoodItem(item: FoodItem)
    suspend fun removeFoodItem(id: String)
}