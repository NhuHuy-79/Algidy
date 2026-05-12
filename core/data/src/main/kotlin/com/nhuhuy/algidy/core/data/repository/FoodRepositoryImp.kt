package com.nhuhuy.algidy.core.data.repository

import com.nhuhuy.algidy.core.data.mapper.toDomain
import com.nhuhuy.algidy.core.data.mapper.toEntity
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.data.util.safeCall
import com.nhuhuy.algidy.core.database.dao.FoodDao
import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.core.network.data_source.FoodRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodRepositoryImpl(
    private val appDispatchers: AppDispatchers,
    private val foodDao: FoodDao,
    private val foodRemoteDataSource: FoodRemoteDataSource
) : FoodRepository {
    override suspend fun scanFoodBarcode(barcodeString: String): Resource<FoodItem> {
        return safeCall(dispatcher = appDispatchers.io) {
            foodRemoteDataSource.fetchFoodApiResponse(barcodeString).toDomain()
        }
    }

    override fun observeFoodItems(): Flow<List<FoodItem>> {
        return foodDao.observeAllFoodItemsByStatus(status = FoodStatus.ACTIVE).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeAllFoodItems(): Flow<List<FoodItem>> {
        return foodDao.getAllFoodItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun updateFoodItem(item: FoodItem): Resource<Unit> {
        return safeCall(dispatcher = appDispatchers.io) {
            foodDao.updateFood(newFood = item.toEntity())
        }
    }

    override suspend fun updateFoodStatus(
        id: String,
        newStatus: FoodStatus
    ): Resource<String> {
        return safeCall(dispatcher = appDispatchers.io) {
            foodDao.updateFoodStatus(id, newStatus)
            id
        }
    }

    override suspend fun getFoodById(id: String): FoodItem? {
        return foodDao.getFoodById(id)?.toDomain()
    }

    override suspend fun addFoodItem(item: FoodItem): Resource<FoodItem> {
        return safeCall(dispatcher = appDispatchers.io) {
            foodDao.insertFood(item.toEntity())
            item
        }
    }

    override suspend fun removeFoodItem(id: String) {
        foodDao.deleteFoodById(id)
    }
}
