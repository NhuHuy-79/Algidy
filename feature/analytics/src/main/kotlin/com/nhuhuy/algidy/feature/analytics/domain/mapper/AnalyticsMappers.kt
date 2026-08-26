package com.nhuhuy.algidy.feature.analytics.domain.mapper

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.feature.analytics.domain.model.AnalyticsPeriod
import com.nhuhuy.algidy.feature.analytics.domain.model.ExpiryOverviewStatistic
import com.nhuhuy.algidy.feature.analytics.domain.model.FreshnessStatistic
import com.nhuhuy.algidy.feature.analytics.domain.model.SpoilagePoint
import com.nhuhuy.algidy.feature.analytics.domain.model.SpoilageStatistic
import com.nhuhuy.algidy.feature.analytics.domain.model.WeeklyExpiryStatistic
import com.nhuhuy.algidy.toLocalDate
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

fun List<FoodItem>.toFreshnessStatistic(period: AnalyticsPeriod): FreshnessStatistic {
    val now = LocalDate.now()
    val startDate = getStartDate(now, period)

    val resolvedInPeriod = this.filter { it.isResolvedInPeriod(startDate) }

    var fresh = 0
    var urgent = 0
    var warning = 0
    var expiry = 0

    resolvedInPeriod.forEach { item ->
        val resolvedDate = requireNotNull(item.resolvedDate).toLocalDate()
        val expiryDate = item.expiryDate.toLocalDate()

        val daysRemaining = ChronoUnit.DAYS.between(resolvedDate, expiryDate)

        when {
            daysRemaining < 0 -> expiry++
            daysRemaining <= 3 -> urgent++
            daysRemaining <= 7 -> warning++
            else -> fresh++
        }
    }

    return FreshnessStatistic(
        fresh = fresh,
        urgent = urgent,
        warning = warning,
        expiry = expiry,
    )
}

fun List<FoodItem>.toSpoilageStatistic(period: AnalyticsPeriod): SpoilageStatistic {
    val now = LocalDate.now()
    val startDate = getStartDate(now, period)

    val resolvedInPeriod = this.filter { it.isResolvedInPeriod(startDate) }

    val pointsMap = resolvedInPeriod.groupBy {
        requireNotNull(it.resolvedDate).toLocalDate()
    }

    val points = mutableListOf<SpoilagePoint>()
    var currentDate = startDate

    while (!currentDate.isAfter(now)) {
        val itemsAtDate = pointsMap[currentDate].orEmpty()
        val waste = itemsAtDate.count { it.status == FoodStatus.WASTED }
        val consumed = itemsAtDate.count { it.status == FoodStatus.CONSUMED }

        points += SpoilagePoint(
            date = currentDate,
            waste = waste,
            consumed = consumed,
        )
        currentDate = currentDate.plusDays(1)
    }

    return SpoilageStatistic(
        period = period,
        points = points,
    )
}

fun List<FoodItem>.toOverviewStatistic(): ExpiryOverviewStatistic {
    val now = LocalDate.now()
    val endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    val activeFoods = this.filter { it.status == FoodStatus.ACTIVE }

    val expiryCount = activeFoods.count { item ->
        val expiryDate = item.expiryDate.toLocalDate()
        expiryDate.isBefore(now)
    }

    val expiringCount = activeFoods.count { item ->
        val expiryDate = item.expiryDate.toLocalDate()
        !expiryDate.isBefore(now) && !expiryDate.isAfter(endOfWeek)
    }

    return ExpiryOverviewStatistic(
        expiryCount = expiryCount,
        expiringCount = expiringCount,
    )
}

fun List<FoodItem>.toWeeklyExpiryStatistic(): List<WeeklyExpiryStatistic> {
    val now = LocalDate.now()
    val monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val activeFoods = filter { it.status == FoodStatus.ACTIVE }
    val expiryMap = activeFoods.groupBy { it.expiryDate.toLocalDate() }

    return (0..6).map { offset ->
        val date = monday.plusDays(offset.toLong())

        WeeklyExpiryStatistic(
            date = date,
            count = expiryMap[date].orEmpty().size,
        )
    }
}

private fun getStartDate(now: LocalDate, period: AnalyticsPeriod): LocalDate {
    return when (period) {
        AnalyticsPeriod.WEEK -> now.minusDays(6)
        AnalyticsPeriod.MONTH -> now.minusDays(29)
    }
}

private fun FoodItem.isResolvedInPeriod(startDate: LocalDate): Boolean {
    val resolvedDate = resolvedDate?.toLocalDate() ?: return false
    val isResolved = status == FoodStatus.CONSUMED || status == FoodStatus.WASTED
    return isResolved && !resolvedDate.isBefore(startDate)
}
