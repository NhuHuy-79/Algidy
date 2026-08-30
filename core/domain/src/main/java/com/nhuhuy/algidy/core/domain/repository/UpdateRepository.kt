package com.nhuhuy.algidy.core.domain.repository

import com.nhuhuy.algidy.core.model.error_handling.Resource

interface UpdateRepository {
    suspend fun getTagName(): Resource<String?>
}