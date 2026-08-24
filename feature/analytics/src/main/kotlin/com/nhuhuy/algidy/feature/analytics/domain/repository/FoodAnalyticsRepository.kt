package com.nhuhuy.algidy.feature.analytics.domain.repository

import com.nhuhuy.algidy.feature.analytics.domain.model.AnalyticsPeriod
import com.nhuhuy.algidy.feature.analytics.domain.model.FreshnessStatistic
import com.nhuhuy.algidy.feature.analytics.domain.model.SpoilageStatistic

interface FoodAnalyticsRepository {
    suspend fun getFreshnessStatistic(
        period: AnalyticsPeriod,
    ): FreshnessStatistic

    suspend fun getSpoilageStatistic(
        period: AnalyticsPeriod,
    ): SpoilageStatistic
}