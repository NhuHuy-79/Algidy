package com.nhuhuy.algidy.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.core.model.food.StorageLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_items ORDER BY expiry_date ASC")
    fun observeAllFoodItems(): Flow<List<FoodItemEntity>>

    @Query("SELECT * FROM food_items ORDER BY expiry_date ASC")
    fun getAllFoodItems(): List<FoodItemEntity>
    @Query("SELECT * FROM food_items WHERE status = :status ORDER BY expiry_date ASC")
    fun observeAllFoodItemsByStatus(status: FoodStatus): Flow<List<FoodItemEntity>>

    @Query("SELECT * FROM food_items WHERE id = :id")
    suspend fun getFoodById(id: String): FoodItemEntity?

    @Query("UPDATE food_items SET status = :newStatus WHERE id = :id")
    suspend fun updateFoodStatus(id: String, newStatus: FoodStatus)

    @Query("SELECT * FROM food_items WHERE location = :location")
    fun getFoodItemsByLocation(location: StorageLocation): Flow<List<FoodItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodItemEntity)

    @Update
    suspend fun updateFood(newFood: FoodItemEntity)
    @Delete
    suspend fun deleteFood(food: FoodItemEntity)

    @Query("DELETE FROM food_items WHERE id = :id")
    suspend fun deleteFoodById(id: String)
}
