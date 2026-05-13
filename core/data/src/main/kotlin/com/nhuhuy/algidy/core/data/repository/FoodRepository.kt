package com.nhuhuy.algidy.core.data.repository

import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.FoodStatus
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    //CREATE
    suspend fun addFoodItem(item: FoodItem): Resource<FoodItem>
    //READ
    suspend fun getFoodById(id: String): FoodItem?
    suspend fun scanFoodBarcode(barcodeString: String): Resource<FoodItem>
    suspend fun getAllFoodItems(): List<FoodItem>
    fun observeFoodItems(): Flow<List<FoodItem>>
    fun observeAllFoodItems(): Flow<List<FoodItem>>

    //UPDATE
    suspend fun updateFoodItem(item: FoodItem): Resource<Unit>
    suspend fun updateFoodStatus(id: String, newStatus: FoodStatus): Resource<String>

    //DELETE
    suspend fun removeFoodItem(id: String)
}
