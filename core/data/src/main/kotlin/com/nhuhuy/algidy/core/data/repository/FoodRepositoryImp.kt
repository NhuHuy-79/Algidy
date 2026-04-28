package com.nhuhuy.algidy.core.data.repository

import com.nhuhuy.algidy.core.data.mapper.toDomain
import com.nhuhuy.algidy.core.data.mapper.toEntity
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.data.util.safeCallInIO
import com.nhuhuy.algidy.core.database.dao.FoodDao
import com.nhuhuy.algidy.core.model.FoodItem
import com.nhuhuy.algidy.core.model.Resource
import com.nhuhuy.algidy.core.network.data_source.FoodRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodRepositoryImpl(
    private val appDispatchers: AppDispatchers,
    private val foodDao: FoodDao,
    private val foodRemoteDataSource: FoodRemoteDataSource
) : FoodRepository {
    override suspend fun scanFoodBarcode(barcodeString: String): Resource<FoodItem> {
        return safeCallInIO(ioDispatcher = appDispatchers.io){
            foodRemoteDataSource.fetchFoodApiResponse(barcodeString).toDomain()
        }
    }

    override fun getInventory(): Flow<List<FoodItem>> {
        return foodDao.getAllFoodItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getFoodById(id: String): FoodItem? {
        return foodDao.getFoodById(id)?.toDomain()
    }

    override suspend fun addFoodItem(item: FoodItem) {
        foodDao.insertFood(item.toEntity())
    }

    override suspend fun removeFoodItem(id: String) {
        foodDao.deleteFoodById(id)
    }
}