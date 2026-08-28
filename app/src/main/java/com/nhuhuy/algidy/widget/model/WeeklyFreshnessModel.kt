package com.nhuhuy.algidy.widget.model

import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.model.food.Freshness

data class WeeklyFreshnessModel(
    val freshnessWithCount: Map<Freshness, Int> = emptyMap(),
)

fun List<FoodItem>.toWeeklyFreshnessModel() = WeeklyFreshnessModel(
    freshnessWithCount = this.groupBy { it.getFreshnessStatus() }.mapValues { it.value.size }
)

val fakeFreshness = WeeklyFreshnessModel(
    freshnessWithCount = mapOf(
        Freshness.EXPIRED to 2,
        Freshness.URGENT to 4,
        Freshness.WARNING to 8,
        Freshness.FRESH to 12,
    )
)