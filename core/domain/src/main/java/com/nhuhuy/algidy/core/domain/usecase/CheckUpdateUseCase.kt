package com.nhuhuy.algidy.core.domain.usecase

import com.nhuhuy.algidy.core.domain.repository.UpdateRepository
import com.nhuhuy.algidy.core.model.error_handling.Resource

class CheckUpdateUseCase(
    private val updateRepository: UpdateRepository
) {
    suspend operator fun invoke(): Resource<String?> {
        return updateRepository.getTagName()
    }
}