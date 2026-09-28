package com.nhuhuy.algidy.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nhuhuy.algidy.core.database.entity.FoodTemplateEntity
import com.nhuhuy.algidy.core.database.entity.FoodTemplateWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodTemplateDao : BaseDao<FoodTemplateEntity> {
    @Transaction
    @Query("SELECT * FROM food_templates")
    fun observeFoodTemplates(): Flow<List<FoodTemplateWithCategory>>


    @Transaction
    @Query("SELECT * FROM food_templates")
    suspend fun getFoodTemplates(): List<FoodTemplateWithCategory>

    @Transaction
    @Query("SELECT * FROM food_templates WHERE id = :id")
    suspend fun getFoodTemplateById(id: Long): FoodTemplateWithCategory?

    @Query("DELETE FROM food_templates WHERE id = :id")
    suspend fun deleteFoodTemplateById(id: Long)

    @Query("DELETE FROM food_templates")
    suspend fun deleteAllFoodTemplates()
}