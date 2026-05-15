package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.StackedLineChart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties

@Composable
fun SpoilageHistoryChart(
    modifier: Modifier = Modifier,
) {
    val scheme = androidx.compose.material3.MaterialTheme.colorScheme
    CardLayout(
        modifier = modifier,
        title = "Spoilage History",
        icon = Icons.Rounded.StackedLineChart
    ) {
        LineChart(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1.5f),
            data = listOf(
                Line(
                    label = "Wasted",
                    values = listOf(5.0, 6.0, 7.0, 8.0, 9.0, 10.0),
                    color = SolidColor(scheme.onErrorContainer),
                    firstGradientFillColor = scheme.onErrorContainer.copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = DrawStyle.Stroke(width = 2.dp),
                ),
                Line(
                    label = "Consumed",
                    values = listOf(8.0, 6.0, 3.0, 4.0, 5.5, 2.0),
                    color = SolidColor(scheme.onPrimaryContainer),
                    firstGradientFillColor = scheme.onPrimaryContainer.copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = DrawStyle.Stroke(width = 2.dp),
                )
            ),

            indicatorProperties = HorizontalIndicatorProperties(
                enabled = true,
                padding = 8.dp,
                textStyle = androidx.compose.material3.MaterialTheme.typography.labelMedium.copy(
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
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
                textStyle = androidx.compose.material3.MaterialTheme.typography.labelMedium.copy(
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            ),
            labelProperties = LabelProperties(
                rotation = LabelProperties.Rotation(degree = 0f),
                labels = listOf("WEEK 1", "WEEK 2", "WEEK 3", "WEEK 4"),
                enabled = true,
                textStyle = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                ),
            ),
            animationMode = AnimationMode.Together()
        )
    }
}