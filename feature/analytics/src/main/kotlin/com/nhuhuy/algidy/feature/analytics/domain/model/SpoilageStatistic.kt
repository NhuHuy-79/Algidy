package com.nhuhuy.algidy.feature.analytics.domain.model

import java.time.LocalDate

data class SpoilageStatistic(
    val period: AnalyticsPeriod,
    val points: List<SpoilagePoint>,
)

data class SpoilagePoint(
    val date: LocalDate,
    val waste: Int,
    val consumed: Int,
)

val fakeWeeklySpoilageStatistic = SpoilageStatistic(
    period = AnalyticsPeriod.WEEK,
    points = listOf(
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
    ),
)