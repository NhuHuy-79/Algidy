package com.nhuhuy.algidy.core.data.repository

import com.nhuhuy.algidy.core.model.food.FoodCategory
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getAllCategories(): List<FoodCategory>
    fun observeAllCategories(): Flow<List<FoodCategory>>
    suspend fun addCategory(category: FoodCategory)
    suspend fun updateCategory(category: FoodCategory)
    suspend fun deleteCategory(id: String)
}
