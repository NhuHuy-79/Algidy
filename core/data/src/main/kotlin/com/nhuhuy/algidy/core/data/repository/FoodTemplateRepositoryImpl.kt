package com.nhuhuy.algidy.core.data.repository

import com.nhuhuy.algidy.core.data.mapper.toDomain
import com.nhuhuy.algidy.core.data.mapper.toEntity
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.database.dao.FoodTemplateDao
import com.nhuhuy.algidy.core.domain.repository.FoodTemplateRepository
import com.nhuhuy.algidy.core.model.food.FoodTemplate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FoodTemplateRepositoryImpl(
    private val dispatchers: AppDispatchers,
    private val foodTemplateDao: FoodTemplateDao
) : FoodTemplateRepository {
    override fun observeFoodTemplates(): Flow<List<FoodTemplate>> {
        return foodTemplateDao.observeFoodTemplates().map {
            it.map { it.toDomain() }
        }
            .distinctUntilChanged()
            .flowOn(dispatchers.io)
    }

    override suspend fun getFoodTemplates(): List<FoodTemplate> {
        return withContext(dispatchers.io) {
            foodTemplateDao.getFoodTemplates().map { it.toDomain() }
        }
    }

    override suspend fun getFoodTemplateById(id: Long): FoodTemplate? {
        return withContext(dispatchers.io) {
            foodTemplateDao.getFoodTemplateById(id)?.toDomain()
        }
    }

    override suspend fun addOrUpdateTemplate(foodTemplate: FoodTemplate) {
        withContext(dispatchers.io) {
            foodTemplateDao.upsert(foodTemplate.toEntity())
        }
    }

    override suspend fun deleteFoodTemplate(id: Long) {
        withContext(dispatchers.io) {
            foodTemplateDao.deleteFoodTemplateById(id)
        }
    }

    override suspend fun deleteAllFoodTemplates() {
        withContext(dispatchers.io) {
            foodTemplateDao.deleteAllFoodTemplates()
        }
    }

}