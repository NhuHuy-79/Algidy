package com.nhuhuy.algidy.core.database.dao

import androidx.room.Query
import com.nhuhuy.algidy.core.database.entity.FoodTemplateEntity

interface FoodTemplateDao : BaseDao<FoodTemplateEntity> {

    @Query("SELECT * FROM food_templates")
    suspend fun getFoodTemplates(): List<FoodTemplateEntity>

    @Query("SELECT * FROM food_templates WHERE id = :id")
    suspend fun getFoodTemplateById(id: Long): FoodTemplateEntity?

    @Query("DELETE FROM food_templates WHERE id = :id")
    suspend fun deleteFoodTemplateById(id: Long)

    @Query("DELETE FROM food_templates")
    suspend fun deleteAllFoodTemplates()
}