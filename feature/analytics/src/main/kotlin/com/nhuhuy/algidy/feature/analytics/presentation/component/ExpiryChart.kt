package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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


@Composable
fun ExpiryChart() {
    val xAxisData = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val testBarParameters: List<BarParameters> = listOf(
        BarParameters(
            dataName = Freshness.FRESH.name.capitalize(),
            data = listOf(0.6, 10.6, 80.0, 50.6, 44.0, 100.6, 10.0),
            barColor = MaterialTheme.colorScheme.primary
        ),
        BarParameters(
            dataName = Freshness.EXPIRED.name.capitalize(),
            data = listOf(50.0, 30.6, 77.0, 69.6, 50.0, 30.6, 80.0),
            barColor = MaterialTheme.colorScheme.error,
        ),
        BarParameters(
            dataName = Freshness.URGENT.name.capitalize(),
            data = listOf(100.0, 99.6, 60.0, 80.6, 10.0, 100.6, 55.99),
            barColor = MaterialTheme.colorScheme.tertiary,
        ),
        BarParameters(
            dataName = Freshness.WARNING.name.capitalize(),
            data = listOf(100.0, 99.6, 60.0, 80.6, 10.0, 100.6, 55.99),
            barColor = MaterialTheme.colorScheme.secondary,
        ),
    )

    CardLayout(
        icon = Icons.Rounded.BarChart,
        title = "Weekly Freshness"
    ) {
        Box(
            modifier = Modifier
                .height(400.dp),
            contentAlignment = Alignment.Center
        ) {
            BarChart(
                chartParameters = testBarParameters,
                gridColor = MaterialTheme.colorScheme.onSurfaceVariant,
                xAxisData = xAxisData,
                isShowGrid = true,
                animateChart = true,
                showGridWithSpacer = true,
                yAxisStyle = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                xAxisStyle = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Black
                ),
                yAxisRange = 4,
                barWidth = 8.dp,
                barCornerRadius = 16.dp,
                spaceBetweenBars = 2.dp,
                spaceBetweenGroups = 20.dp,
                descriptionStyle = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Black
                ),
                legendPosition = LegendPosition.TOP
            )
        }
    }
}