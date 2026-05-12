package com.nhuhuy.algidy.feature.inventory.domain.repository

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.feature.inventory.domain.model.HistoryResult

interface SearchRepository {
    suspend fun getHistoryResultList(): List<HistoryResult>
    suspend fun getFoodItemListByQuery(query: String): List<FoodItem>
}