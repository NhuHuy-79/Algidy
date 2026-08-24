package com.nhuhuy.algidy.feature.analytics.data.repository

import com.nhuhuy.algidy.core.data.util.AppDispatchers
import com.nhuhuy.algidy.core.database.dao.FoodDao
import com.nhuhuy.algidy.core.database.entity.FoodItemEntity
import com.nhuhuy.algidy.core.model.food.FoodStatus
import com.nhuhuy.algidy.feature.analytics.domain.model.AnalyticsPeriod
import com.nhuhuy.algidy.feature.analytics.domain.model.ExpiryOverviewStatistic
import com.nhuhuy.algidy.feature.analytics.domain.model.FreshnessStatistic
import com.nhuhuy.algidy.feature.analytics.domain.model.SpoilagePoint
import com.nhuhuy.algidy.feature.analytics.domain.model.SpoilageStatistic
import com.nhuhuy.algidy.feature.analytics.domain.repository.FoodAnalyticsRepository
import com.nhuhuy.algidy.toLocalDate
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

class FoodAnalyticsRepositoryImpl(
    private val dispatchers: AppDispatchers,
    private val foodDao: FoodDao,
) : FoodAnalyticsRepository {

    override suspend fun getFreshnessStatistic(
        period: AnalyticsPeriod,
    ): FreshnessStatistic {
        return withContext(dispatchers.io) {

            val now = LocalDate.now()
            val startDate = getStartDate(now, period)

            val resolvedInPeriod = foodDao
                .getAllFoodItemEntities()
                .filter {
                    it.isResolvedInPeriod(startDate)
                }

            var fresh = 0
            var urgent = 0
            var warning = 0
            var expiry = 0

            resolvedInPeriod.forEach { item ->

                val resolvedDate =
                    requireNotNull(item.resolvedDate)
                        .toLocalDate()

                val expiryDate =
                    item.expiryDate.toLocalDate()

                val daysRemaining = ChronoUnit.DAYS.between(
                    resolvedDate,
                    expiryDate,
                )

                when {
                    daysRemaining < 0 -> expiry++
                    daysRemaining <= 3 -> urgent++
                    daysRemaining <= 7 -> warning++
                    else -> fresh++
                }
            }

            FreshnessStatistic(
                fresh = fresh,
                urgent = urgent,
                warning = warning,
                expiry = expiry,
            )
        }
    }

    override suspend fun getSpoilageStatistic(
        period: AnalyticsPeriod,
    ): SpoilageStatistic {
        return withContext(dispatchers.io) {

            val now = LocalDate.now()
            val startDate = getStartDate(now, period)

            val resolvedInPeriod = foodDao
                .getAllFoodItemEntities()
                .filter {
                    it.isResolvedInPeriod(startDate)
                }

            val pointsMap = resolvedInPeriod.groupBy { item ->
                requireNotNull(item.resolvedDate)
                    .toLocalDate()
            }

            val points = mutableListOf<SpoilagePoint>()

            var currentDate = startDate

            while (!currentDate.isAfter(now)) {

                val itemsAtDate =
                    pointsMap[currentDate].orEmpty()

                val waste = itemsAtDate.count {
                    it.status == FoodStatus.WASTED
                }

                val consumed = itemsAtDate.count {
                    it.status == FoodStatus.CONSUMED
                }

                points += SpoilagePoint(
                    date = currentDate,
                    waste = waste,
                    consumed = consumed,
                )

                currentDate = currentDate.plusDays(1)
            }

            SpoilageStatistic(
                period = period,
                points = points,
            )
        }
    }

    override suspend fun getOverviewStatistic(): ExpiryOverviewStatistic {
        return withContext(dispatchers.io) {
            val now = LocalDate.now()
            val endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

            val activeFoods = foodDao
                .getAllFoodItemEntities()
                .filter { item ->
                    item.status == FoodStatus.ACTIVE
                }

            val expiryCount = activeFoods.count { item ->
                val expiryDate = item.expiryDate.toLocalDate()
                expiryDate.isBefore(now)
            }

            val expiringCount = activeFoods.count { item ->
                val expiryDate = item.expiryDate.toLocalDate()

                !expiryDate.isBefore(now) &&
                        !expiryDate.isAfter(endOfWeek)
            }

            ExpiryOverviewStatistic(
                expiryCount = expiryCount,
                expiringCount = expiringCount,
            )
        }
    }

    private fun getStartDate(
        now: LocalDate,
        period: AnalyticsPeriod,
    ): LocalDate {
        return when (period) {
            AnalyticsPeriod.WEEK ->
                now.minusDays(6)

            AnalyticsPeriod.MONTH ->
                now.minusDays(29)
        }
    }

    private fun FoodItemEntity.isResolvedInPeriod(
        startDate: LocalDate,
    ): Boolean {

        val resolvedDate =
            resolvedDate?.toLocalDate()
                ?: return false

        val isResolved =
            status == FoodStatus.CONSUMED ||
                    status == FoodStatus.WASTED

        return isResolved &&
                !resolvedDate.isBefore(startDate)
    }
}