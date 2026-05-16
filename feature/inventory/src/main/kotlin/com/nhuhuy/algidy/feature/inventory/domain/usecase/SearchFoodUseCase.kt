package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.feature.inventory.domain.model.HistoryResult
import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository
import com.nhuhuy.algidy.toGenericNormalized

class SearchFoodUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(searchQuery: String): List<FoodItem> {
        val historyResult = HistoryResult(name = searchQuery)
        val normalizedQuery = searchQuery.toGenericNormalized()
        return searchRepository.getFoodItemListByQuery(query = normalizedQuery).also {
            searchRepository.addHistoryResult(historyResult)
        }
    }
}