package com.nhuhuy.algidy.feature.analytics.presentation.model

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.feature.analytics.domain.model.WeeklyExpiryStatistic
import java.time.format.DateTimeFormatter
import java.util.Locale

@Immutable
data class WeeklyExpiryStatisticUiModel(
    val label: String,
    val count: Int
)

fun List<WeeklyExpiryStatistic>.toUiModel(): List<WeeklyExpiryStatisticUiModel> {
    return this.map { it.toUiModel() }
}

fun WeeklyExpiryStatistic.toUiModel(): WeeklyExpiryStatisticUiModel {
    return WeeklyExpiryStatisticUiModel(
        label = date.format(DateTimeFormatter.ofPattern("EEE", Locale.getDefault())),
        count = this.count
    )
}


