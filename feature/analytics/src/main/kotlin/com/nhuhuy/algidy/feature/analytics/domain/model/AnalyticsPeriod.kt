package com.nhuhuy.algidy.feature.analytics.domain.model

import androidx.annotation.StringRes
import com.nhuhuy.algidy.core.presentation.R

enum class AnalyticsPeriod {
    WEEK, MONTH
}

@StringRes
fun AnalyticsPeriod.getId(): Int {
    return when (this) {
        AnalyticsPeriod.WEEK -> R.string.analytics_period_week
        AnalyticsPeriod.MONTH -> R.string.analytics_period_month
    }
}

