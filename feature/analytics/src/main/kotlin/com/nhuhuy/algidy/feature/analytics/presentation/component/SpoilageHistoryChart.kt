package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.SpoilageChartUiModel
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
    uiModel: SpoilageChartUiModel = SpoilageChartUiModel()
) {
    CardLayout(
        modifier = modifier,
        title = stringResource(com.nhuhuy.algidy.core.presentation.R.string.analytics_card_spoilage_history),
        icon = AlgidyIcons.Analytics.SpoilageChart.toImageVector()
    ) {
        LineChart(
            modifier = Modifier
                .fillMaxSize(),
            data = listOf(
                Line(
                    label = stringResource(com.nhuhuy.algidy.core.presentation.R.string.analytics_card_wasted),
                    values = uiModel.wastedValues,
                    color = SolidColor(AlgidyTheme.extendedColors.wasted),
                    firstGradientFillColor = AlgidyTheme.extendedColors.wasted.copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = DrawStyle.Stroke(width = 2.dp),
                ),
                Line(
                    label = stringResource(com.nhuhuy.algidy.core.presentation.R.string.analytics_card_consumed),
                    values = uiModel.consumedValues,
                    color = SolidColor(AlgidyTheme.extendedColors.consumed),
                    firstGradientFillColor = AlgidyTheme.extendedColors.consumed.copy(alpha = .5f),
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
                labels = uiModel.labels,
                enabled = true,
                textStyle = androidx.compose.material3.MaterialTheme.typography.labelSmall.copy(
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                ),
            ),
            animationMode = AnimationMode.OneByOne,
        )
    }
}
