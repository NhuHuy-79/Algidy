package com.nhuhuy.algidy.core.data.repository

import com.nhuhuy.algidy.core.data.mapper.toDomain
import com.nhuhuy.algidy.core.data.mapper.toEntity
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.data.util.safeCall
import com.nhuhuy.algidy.core.database.TransactionRunner
import com.nhuhuy.algidy.core.database.dao.CategoryDao
import com.nhuhuy.algidy.core.database.dao.FoodDao
import com.nhuhuy.algidy.core.database.dao.SearchDao
import com.nhuhuy.algidy.core.model.error_handling.Resource
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.core.network.data_source.FoodRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FoodRepositoryImpl(
    private val appDispatchers: AppDispatchers,
    private val transactionRunner: TransactionRunner,
    private val foodDao: FoodDao,
    private val categoryDao: CategoryDao,
    private val searchDao: SearchDao,
    private val foodRemoteDataSource: FoodRemoteDataSource
) : FoodRepository {
    override suspend fun scanFoodBarcode(barcodeString: String): Resource<FoodItem> {
        return safeCall(dispatcher = appDispatchers.io) {
            foodRemoteDataSource.fetchFoodApiResponse(barcodeString).toDomain()
        }
    }

    override suspend fun getAllFoodItems(): List<FoodItem> {
        return withContext(appDispatchers.io) {
            foodDao.getAllFoodItems().map { foodItemEntity ->
                foodItemEntity.toDomain()
            }
        }
    }

    override fun observeAllActiveFoodItems(): Flow<List<FoodItem>> {
        return foodDao.observeAllFoodItemsByStatus(status = FoodStatus.ACTIVE).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeAllFoodItems(): Flow<List<FoodItem>> {
        return foodDao.observeAllFoodItems().map { entities ->
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
            foodDao.updateFoodStatus(id, newStatus, System.currentTimeMillis())
            id
        }
    }

    override suspend fun updateFoodStatusList(
        ids: List<String>,
        newStatus: FoodStatus
    ): Resource<Unit> {
        return safeCall(dispatcher = appDispatchers.io) {
            foodDao.updateFoodStatusList(ids, newStatus, System.currentTimeMillis())
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

    override suspend fun getFoodUriList(): List<String> {
        return getAllFoodItems().mapNotNull { foodItem ->
            foodItem.imageUri
        }
    }

    override fun observeFoodItemById(id: String): Flow<FoodItem> {
        return foodDao.observeFoodItem(id).map { entity -> entity.toDomain() }
    }

    override fun observeFoodItemBeforeTime(beforeTime: Long): Flow<List<FoodItem>> {
        return foodDao.observeAllFoodItemsBeforeTime(beforeTime).map { entities ->
            entities.map { it.toDomain() }
        }.flowOn(appDispatchers.io)
    }


    override suspend fun removeFoodItem(id: String) {
        foodDao.deleteFoodById(id)
    }

    override suspend fun deleteAllFoodItems(): Resource<Unit> {
        return safeCall(dispatcher = appDispatchers.io) {
            transactionRunner.run {
                categoryDao.deleteAllCategories()
                foodDao.deleteAllFoods()
                searchDao.clearAllSearchHistory()
            }
        }
    }

    override suspend fun deleteFoodAfterDay(day: Long): Resource<Unit> {
        return safeCall(dispatcher = appDispatchers.io) {
            foodDao.deleteFoodBeforeTimestamp(day)
        }
    }
}
