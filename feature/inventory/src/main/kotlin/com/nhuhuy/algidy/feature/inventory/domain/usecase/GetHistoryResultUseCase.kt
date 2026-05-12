package com.nhuhuy.algidy.feature.inventory.domain.usecase

import com.nhuhuy.algidy.feature.inventory.domain.model.HistoryResult
import com.nhuhuy.algidy.feature.inventory.domain.repository.SearchRepository

class GetHistoryResultUseCase(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(): List<HistoryResult> {
        return searchRepository.getHistoryResultList()
    }
}