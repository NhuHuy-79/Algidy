package com.nhuhuy.algidy.feature.inventory.domain.usecase.food

import com.nhuhuy.algidy.feature.inventory.domain.model.SearchHistory
import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository

class UpdateSearchHistoryUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(searchQuery: String) {
        val searchHistory = SearchHistory(
            name = searchQuery,
            timeStamp = System.currentTimeMillis()
        )
        return searchRepository.addHistoryResult(searchHistory)
    }
}