package com.nhuhuy.algidy.feature.scanner.domain

import com.nhuhuy.algidy.core.model.error_handling.Resource

interface TextExtractor {
    suspend fun extractTextFromImage(uriString: String): Resource<String>
}
