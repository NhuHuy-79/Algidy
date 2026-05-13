package com.nhuhuy.algidy.feature.inventory.data.repository

import com.nhuhuy.algidy.core.data.mapper.toDomain
import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.database.dao.SearchDao
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.feature.inventory.data.mapper.toDomain
import com.nhuhuy.algidy.feature.inventory.data.mapper.toSearchHistoryEntity
import com.nhuhuy.algidy.feature.inventory.domain.model.HistoryResult
import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository
import kotlinx.coroutines.withContext

class SearchRepositoryImp(
    private val appDispatchers: AppDispatchers,
    private val searchDao: SearchDao
) : SearchRepository {
    override suspend fun addHistoryResult(historyResult: HistoryResult) {
        return withContext(appDispatchers.io) {
            searchDao.insertSearchHistory(historyResult.toSearchHistoryEntity())
        }
    }

    override suspend fun getHistoryResultList(): List<HistoryResult> {
        return withContext(appDispatchers.io) {
            searchDao.getRecentSearchHistory().map { historyEntity ->
                historyEntity.toDomain()
            }
        }
    }

    override suspend fun getFoodItemListByQuery(query: String): List<FoodItem> {
        if (query.isBlank()) return emptyList()
        // Loại bỏ ký tự đặc biệt trừ khoảng trắng, FTS MATCH không thích ký tự lạ
        val sanitizedQuery = query.trim().replace(Regex("[^a-zA-Z0-9\\s]"), "")
        if (sanitizedQuery.isEmpty()) return emptyList()

        // Phân tách các từ và thêm * vào mỗi từ để tìm kiếm linh hoạt hơn
        // Ví dụ: "thịt bò" -> "thịt* bò*"
        val ftsQuery = sanitizedQuery.split(Regex("\\s+"))
            .filter { it.isNotEmpty() }
            .joinToString(" ") { "$it*" }

        return withContext(appDispatchers.io) {
            searchDao.searchInventory(ftsQuery).map { foodItemEntity ->
                foodItemEntity.toDomain()
            }
        }
    }

}