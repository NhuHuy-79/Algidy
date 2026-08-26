package com.nhuhuy.algidy.feature.analytics.presentation.model

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.feature.analytics.domain.model.SpoilagePoint
import java.time.format.DateTimeFormatter
import java.util.Locale

@Immutable
data class SpoilagePointUiModel(
    val label: String,
    val waste: Int,
    val consumed: Int,
)

fun SpoilagePoint.toUiModel(): SpoilagePointUiModel {
    return SpoilagePointUiModel(
        label = date.format(DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())),
        waste = waste,
        consumed = consumed,
    )
}
