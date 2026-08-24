package com.nhuhuy.algidy.feature.analytics.domain.model

import com.nhuhuy.algidy.core.model.food.Freshness

data class FreshnessStatistic(
    val fresh: Int = 0,
    val urgent: Int = 0,
    val warning: Int = 0,
    val expiry: Int = 0
) {
    val sum = fresh + urgent + warning + expiry
    val freshPercent = safePercent(fresh.toFloat(), sum)
    val urgentPercent = safePercent(urgent.toFloat(), sum)
    val warningPercent = safePercent(warning.toFloat(), sum)
    val expiryPercent = safePercent(expiry.toFloat(), sum)
}

private fun safePercent(value: Float, sum: Int): Float {
    return if (sum == 0) 0f else value / sum
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
