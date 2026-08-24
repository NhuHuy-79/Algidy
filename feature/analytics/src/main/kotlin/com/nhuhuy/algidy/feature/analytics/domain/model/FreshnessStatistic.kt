package com.nhuhuy.algidy.feature.analytics.domain.model

import com.nhuhuy.algidy.core.model.food.Freshness

data class FreshnessStatistic(
    val fresh: Int,
    val urgent: Int,
    val warning: Int,
    val expiry: Int
) {
    val sum = fresh + urgent + warning + expiry
    val freshPercent = fresh.toFloat() / sum
    val urgentPercent = urgent.toFloat() / sum
    val warningPercent = warning.toFloat() / sum
    val expiryPercent = expiry.toFloat() / sum
}

val fakeFreshnessStatistic = FreshnessStatistic(
    fresh = 12,
    urgent = 4,
    warning = 8,
    expiry = 4
)

fun Freshness.getStatistic(statistic: FreshnessStatistic): Pair<Int, Float> {
    return when (this) {
        Freshness.FRESH -> statistic.fresh to statistic.freshPercent
        Freshness.URGENT -> statistic.urgent to statistic.urgentPercent
        Freshness.WARNING -> statistic.warning to statistic.warningPercent
        Freshness.EXPIRED -> statistic.expiry to statistic.expiryPercent
    }
}
