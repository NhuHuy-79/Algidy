package com.nhuhuy.algidy.feature.inventory.domain.repository

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.feature.inventory.domain.model.SearchHistory

interface SearchRepository {
    suspend fun addHistoryResult(searchHistory: SearchHistory)
    suspend fun getHistoryResultList(): List<SearchHistory>
    suspend fun getFoodItemListByQuery(query: String): List<FoodItem>
}