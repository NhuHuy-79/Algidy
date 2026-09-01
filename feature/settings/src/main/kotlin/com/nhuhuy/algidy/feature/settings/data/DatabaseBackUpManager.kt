package com.nhuhuy.algidy.feature.settings.data

import androidx.room.withTransaction
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.database.AppDatabase
import com.nhuhuy.algidy.core.database.DatabaseConstant
import com.nhuhuy.algidy.feature.settings.data.model.ExportData
import com.nhuhuy.algidy.feature.settings.mapper.toCategoryEntity
import com.nhuhuy.algidy.feature.settings.mapper.toFlattenCategoryData
import com.nhuhuy.algidy.feature.settings.mapper.toFlattenFood
import com.nhuhuy.algidy.feature.settings.mapper.toFoodItemEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

interface DatabaseBackUpManager {
    suspend fun exportToJson(): String

    suspend fun importFromJson(jsonString: String)

    suspend fun getAllImageUris(): List<String>
}

class DatabaseBackUpManagerImpl(
    private val appDispatchers: AppDispatchers,
    private val database: AppDatabase,
) : DatabaseBackUpManager {
    private val foodDao = database.foodDao()
    private val categoryDao = database.categoryDao()
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    override suspend fun exportToJson(): String {
        return withContext(appDispatchers.io) {
            val flattenFoodData = foodDao.getAllFoodItems().map { foodItemEntity ->
                foodItemEntity.toFlattenFood()
            }

            val flattenCategoryData = categoryDao.getAllCategories().map { categoryEntity ->
                categoryEntity.toFlattenCategoryData()
            }

            val exportData = ExportData(
                schemaVersion = DatabaseConstant.SCHEMA_VERSION,
                foods = flattenFoodData,
                categories = flattenCategoryData
            )

            json.encodeToString(exportData)
        }
    }

    override suspend fun importFromJson(jsonString: String) {
        withContext(appDispatchers.io) {
            val exportData =
                json.decodeFromString<ExportData>(jsonString)

            database.withTransaction {
                val foodDeferred = async {
                    exportData.foods.map {
                        it.toFoodItemEntity()
                    }
                }

                val categoryDeferred = async {
                    exportData.categories.map {
                        it.toCategoryEntity()
                    }
                }

                categoryDao.upsertAll(categoryDeferred.await())
                foodDao.upsertAll(foodDeferred.await())
            }
        }
    }

    override suspend fun getAllImageUris(): List<String> {
        return foodDao.getAllFoodItems().mapNotNull { foodItemEntity ->
            foodItemEntity.foodItem.imageUri
        }
    }

}

