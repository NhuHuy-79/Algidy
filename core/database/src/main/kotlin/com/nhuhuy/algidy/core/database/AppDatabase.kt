package com.nhuhuy.algidy.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nhuhuy.algidy.core.database.dao.CategoryDao
import com.nhuhuy.algidy.core.database.dao.FoodDao
import com.nhuhuy.algidy.core.database.dao.SearchDao
import com.nhuhuy.algidy.core.database.dao.WasteDao
import com.nhuhuy.algidy.core.database.entity.CategoryEntity
import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.core.database.entity.InventoryItemFtsEntity
import com.nhuhuy.algidy.core.database.entity.SearchHistoryEntity
import com.nhuhuy.algidy.core.database.entity.WasteEntity

@Database(
    entities = [
        FoodItemEntity::class,
        WasteEntity::class,
        SearchHistoryEntity::class,
        InventoryItemFtsEntity::class,
        CategoryEntity::class
    ],
    version = DatabaseConstant.SCHEMA_VERSION,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun wasteDao(): WasteDao
    abstract fun searchDao(): SearchDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create the new table
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `food_items_new` (
                        `id` TEXT NOT NULL, 
                        `name` TEXT NOT NULL, 
                        `normalized_name` TEXT NOT NULL DEFAULT '',
                        `category_id` TEXT, 
                        `location` TEXT NOT NULL, 
                        `purchase_date` INTEGER NOT NULL, 
                        `expiry_date` INTEGER NOT NULL, 
                        `image_uri` TEXT, 
                        `notes` TEXT NOT NULL, 
                        `status` TEXT NOT NULL, 
                        `resolved_date` INTEGER, 
                        PRIMARY KEY(`id`), 
                        FOREIGN KEY(`category_id`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                    """.trimIndent()
                )

                // Copy the data
                db.execSQL(
                    """
                    INSERT INTO `food_items_new` (id, name, normalized_name, category_id, location, purchase_date, expiry_date, image_uri, notes, status, resolved_date)
                    SELECT id, name, normalized_name, category_id, location, purchase_date, expiry_date, image_uri, notes, status, resolved_date FROM `food_items`
                    """.trimIndent()
                )

                // Remove the old table
                db.execSQL("DROP TABLE `food_items`")

                // Rename the new table to the original name
                db.execSQL("ALTER TABLE `food_items_new` RENAME TO `food_items`")

                // Re-create indices if necessary
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_food_items_category_id` ON `food_items` (`category_id`)")
            }
        }
    }
}

object DatabaseConstant {
    const val SCHEMA_VERSION = 13
}