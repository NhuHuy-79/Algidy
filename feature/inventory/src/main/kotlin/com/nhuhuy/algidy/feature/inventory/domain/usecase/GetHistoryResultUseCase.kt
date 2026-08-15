package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.feature.inventory.domain.model.SearchHistory
import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository

class GetHistoryResultUseCase(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(): List<SearchHistory> {
        return searchRepository.getHistoryResultList()
    }
}