package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.toBackgroundColor
import com.nhuhuy.algidy.core.presentation.utils.toContentColor
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.ExpiryChartUiModel
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.LineProperties

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WeeklyFreshnessChart(
    uiModel: ExpiryChartUiModel,
    modifier: Modifier = Modifier,
    selectedFreshness: Freshness
) {
    val chartData = uiModel.toBarData(selectedFreshness)
    if (chartData.isNotEmpty()) {
        CardLayout(
            modifier = modifier,
            icon = Icons.Rounded.BarChart,
            title = stringResource(R.string.analytics_card_weekly_freshness)
        ) {
            ColumnChart(
                modifier = Modifier
                    .fillMaxSize(),
                data = chartData,
                barProperties = BarProperties(
                    cornerRadius = Bars.Data.Radius.Rectangle(topRight = 24.dp, topLeft = 24.dp),
                    spacing = 2.dp,
                    thickness = 24.dp
                ),
                animationMode = AnimationMode.OneByOne,
                indicatorProperties = HorizontalIndicatorProperties(
                    enabled = true,
                    padding = 8.dp,
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ),
                dividerProperties = DividerProperties(
                    enabled = true,
                    xAxisProperties = LineProperties(
                        enabled = true,
                    )
                ),
                labelHelperProperties = LabelHelperProperties(
                    labelCountPerLine = 2,
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ),
                labelProperties = LabelProperties(
                    rotation = LabelProperties.Rotation(degree = 0f),
                    enabled = true,
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                ),
                animationSpec = tween(300, easing = FastOutSlowInEasing),
            )
        }
    }
}

@Composable
fun ExpiryChartUiModel.toBarData(filter: Freshness): List<Bars> {
    val color = filter.toBackgroundColor()
    val solid = SolidColor(color)

    return labels.indices.map { index ->
        Bars(
            label = labels[index],
            values = items
                .filter { it.type == filter }
                .map { freshnessData ->
                    Bars.Data(
                        label = freshnessData.type.name,
                        value = freshnessData.values.getOrElse(index) { 0.0 },
                        color = solid
                    )
                }
        )
    }
}

@Composable
fun FreshnessSegmentedButton(
    modifier: Modifier = Modifier,
    selectedFreshness: Freshness,
    onSelectFreshness: (freshness: Freshness) -> Unit,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Freshness.entries.forEachIndexed { index, freshness ->
            SegmentedButton(
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = freshness.toBackgroundColor(),
                    activeContentColor = freshness.toContentColor(),
                    disabledActiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = Freshness.entries.size
                ),
                icon = {
                    SegmentedButtonDefaults.Icon(active = selectedFreshness == freshness)
                },
                onClick = {
                    onSelectFreshness(freshness)
                },
                selected = selectedFreshness == freshness,
                label = {
                    Text(
                        text = freshness.name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                },
            )
        }
    }
}
