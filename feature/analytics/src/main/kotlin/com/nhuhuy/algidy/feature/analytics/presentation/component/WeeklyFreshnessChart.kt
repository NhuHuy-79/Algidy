package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Dangerous
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.EnergySavingsLeaf
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.model.food.Freshness.EXPIRED
import com.nhuhuy.algidy.core.model.food.Freshness.FRESH
import com.nhuhuy.algidy.core.model.food.Freshness.URGENT
import com.nhuhuy.algidy.core.model.food.Freshness.WARNING
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
    modifier: Modifier = Modifier
) {
    var selectedFilter: Freshness by remember { mutableStateOf(FRESH) }
    val chartData = uiModel.toBarData(selectedFilter)
    if (chartData.isNotEmpty()) {
        CardLayout(
            modifier = modifier,
            icon = Icons.Rounded.BarChart,
            title = "Weekly Freshness"
        ) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Freshness.entries.forEachIndexed { index, level ->
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                            TooltipAnchorPosition.Above
                        ),
                        tooltip = {
                            PlainTooltip {
                                Text(
                                    text = level.name.capitalize()
                                )
                            }
                        },
                        state = rememberTooltipState()
                    ) {
                        SegmentedButton(
                            colors = SegmentedButtonDefaults.colors(
                                activeContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                disabledActiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = Freshness.entries.size
                            ),
                            icon = {
                                SegmentedButtonDefaults.Icon(active = selectedFilter == level)
                            },
                            onClick = {
                                selectedFilter = level
                            },
                            selected = selectedFilter == level,
                            label = {
                                Icon(
                                    imageVector = level.toImageVector(),
                                    contentDescription = level.name,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                        )
                    }
                }
            }

            ColumnChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f),
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
    val colorScheme = MaterialTheme.colorScheme

    fun getFreshnessBrush(type: Freshness): Brush {
        val color = when (type) {
            FRESH -> colorScheme.primary
            WARNING -> colorScheme.tertiary
            URGENT -> colorScheme.secondary
            EXPIRED -> colorScheme.error
        }
        return SolidColor(color)
    }

    return labels.indices.map { index ->
        Bars(
            label = labels[index],
            values = items
                .filter { it.type == filter } // Lọc dữ liệu tại đây
                .map { freshnessData ->
                    Bars.Data(
                        label = freshnessData.type.name,
                        value = freshnessData.values.getOrElse(index) { 0.0 },
                        color = getFreshnessBrush(freshnessData.type)
                    )
                }
        )
    }
}

fun Freshness.toImageVector(): ImageVector {
    return when (this) {
        EXPIRED -> Icons.Rounded.DeleteForever
        URGENT -> Icons.Rounded.Warning
        WARNING -> Icons.Rounded.Dangerous
        FRESH -> Icons.Rounded.EnergySavingsLeaf
    }
}