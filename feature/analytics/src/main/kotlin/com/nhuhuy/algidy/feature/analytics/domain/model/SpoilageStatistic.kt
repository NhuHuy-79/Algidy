package com.nhuhuy.algidy.feature.analytics.domain.model

import com.nhuhuy.algidy.feature.analytics.presentation.model.toUiModel
import java.time.LocalDate

data class SpoilageStatistic(
    val period: AnalyticsPeriod = AnalyticsPeriod.WEEK,
    val points: List<SpoilagePoint> = emptyList(),
)

data class SpoilagePoint(
    val date: LocalDate,
    val waste: Int,
    val consumed: Int,
)

val fakeWeeklySpoilageStatistic = listOf(
        SpoilagePoint(
            date = LocalDate.now().minusDays(6),
            waste = 1,
            consumed = 4,
        ),
        SpoilagePoint(
            date = LocalDate.now().minusDays(5),
            waste = 0,
            consumed = 2,
        ),
        SpoilagePoint(
            date = LocalDate.now().minusDays(4),
            waste = 2,
            consumed = 5,
        ),
        SpoilagePoint(
            date = LocalDate.now().minusDays(3),
            waste = 1,
            consumed = 3,
        ),
        SpoilagePoint(
            date = LocalDate.now().minusDays(2),
            waste = 3,
            consumed = 2,
        ),
        SpoilagePoint(
            date = LocalDate.now().minusDays(1),
            waste = 0,
            consumed = 6,
        ),
        SpoilagePoint(
            date = LocalDate.now(),
            waste = 2,
            consumed = 4,
        ),
).map { it.toUiModel() }