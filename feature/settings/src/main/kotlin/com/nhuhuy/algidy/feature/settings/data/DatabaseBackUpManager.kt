package com.nhuhuy.algidy.feature.settings.data

import androidx.room.withTransaction
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.database.AppDatabase
import com.nhuhuy.algidy.core.database.DatabaseConstant
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

            val flattenExportData = FlattenExportData(
                foodDataVersion = DatabaseConstant.SCHEMA_VERSION,
                foodData = flattenFoodData,
                category = flattenCategoryData
            )

            json.encodeToString(flattenExportData)
        }
    }

    override suspend fun importFromJson(jsonString: String) {
        withContext(appDispatchers.io) {
            val flattenExportData =
                json.decodeFromString<FlattenExportData>(jsonString)

            database.withTransaction {
                val foodDeferred = async {
                    flattenExportData.foodData.map {
                        it.toFoodItemEntity()
                    }
                }

                val categoryDeferred = async {
                    flattenExportData.category.map {
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

