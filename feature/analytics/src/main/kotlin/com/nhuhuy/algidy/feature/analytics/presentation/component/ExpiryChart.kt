package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.aay.compose.baseComponents.model.LegendPosition
import com.nhuhuy.algidy.capitalize
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.ExpiryChartUiModel

@Composable
fun ExpiryChart(
    uiModel: ExpiryChartUiModel,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val barParameters = remember(uiModel, colorScheme) {
        uiModel.items.map { chartData ->
            val color = when (chartData.type) {
                Freshness.EXPIRED -> colorScheme.error
                Freshness.URGENT -> colorScheme.tertiary
                Freshness.WARNING -> colorScheme.secondary
                Freshness.FRESH -> colorScheme.primary
            }
            BarParameters(
                dataName = chartData.type.name.lowercase().capitalize(),
                data = chartData.values,
                barColor = color
            )
        }
    }

    CardLayout(
        modifier = modifier,
        icon = Icons.Rounded.BarChart,
        title = "Weekly Freshness"
    ) {
        Box(
            modifier = Modifier
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            if (uiModel.items.isNotEmpty()) {
                BarChart(
                    chartParameters = barParameters,
                    gridColor = MaterialTheme.colorScheme.outlineVariant,
                    xAxisData = uiModel.labels,
                    isShowGrid = true,
                    animateChart = true,
                    showGridWithSpacer = true,
                    yAxisStyle = TextStyle(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    xAxisStyle = TextStyle(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    ),
                    yAxisRange = 5,
                    barWidth = 6.dp,
                    barCornerRadius = 4.dp,
                    spaceBetweenBars = 2.dp,
                    spaceBetweenGroups = 16.dp,
                    legendPosition = LegendPosition.TOP
                )
            }
        }
    }
}
