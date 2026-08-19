package com.nhuhuy.algidy.core.data.repository

import com.nhuhuy.algidy.core.data.mapper.toDomain
import com.nhuhuy.algidy.core.data.mapper.toEntity
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.database.dao.CategoryDao
import com.nhuhuy.algidy.core.domain.repository.CategoryRepository
import com.nhuhuy.algidy.core.model.food.FoodCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CategoryRepositoryImpl(
    private val appDispatchers: AppDispatchers,
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override suspend fun getAllCategories(): List<FoodCategory> {
        return withContext(appDispatchers.io) {
            categoryDao.getAllCategories().map {
                it.toDomain()
            }
        }
    }

    override fun observeAllCategories(): Flow<List<FoodCategory>> {
        return categoryDao.observeAllCategories().map { entities ->
            entities.map { it.toDomain() }
        }.flowOn(appDispatchers.io)
    }

    override suspend fun addCategory(category: FoodCategory) {
        withContext(appDispatchers.io) {
            categoryDao.insertCategory(category.toEntity())
        }
    }

    override suspend fun updateCategory(category: FoodCategory) {
        withContext(appDispatchers.io) {
            categoryDao.update(category.toEntity())
        }
    }

    override suspend fun deleteCategory(id: String) {
        withContext(appDispatchers.io) {
            categoryDao.deleteCategory(id)
        }
    }

    override suspend fun getCategoryById(id: String): FoodCategory? {
        return withContext(appDispatchers.io) {
            categoryDao.getCategoryById(id)?.toDomain()
        }
    }
}
