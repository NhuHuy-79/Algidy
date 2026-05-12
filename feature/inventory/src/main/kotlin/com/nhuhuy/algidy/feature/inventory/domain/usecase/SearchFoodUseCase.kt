package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository

class SearchFoodUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(searchQuery: String): List<FoodItem> {
        return searchRepository.getFoodItemListByQuery(query = searchQuery)
    }
}