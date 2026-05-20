package com.nhuhuy.algidy.feature.settings.domain.model

import com.nhuhuy.algidy.core.model.error_handling.Resource

sealed interface BackUpResult {
    fun exportToJson(): Resource<Unit>
}
