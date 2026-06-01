package com.nhuhuy.algidy.feature.analytics.presentation.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.StorageLocation
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState
import com.nhuhuy.algidy.feature.analytics.domain.model.CategoryWasteStats
import com.nhuhuy.algidy.feature.analytics.domain.model.DailyFreshnessStats
import com.nhuhuy.algidy.feature.analytics.domain.model.SpoilageHistory

@Immutable
data class AnalyticsUiState(
    val weeklyFoodItemsCount: Int = 10,
    val wastedCount: Int = 5,
    val consumedCount: Int = 5,
    val othersCount: Int = 0,
    val expiryChartUiModel: ExpiryChartUiModel = ExpiryChartUiModel(),
    val spoilageChartUiModel: SpoilageChartUiModel = SpoilageChartUiModel(),
    val wastedByCategory: List<CategoryWasteUiModel> = emptyList(),
    val isLoading: Boolean = false,

    //Ver1
    val circularChartData: CircularChartData = CircularChartData.CONSUMED,
) : UiState {

    val wastedPercent: Float
        get() = if (weeklyFoodItemsCount == 0) 0f else wastedCount.toFloat() / weeklyFoodItemsCount
    val consumedPercent: Float
        get() = if (weeklyFoodItemsCount == 0) 0f else consumedCount.toFloat() / weeklyFoodItemsCount
    val othersPercent: Float
        get() = if (weeklyFoodItemsCount == 0) 0f else othersCount.toFloat() / weeklyFoodItemsCount

    fun getCountByCircularChart(): Int {
        return when (circularChartData) {
            CircularChartData.CONSUMED -> consumedCount
            CircularChartData.WASTED -> wastedCount
            CircularChartData.OTHERS -> othersCount
        }
    }

    fun getFloatByCircularChart(): Float {
        return when (circularChartData) {
            CircularChartData.OTHERS -> othersPercent
            CircularChartData.CONSUMED -> consumedPercent
            CircularChartData.WASTED -> wastedPercent
        }
    }

}

@Immutable
data class ActionAnalyticsState(
    val currentChartData: CircularChartData = CircularChartData.CONSUMED
)

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
    val items: List<FreshnessChartData> = Freshness.entries.map { freshness ->
        FreshnessChartData(type = freshness, values = emptyList())
    },
    val labels: List<String> = emptyList()
)

fun CategoryWasteStats.toCategoryWastedUiModel() : CategoryWasteUiModel {
    return CategoryWasteUiModel(
        location = this.location,
        label = this.location.name.capitalize(),
        percentage = this.percentage
    )
}

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

fun SpoilageHistory.toSpoilageChartUiModel() : SpoilageChartUiModel {
    return SpoilageChartUiModel(
        wastedValues = this.wastedValues,
        consumedValues = this.consumedValues,
        labels = this.labels
    )
}

enum class CircularChartData {
    CONSUMED, WASTED, OTHERS;
}
