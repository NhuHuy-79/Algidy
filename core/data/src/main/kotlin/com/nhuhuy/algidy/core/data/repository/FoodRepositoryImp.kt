package com.nhuhuy.algidy.core.data.repository

import com.nhuhuy.algidy.core.data.mapper.asEntity
import com.nhuhuy.algidy.core.data.mapper.asExternalModel
import com.nhuhuy.algidy.core.database.dao.FoodDao
import com.nhuhuy.algidy.core.model.FoodItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodRepositoryImpl(
    private val foodDao: FoodDao
) : FoodRepository {

    override fun getInventory(): Flow<List<FoodItem>> {
        return foodDao.getAllFoodItems().map { entities ->
            entities.map { it.asExternalModel() }
        }
    }

    override suspend fun addFoodItem(item: FoodItem) {
        foodDao.insertFood(item.asEntity())
    }

    override suspend fun removeFoodItem(id: String) {
        foodDao.deleteFoodById(id)
    }
}