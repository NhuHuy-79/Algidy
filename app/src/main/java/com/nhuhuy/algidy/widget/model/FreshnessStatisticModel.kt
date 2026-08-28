package com.nhuhuy.algidy.widget.model

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.Freshness

@Immutable
data class FreshnessStatisticModel(
    val freshCount: Int = 0,
    val urgentCount: Int = 0,
    val noticeCount: Int = 0,
    val expiredCount: Int = 0,
) {
    val sum
        get() = maxOf(
            freshCount,
            urgentCount,
            noticeCount,
            expiredCount
        )
    val freshPercent get() = freshCount.safePercent(sum)
    val urgentPercent get() = urgentCount.safePercent(sum)
    val noticePercent get() = noticeCount.safePercent(sum)
    val expiredPercent get() = expiredCount.safePercent(sum)
}

fun List<FoodItem>.toFreshnessStats(): FreshnessStatisticModel {
    val counts = groupingBy { it.getFreshnessStatus() }
        .eachCount()
    return FreshnessStatisticModel(
        freshCount = counts[Freshness.FRESH] ?: 0,
        urgentCount = counts[Freshness.URGENT] ?: 0,
        noticeCount = counts[Freshness.WARNING] ?: 0,
        expiredCount = counts[Freshness.EXPIRED] ?: 0,
    )
}

val fakeFreshnessStats = FreshnessStatisticModel(
    freshCount = 10,
    urgentCount = 8,
    noticeCount = 4,
    expiredCount = 0
)

fun Number.safePercent(total: Number): Float {
    val totalValue = total.toDouble()

    if (totalValue == 0.0) return 0f

    return (toDouble() / totalValue).toFloat()
}