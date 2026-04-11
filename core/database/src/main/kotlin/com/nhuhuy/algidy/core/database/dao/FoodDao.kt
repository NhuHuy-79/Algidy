package com.nhuhuy.algidy.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nhuhuy.algidy.core.database.entity.FoodEntity
import com.nhuhuy.algidy.core.model.StorageLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_items ORDER BY expiry_date ASC")
    fun getAllFoodItems(): Flow<List<FoodEntity>>

    // Lấy thực phẩm theo vị trí (Ngăn đông, Ngăn mát...)
    @Query("SELECT * FROM food_items WHERE location = :location")
    fun getFoodItemsByLocation(location: StorageLocation): Flow<List<FoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodEntity)

    @Delete
    suspend fun deleteFood(food: FoodEntity)

    @Query("DELETE FROM food_items WHERE id = :id")
    suspend fun deleteFoodById(id: String)
}