package com.nhuhuy.algidy.feature.analytics.presentation.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.AppFilterButton
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.toBackgroundColor
import com.nhuhuy.algidy.core.presentation.utils.toContentColor
import com.nhuhuy.algidy.core.presentation.utils.toContentContainerColor
import com.nhuhuy.algidy.core.presentation.utils.toStringRes
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.ExpiryChartUiModel
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.LineProperties

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WeeklyFreshnessChart(
    uiModel: ExpiryChartUiModel,
    modifier: Modifier = Modifier,
    selectedFreshness: Freshness,
    onSelectFreshness: (freshness: Freshness) -> Unit
) {
    val chartData = uiModel.toBarData(selectedFreshness)
    if (chartData.isNotEmpty()) {
        CardLayout(
            modifier = modifier,
            icon = AlgidyIcons.Analytics.WeeklyChart.toImageVector(),
            title = stringResource(R.string.analytics_card_weekly_freshness)
        ) {
            FreshnessCategoryRow(
                modifier = Modifier,
                selectedFreshness = selectedFreshness,
                onSelectFreshness = onSelectFreshness
            )

            ColumnChart(
                modifier = Modifier
                    .fillMaxSize(),
                data = chartData,
                barProperties = BarProperties(
                    cornerRadius = Bars.Data.Radius.Rectangle(topRight = 24.dp, topLeft = 24.dp),
                    spacing = 2.dp,
                    thickness = 24.dp
                ),
                gridProperties = GridProperties(
                    enabled = false
                ),
                animationMode = AnimationMode.OneByOne,
                indicatorProperties = HorizontalIndicatorProperties(
                    enabled = true,
                    padding = 8.dp,
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        color = selectedFreshness.toContentContainerColor()
                    )
                ),
                dividerProperties = DividerProperties(
                    enabled = true,
                    yAxisProperties = LineProperties(
                        enabled = true,
                    ),
                    xAxisProperties = LineProperties(
                        enabled = true,
                    )
                ),
                labelHelperProperties = LabelHelperProperties(
                    enabled = false,
                    labelCountPerLine = 2,
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        color = selectedFreshness.toContentContainerColor()
                    )
                ),
                labelProperties = LabelProperties(
                    rotation = LabelProperties.Rotation(degree = 0f),
                    enabled = true,
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        color = selectedFreshness.toContentContainerColor()
                    ),
                ),
                animationSpec = tween(300, easing = FastOutSlowInEasing),
            )
        }
    }
}

@Composable
private fun ExpiryChartUiModel.toBarData(filter: Freshness): List<Bars> {
    val resource = LocalResources.current
    val color = filter.toBackgroundColor()
    val solid = SolidColor(color)

    return labels.indices.map { index ->
        Bars(
            label = labels[index],
            values = items
                .filter { it.type == filter }
                .map { freshnessData ->
                    Bars.Data(
                        label = resource.getString(freshnessData.type.toStringRes()),
                        value = freshnessData.values.getOrElse(index) { 0.0 },
                        color = solid
                    )
                }
        )
    }
}

@Composable
private fun FreshnessCategoryRow(
    modifier: Modifier = Modifier,
    selectedFreshness: Freshness,
    onSelectFreshness: (freshness: Freshness) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = Freshness.entries,
            key = { it.name }
        ) { freshness ->
            AppFilterButton(
                selected = selectedFreshness == freshness,
                label = stringResource(freshness.toStringRes()),
                activeContainerColor = freshness.toBackgroundColor(),
                activeContentColor = freshness.toContentColor(),
                onClick = {
                    onSelectFreshness(freshness)
                }
            )
        }
    }
}


