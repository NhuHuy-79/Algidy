package com.nhuhuy.algidy.feature.analytics.domain.model

import java.time.LocalDate


data class DailyFreshnessStats(
    val date: LocalDate,
    val freshCount: Double,
    val expiredCount: Double,
    val urgentCount: Double,
    val warningCount: Double
)
