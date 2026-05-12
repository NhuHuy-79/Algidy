package com.nhuhuy.algidy.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nhuhuy.algidy.core.database.entity.WasteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WasteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWasteRecord(waste: WasteEntity)

    @Query("SELECT * FROM waste_records ORDER BY date DESC")
    fun getAllWasteRecords(): Flow<List<WasteEntity>>
}
