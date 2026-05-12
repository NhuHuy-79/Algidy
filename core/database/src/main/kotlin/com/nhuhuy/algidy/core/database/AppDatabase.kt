package com.nhuhuy.algidy.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nhuhuy.algidy.core.database.dao.FoodDao
import com.nhuhuy.algidy.core.database.dao.WasteDao
import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.core.database.entity.WasteEntity

@Database(
    entities = [FoodItemEntity::class, WasteEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun wasteDao(): WasteDao
}
