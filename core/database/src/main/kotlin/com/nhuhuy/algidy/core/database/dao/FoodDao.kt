package com.nhuhuy.algidy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.core.database.entity.FoodItemWithCategory
import com.nhuhuy.algidy.core.model.food.FoodStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao : BaseDao<FoodItemEntity> {
    @Transaction
    @Query("SELECT * FROM food_items ORDER BY expiry_date ASC")
    fun observeAllFoodItems(): Flow<List<FoodItemWithCategory>>

    @Transaction
    @Query("SELECT * FROM food_items WHERE expiry_date >= :beforeTime")
    fun observeAllFoodItemsBeforeTime(beforeTime: Long): Flow<List<FoodItemWithCategory>>

    @Transaction
    @Query("SELECT * FROM food_items ORDER BY expiry_date ASC")
    fun getAllFoodItems(): List<FoodItemWithCategory>

    @Query("SELECT * FROM food_items ORDER BY expiry_date ASC")
    fun getAllFoodItemEntities(): List<FoodItemEntity>

    @Transaction
    @Query("SELECT * FROM food_items WHERE status = :status ORDER BY expiry_date ASC")
    fun observeAllFoodItemsByStatus(status: FoodStatus): Flow<List<FoodItemWithCategory>>

    @Transaction
    @Query("SELECT * FROM food_items WHERE id = :id")
    suspend fun getFoodById(id: String): FoodItemWithCategory?

    @Transaction
    @Query("SELECT * FROM food_items WHERE id = :id")
    fun observeFoodItem(id: String): Flow<FoodItemWithCategory>

    @Query("UPDATE food_items SET status = :newStatus, resolved_date = :resolvedDate WHERE id = :id")
    suspend fun updateFoodStatus(id: String, newStatus: FoodStatus, resolvedDate: Long)

    @Query("UPDATE food_items SET status = :newStatus, resolved_date = :resolvedDate WHERE id IN (:ids)")
    suspend fun updateFoodStatusList(ids: List<String>, newStatus: FoodStatus, resolvedDate: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodItemEntity)

    @Update
    suspend fun updateFood(newFood: FoodItemEntity)

    @Query("DELETE FROM food_items")
    suspend fun deleteAllFoods()

    @Query("DELETE FROM food_items WHERE id = :id")
    suspend fun deleteFoodById(id: String)

    @Query("DELETE FROM food_items WHERE expiry_date < :timestamp")
    suspend fun deleteFoodBeforeTimestamp(timestamp: Long)
}
