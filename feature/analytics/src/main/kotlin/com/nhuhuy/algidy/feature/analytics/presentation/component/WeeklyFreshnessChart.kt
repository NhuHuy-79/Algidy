package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import com.nhuhuy.algidy.core.model.food.Freshness
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

@Composable
fun WeeklyFreshnessChart(
    uiModel: ExpiryChartUiModel
) {
    var selectedFilter: Freshness by remember { mutableStateOf(Freshness.FRESH) }
    val chartData = uiModel.toBarData(selectedFilter)
    if (chartData.isNotEmpty()) {
        CardLayout(
            icon = Icons.Rounded.BarChart,
            title = "Weekly Freshness"
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Freshness.entries.forEach { freshness ->
                    if (selectedFilter != freshness) {
                        FilterChip(
                            selected = false,
                            onClick = {
                                selectedFilter =
                                    if (selectedFilter == freshness) Freshness.FRESH else freshness
                            },
                            label = {
                                Text(
                                    text = freshness.name.capitalize(),
                                    maxLines = 1
                                )
                            },
                            shape = MaterialTheme.shapes.medium
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
            Freshness.FRESH -> colorScheme.primary
            Freshness.WARNING -> colorScheme.tertiary
            Freshness.URGENT -> colorScheme.secondary
            Freshness.EXPIRED -> colorScheme.error
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