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
import com.nhuhuy.algidy.core.presentation.utils.toContentContainerColor
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape
import com.nhuhuy.algidy.feature.analytics.domain.model.FreshnessStatistic
import com.nhuhuy.algidy.feature.analytics.domain.model.getStatistic

@Composable
fun WeeklyFreshness(
    freshnessStatistic: FreshnessStatistic,
    modifier: Modifier = Modifier,
    itemPosition: ItemPosition = ItemPosition.SINGLE,
) {
    CardLayout(
        modifier = modifier,
        icon = AlgidyIcons.Analytics.WeeklyChart.toImageVector(),
        title = stringResource(R.string.analytics_card_weekly_freshness),
        shape = itemPosition.toVerticalSegmentedShape(),
        cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        WeeklyFreshnessContent(
            modifier = Modifier.weight(1f),
            freshnessStatistic = freshnessStatistic
        )
    }
}

@Composable
private fun WeeklyFreshnessContent(
    modifier: Modifier = Modifier,
    freshnessStatistic: FreshnessStatistic,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Freshness.entries.forEach { freshness ->
            val (count, fraction) = freshness.getStatistic(freshnessStatistic)
            WeeklyFreshnessItem(
                label = stringResource(freshness.toStringRes()),
                value = count,
                fraction = fraction,
                barColor = freshness.toBackgroundColor(),
                labelSurfaceColor = freshness.toBackgroundContainerColor(),
                labelColor = freshness.toContentContainerColor()
            )
        }
    }
}

@Composable
private fun WeeklyFreshnessItem(
    label: String,
    modifier: Modifier = Modifier,
    value: Int,
    fraction: Float,
    barColor: Color = MaterialTheme.colorScheme.primary,
    labelSurfaceColor: Color = MaterialTheme.colorScheme.primaryContainer,
    labelColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            modifier = Modifier
                .width(88.dp)
                .basicMarquee(),
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )

        Box(
            modifier = Modifier.weight(1f),
        ) {
            WeeklyFreshnessChartBar(
                modifier = Modifier.fillMaxWidth(fraction.coerceIn(0.2f, 1f)),
                label = value.toString(),
                barColor = barColor,
                labelSurfaceColor = labelSurfaceColor,
                labelColor = labelColor,
            )
        }
    }
}

@Composable
private fun WeeklyFreshnessChartBar(
    label: String,
    modifier: Modifier = Modifier,
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
            .background(
                color = barColor,
                shape = shapes.extraLarge,
            )
            .padding(horizontal = spacing.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {

        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = labelSurfaceColor,
                    shape = MaterialShapes.VerySunny.toShape(),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = labelColor,
                fontWeight = FontWeight.Black
            )
        }
    }
}

