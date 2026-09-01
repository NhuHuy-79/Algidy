package com.nhuhuy.algidy.feature.settings.data.repository

import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.database.dao.CategoryDao
import com.nhuhuy.algidy.core.database.dao.FoodDao
import com.nhuhuy.algidy.feature.settings.data.constant.BackupConstant
import com.nhuhuy.algidy.feature.settings.data.model.ExportData
import com.nhuhuy.algidy.feature.settings.domain.repository.ExportDataProvider
import com.nhuhuy.algidy.feature.settings.mapper.toFlattenCategoryData
import com.nhuhuy.algidy.feature.settings.mapper.toFlattenFood
import kotlinx.coroutines.withContext

class ExportDataProviderImpl(
    private val dispatchers: AppDispatchers,
    private val foodDao: FoodDao,
    private val categoryDao: CategoryDao,
) : ExportDataProvider {
    override suspend fun getExportData(): ExportData {
        return withContext(dispatchers.io) {
            val foodEntities = foodDao.getAllFoodItems().map { it.toFlattenFood() }
            val categoryEntities = categoryDao.getAllCategories().map { it.toFlattenCategoryData() }

            ExportData(
                schemaVersion = BackupConstant.VERSION_CODE,
                foods = foodEntities,
                categories = categoryEntities
            )
        }
    }
}