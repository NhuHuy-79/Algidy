package com.nhuhuy.algidy.feature.analytics.domain.repository

import com.nhuhuy.algidy.feature.analytics.domain.model.AnalyticsStats
import kotlinx.coroutines.flow.Flow

interface AnalyticsRepository {
    fun getAnalyticsStats(): Flow<AnalyticsStats>
}
