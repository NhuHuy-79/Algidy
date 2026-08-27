@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidyShapes
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toBackgroundColor
import com.nhuhuy.algidy.core.presentation.utils.toBackgroundContainerColor
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape
import com.nhuhuy.algidy.feature.analytics.domain.model.FreshnessStatistic
import com.nhuhuy.algidy.feature.analytics.domain.model.getStatistic

@Composable
fun MonthlyFreshness(
    statisticByMonth: FreshnessStatistic,
    modifier: Modifier = Modifier,
    itemPosition: ItemPosition = ItemPosition.SINGLE,
) {
    CardLayout(
        modifier = modifier.wrapContentHeight(),
        icon = AlgidyIcons.Analytics.MonthlyFreshness.toImageVector(),
        title = stringResource(R.string.analytics_card_monthly_freshness),
        shape = itemPosition.toVerticalSegmentedShape(),
        cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(
            text = pluralStringResource(
                R.plurals.analytics_urgent_foods_need_attention,
                statisticByMonth.urgent,
                statisticByMonth.urgent
            ),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        FreshnessContent(
            modifier = Modifier.weight(1f),
            monthStatistic = statisticByMonth
        )
    }
}

@Composable
private fun FreshnessContent(
    modifier: Modifier = Modifier,
    monthStatistic: FreshnessStatistic,
) {
    val statistics = Freshness.entries.map { freshness ->
        freshness to freshness.getStatistic(monthStatistic)
    }

    val maxCount = statistics
        .maxByOrNull { it.second.first }?.second?.first ?: 0

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        statistics.forEach { (freshness, statistic) ->
            val (count, _) = statistic
            val normalizedFraction = if (maxCount > 0) {
                count.toFloat() / maxCount
            } else 0f

            FreshnessItem(
                modifier = Modifier,
                label = stringResource(freshness.toStringRes()),
                monthValue = count,
                isPrimary = freshness == Freshness.WARNING,
                monthFraction = normalizedFraction,
                barColor = freshness.toBackgroundColor(),
                labelSurfaceColor = freshness.toBackgroundContainerColor(),
                labelColor = freshness.toBackgroundColor(),
            )
        }
    }
}

@Composable
private fun FreshnessItem(
    label: String,
    isPrimary: Boolean,
    modifier: Modifier = Modifier,
    monthValue: Int,
    monthFraction: Float,
    barColor: Color = MaterialTheme.colorScheme.primary,
    labelSurfaceColor: Color = MaterialTheme.colorScheme.primaryContainer,
    labelColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            modifier = Modifier
                .width(88.dp)
                .basicMarquee(),
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = barColor
        )

        if (monthFraction > 0) {
            Box(
                modifier = Modifier.weight(1f),
            ) {
                FreshnessChartBar(
                    modifier = Modifier.fillMaxWidth(monthFraction),
                    isPrimary = isPrimary,
                    label = "$monthValue",
                    barColor = barColor,
                    labelSurfaceColor = labelSurfaceColor,
                    labelColor = labelColor,
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color = barColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$monthValue",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = labelSurfaceColor
                )
            }
        }
    }
}

@Composable
private fun FreshnessChartBar(
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true,
    label: String,
    barColor: Color = MaterialTheme.colorScheme.primary,
    labelSurfaceColor: Color = MaterialTheme.colorScheme.primaryContainer,
    labelColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    val shapes = LocalAlgidyShapes.current
    val spacing = LocalAlgidySpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(color = barColor, shape = shapes.extraLarge)
            .padding(horizontal = spacing.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        val shape = if (isPrimary) MaterialShapes.VerySunny.toShape() else CircleShape
        val containerColor = if (isPrimary) labelSurfaceColor else barColor
        val contentColor = if (isPrimary) labelColor else labelSurfaceColor
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(color = containerColor, shape = shape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

