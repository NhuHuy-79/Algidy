package com.nhuhuy.algidy.feature.analytics.domain.model

import java.time.LocalDate

enum class DayOfWeekends {
    MON, TUE, WED, THU, FRI, SAT, SUN
}


data class DailyFreshnessStats(
    val date: LocalDate,
    val freshCount: Double,
    val expiredCount: Double,
    val urgentCount: Double,
    val warningCount: Double
)
