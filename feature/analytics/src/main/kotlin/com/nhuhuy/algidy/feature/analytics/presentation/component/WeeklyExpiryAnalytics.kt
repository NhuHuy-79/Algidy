package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape
import com.nhuhuy.algidy.feature.analytics.presentation.model.WeeklyExpiryStatisticUiModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun WeeklyExpiryAnalytics(
    itemPosition: ItemPosition,
    weeklyExpiryStatistics: ImmutableList<WeeklyExpiryStatisticUiModel>,
    modifier: Modifier = Modifier,
) {
    LocalAlgidySpacing.current
    CardLayout(
        icon = AlgidyIcons.Analytics.WeeklyFoodChart.toImageVector(),
        title = stringResource(R.string.analytics_card_expiring_this_week),
        modifier = modifier,
        cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = itemPosition.toVerticalSegmentedShape()
    ) {
        val allFoodCount = weeklyExpiryStatistics.sumOf { it.count }
        Text(
            text = pluralStringResource(
                R.plurals.analytics_foods_expiring_this_week,
                allFoodCount,
                allFoodCount
            ),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (allFoodCount == 0) {
            NoExpiryFoodContent(modifier = Modifier.weight(1f))
        } else {
            WeeklyExpiryAnalyticsContent(
                weeklyExpiryStatistics = weeklyExpiryStatistics,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun NoExpiryFoodContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.analytics_no_food_expiring_this_week),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun WeeklyExpiryAnalyticsContent(
    weeklyExpiryStatistics: ImmutableList<WeeklyExpiryStatisticUiModel>,
    modifier: Modifier = Modifier,
) {
    val highestCount = weeklyExpiryStatistics.maxOfOrNull { it.count } ?: 0
    val highestItem = weeklyExpiryStatistics.maxByOrNull { it.count }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        weeklyExpiryStatistics.forEach { statistic ->
            val fraction = if (highestCount > 0) {
                statistic.count.toFloat() / highestCount
            } else {
                0f
            }

            WeeklyExpiryChartItem(
                modifier = Modifier.weight(1f),
                value = statistic.count,
                label = statistic.label,
                fraction = fraction,
                isPrimary = statistic == highestItem,
            )
        }
    }
}

@Composable
private fun WeeklyExpiryChartItem(
    modifier: Modifier = Modifier,
    value: Int,
    label: String,
    fraction: Float,
    isPrimary: Boolean,
) {
    val safePercent = fraction.coerceIn(0.2f..1f)
    val contentColor =
        if (isPrimary) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurface
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (fraction > 0f) {
            ChartBar(
                modifier = Modifier.weight(safePercent),
                value = value,
                isPrimary = isPrimary,
            )
        }

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = contentColor
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ChartBar(
    modifier: Modifier = Modifier,
    value: Int,
    isPrimary: Boolean,
) {
    val scheme = MaterialTheme.colorScheme
    val containerColor = if (isPrimary) scheme.primary else scheme.secondary
    val contentColor = if (isPrimary) scheme.primaryContainer else scheme.secondaryContainer
    val contentShape = if (isPrimary) MaterialShapes.VerySunny.toShape() else CircleShape
    val localShape = LocalAlgidyShapes.current

    Column(
        modifier = modifier
            .background(color = containerColor, shape = localShape.extraLarge)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(color = contentColor, shape = contentShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$value",
                style = MaterialTheme.typography.labelMedium,
                color = containerColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}