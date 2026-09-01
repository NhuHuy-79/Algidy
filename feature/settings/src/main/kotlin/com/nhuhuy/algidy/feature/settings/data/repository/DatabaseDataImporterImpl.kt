package com.nhuhuy.algidy.feature.settings.data.repository

import androidx.room.withTransaction
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.database.AppDatabase
import com.nhuhuy.algidy.feature.settings.data.model.ExportData
import com.nhuhuy.algidy.feature.settings.domain.repository.DatabaseDataImporter
import com.nhuhuy.algidy.feature.settings.mapper.toCategoryEntity
import com.nhuhuy.algidy.feature.settings.mapper.toFoodItemEntity
import kotlinx.coroutines.withContext

class DatabaseDataImporterImpl(
    private val appDispatchers: AppDispatchers,
    private val database: AppDatabase,
) : DatabaseDataImporter {

    private val foodDao = database.foodDao()
    private val categoryDao = database.categoryDao()

    override suspend fun import(
        data: ExportData,
    ) {
        withContext(appDispatchers.io) {
            database.withTransaction {
                categoryDao.upsertAll(
                    data.categories.map {
                        it.toCategoryEntity()
                    }
                )

                foodDao.upsertAll(
                    data.foods.map {
                        it.toFoodItemEntity()
                    }
                )
            }
        }
    }
}