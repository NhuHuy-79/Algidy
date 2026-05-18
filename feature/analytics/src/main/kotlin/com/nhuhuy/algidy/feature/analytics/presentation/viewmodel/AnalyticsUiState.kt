package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState
import com.nhuhuy.algidy.feature.analytics.domain.model.DailyFreshnessStats

@Immutable
data class AnalyticsUiState(
    val weeklyFoodItemsCount: Int = 0,
    val wastedCount: Int = 0,
    val consumedCount: Int = 0,
    val otherCount: Int = 0,
    val expiryChartUiModel: ExpiryChartUiModel = ExpiryChartUiModel(),
    val spoilageChartUiModel: SpoilageChartUiModel = SpoilageChartUiModel(),
    val wastedByCategory: List<CategoryWasteUiModel> = emptyList(),
    val isLoading: Boolean = false,
) : UiState

@Immutable
data class SpoilageChartUiModel(
    val wastedValues: List<Double> = emptyList(),
    val consumedValues: List<Double> = emptyList(),
    val labels: List<String> = emptyList()
)

@Immutable
data class CategoryWasteUiModel(
    val location: StorageLocation,
    val label: String,
    val percentage: Float
)

@Immutable
data class FreshnessChartData(
    val type: Freshness,
    val values: List<Double>
)


@Immutable
data class ExpiryChartUiModel(
    val items: List<FreshnessChartData> = emptyList(),
    val labels: List<String> = emptyList()
)

fun List<DailyFreshnessStats>.toExpiryChartUiModel(): ExpiryChartUiModel {
    val labels = this.map { it.date.dayOfWeek.name.take(3) }

    val items = listOf(
        FreshnessChartData(Freshness.FRESH, this.map { it.freshCount }),
        FreshnessChartData(Freshness.EXPIRED, this.map { it.expiredCount }),
        FreshnessChartData(Freshness.URGENT, this.map { it.urgentCount }),
        FreshnessChartData(Freshness.WARNING, this.map { it.warningCount })
    )

    return ExpiryChartUiModel(items = items, labels = labels)
}

fun List<DailyFreshnessStats>.toSpoilageChartUiModel(): SpoilageChartUiModel {
    return SpoilageChartUiModel(
        wastedValues = this.map { it.expiredCount },
        consumedValues = this.map { it.freshCount },
        labels = this.map { it.date.dayOfWeek.name.take(3) }
    )
}
