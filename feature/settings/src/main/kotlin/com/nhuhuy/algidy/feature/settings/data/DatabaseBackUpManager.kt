package com.nhuhuy.algidy.feature.settings.data

import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.database.dao.FoodDao
import com.nhuhuy.algidy.feature.settings.mapper.toFlattenFood
import com.nhuhuy.algidy.feature.settings.mapper.toFoodItemEntity
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

interface DatabaseBackUpManager {
    suspend fun exportToJson(): String

    suspend fun importFromJson(jsonString: String)

    suspend fun getAllImageUris(): List<String>
}

class DatabaseBackUpManagerImpl(
    private val appDispatchers: AppDispatchers,
    private val foodDao: FoodDao
) : DatabaseBackUpManager {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    override suspend fun exportToJson(): String {
        return withContext(appDispatchers.io) {
            val flattenFoodData = foodDao.getAllFoodItems().map { foodItemEntity ->
                foodItemEntity.toFlattenFood()
            }

            val flattenExportData = FlattenExportData(
                foodDataVersion = 8,
                foodData = flattenFoodData
            )

            json.encodeToString(flattenExportData)
        }
    }

    override suspend fun importFromJson(jsonString: String) {
        return withContext(appDispatchers.io) {
            val flattenExportData = json.decodeFromString<FlattenExportData>(jsonString)
            val foodItemEntities = flattenExportData.foodData.map { flattenFoodData ->
                flattenFoodData.toFoodItemEntity()
            }
            foodDao.upsertAll(list = foodItemEntities)
        }
    }

    override suspend fun getAllImageUris(): List<String> {
        return foodDao.getAllFoodItems().mapNotNull { foodItemEntity ->
            foodItemEntity.imageUri
        }
    }

}

