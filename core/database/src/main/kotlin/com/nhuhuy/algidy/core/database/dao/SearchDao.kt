package com.nhuhuy.algidy.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.core.database.entity.SearchHistoryEntity

@Dao
interface SearchDao {

    @Query(
        """
    SELECT food_items.* FROM food_items
    JOIN inventory_items_fts ON food_items.id = inventory_items_fts.id
    WHERE inventory_items_fts.normalized_name MATCH :searchQuery
"""
    )
    fun searchInventory(searchQuery: String): List<FoodItemEntity>

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 10")
    fun getRecentSearchHistory(): List<SearchHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(history: SearchHistoryEntity)

    @Delete
    suspend fun deleteSearchHistory(history: SearchHistoryEntity)

    @Query("DELETE FROM search_history")
    suspend fun clearAllSearchHistory()
}