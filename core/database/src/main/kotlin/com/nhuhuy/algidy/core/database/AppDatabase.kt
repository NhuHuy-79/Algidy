package com.nhuhuy.algidy.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.nhuhuy.algidy.core.database.dao.FoodDao
import com.nhuhuy.algidy.core.database.dao.SearchDao
import com.nhuhuy.algidy.core.database.dao.WasteDao
import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.core.database.entity.InventoryItemFtsEntity
import com.nhuhuy.algidy.core.database.entity.SearchHistoryEntity
import com.nhuhuy.algidy.core.database.entity.WasteEntity

@Database(
    entities = [
        FoodItemEntity::class,
        WasteEntity::class,
        SearchHistoryEntity::class,
        InventoryItemFtsEntity::class
    ],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 4, to = 5)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun wasteDao(): WasteDao
    abstract fun searchDao(): SearchDao
}
