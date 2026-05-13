package com.nhuhuy.algidy.feature.inventory.data.repository

import com.nhuhuy.algidy.core.data.mapper.toDomain
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.database.dao.SearchDao
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.feature.inventory.data.mapper.toDomain
import com.nhuhuy.algidy.feature.inventory.domain.model.HistoryResult
import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository
import kotlinx.coroutines.withContext

class SearchRepositoryImp(
    private val appDispatchers: AppDispatchers,
    private val searchDao: SearchDao
) : SearchRepository {
    override suspend fun getHistoryResultList(): List<HistoryResult> {
        return withContext(appDispatchers.io) {
            searchDao.getRecentSearchHistory().map { historyEntity ->
                historyEntity.toDomain()
            }
        }
    }

    override suspend fun getFoodItemListByQuery(query: String): List<FoodItem> {
        return withContext(appDispatchers.io) {
            searchDao.searchInventory(query).map { foodItemEntity ->
                foodItemEntity.toDomain()
            }
        }
    }

}