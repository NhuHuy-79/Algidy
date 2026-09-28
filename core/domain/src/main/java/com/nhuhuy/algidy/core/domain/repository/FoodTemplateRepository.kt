package com.nhuhuy.algidy.core.domain.repository

import com.nhuhuy.algidy.core.model.food.FoodTemplate
import kotlinx.coroutines.flow.Flow

interface FoodTemplateRepository {
    fun observeFoodTemplates(): Flow<List<FoodTemplate>>
    suspend fun getFoodTemplates(): List<FoodTemplate>
    suspend fun getFoodTemplateById(id: Long): FoodTemplate?
    suspend fun addOrUpdateTemplate(foodTemplate: FoodTemplate)
    suspend fun deleteFoodTemplate(id: Long)
    suspend fun deleteAllFoodTemplates()
}