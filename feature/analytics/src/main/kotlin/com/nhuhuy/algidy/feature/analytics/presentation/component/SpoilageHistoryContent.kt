package com.nhuhuy.algidy.feature.analytics.presentation.component

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.component.CardLayout
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toVerticalSegmentedShape
import com.nhuhuy.algidy.feature.analytics.domain.model.fakeWeeklySpoilageStatistic
import com.nhuhuy.algidy.feature.analytics.presentation.model.SpoilagePointUiModel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineModel
import com.patrykandpatrick.vico.compose.cartesian.layer.CartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.component.ShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SpoilageHistory(
    modifier: Modifier = Modifier,
    consumedValue: Int = 0,
    wastedValue: Int = 0,
    itemPosition: ItemPosition = ItemPosition.SINGLE,
    statisticByMonth: ImmutableList<SpoilagePointUiModel>,
    onLineSpotPressed: (consumedAndWasted: Pair<Int, Int>) -> Unit,
    onLineSpotHide: () -> Unit
) {
    val extendColor = AlgidyTheme.extendedColors
    val totalFoods = consumedValue + wastedValue
    CardLayout(
        modifier = modifier,
        icon = AlgidyIcons.Analytics.SpoilageChart.toImageVector(),
        title = stringResource(R.string.analytics_card_spoilage_history),
        shape = itemPosition.toVerticalSegmentedShape(),
        cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(
            text = pluralStringResource(
                R.plurals.analytics_total_foods_handled,
                totalFoods,
                totalFoods
            ),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            LegendLabel(
                color = extendColor.consumed,
                legend = stringResource(R.string.analytics_card_consumed)
            )

            Spacer(modifier = Modifier.width(16.dp))

            LegendLabel(
                color = extendColor.wasted,
                legend = stringResource(R.string.analytics_card_wasted)
            )
        }

        SpoilageHistoryContainer(
            modifier = Modifier.weight(1f),
            spoilageStatistic = statisticByMonth,
            onValueHide = onLineSpotHide,
            onValueChange = onLineSpotPressed
        )
    }
}

@Composable
private fun LegendLabel(
    modifier: Modifier = Modifier,
    color: Color,
    legend: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = color, shape = CircleShape)
        )

        Text(
            text = legend,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun SpoilageHistoryContainer(
    modifier: Modifier = Modifier,
    spoilageStatistic: ImmutableList<SpoilagePointUiModel>,
    onValueChange: (Pair<Int, Int>) -> Unit = {},
    onValueHide: () -> Unit = {}
) {
    if (spoilageStatistic.isEmpty()) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.analytics_card_empty_state),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    } else {
        SpoilageHistoryContent(
            modifier = modifier,
            spoilageStatistic = spoilageStatistic,
            onValueChange = onValueChange,
            onValueHide = onValueHide
        )
    }
}

@Composable
fun SpoilageHistoryContent(
    modifier: Modifier = Modifier,
    spoilageStatistic: ImmutableList<SpoilagePointUiModel> = fakeWeeklySpoilageStatistic.toImmutableList(),
    onValueChange: (Pair<Int, Int>) -> Unit,
    onValueHide: () -> Unit
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val extendColor = AlgidyTheme.extendedColors

    val bottomAxisValueFormatter = CartesianValueFormatter { _, value, _ ->
        val index = value.toInt()
        spoilageStatistic[index].label
    }
    val wasteSeries = spoilageStatistic.map { it.waste }
    val consumedSeries = spoilageStatistic.map { it.consumed }
    val maxCount = maxOf(
        wasteSeries.maxOrNull() ?: 0,
        consumedSeries.maxOrNull() ?: 0,
    )

    val maxY = when {
        maxCount <= 5 -> 8.0
        else -> kotlin.math.ceil(maxCount * 1.2)
    }

    val rangeProvider = CartesianLayerRangeProvider.fixed(minY = 0.0, maxY = maxY)
    val chartStep = calculateStep(maxCount)
    val markerListener = SpoilageHistoryMarker(
        onValueChange = onValueChange,
        onValueHide = onValueHide
    )

    val scheme = MaterialTheme.colorScheme
    val resource = LocalResources.current

    val markerValueFormatter = DefaultCartesianMarker.ValueFormatter { _, targets ->
        val points = (targets.first() as LineCartesianLayerMarkerTarget).points

        val consumedPoint = points.getOrNull(0)?.entry?.y?.toInt() ?: 0
        val wastedPoint = points.getOrNull(1)?.entry?.y?.toInt() ?: 0

        val consumedPointText = resource.getString(
            R.string.analytics_spoilage_history_consumed_value,
            consumedPoint,
        )

        val wastedPointText = resource.getString(
            R.string.analytics_spoilage_history_wasted_value,
            wastedPoint,
        )

        val separator = " - "
        val fullText = "$consumedPointText$separator$wastedPointText"

        SpannableString(fullText).apply {
            setSpan(
                ForegroundColorSpan(extendColor.consumed.toArgb()),
                0,
                consumedPointText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )

            val wastedStart = consumedPointText.length + separator.length

            setSpan(
                ForegroundColorSpan(extendColor.wasted.toArgb()),
                wastedStart,
                fullText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }
    }

    val marker = rememberDefaultCartesianMarker(
        label = rememberTextComponent(
            padding = Insets(8.dp),
            background = ShapeComponent(
                shape = RoundedCornerShape(24.dp),
                fill = Fill(color = scheme.tertiary),
            ),
            style = MaterialTheme.typography.labelSmall,
        ),
        valueFormatter = markerValueFormatter,
        labelPosition = DefaultCartesianMarker.LabelPosition.Top,
    )

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineModel {
                series(y = consumedSeries)
                series(y = wasteSeries)
            }
        }
    }

    CartesianChartHost(
        modifier = modifier,
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                rangeProvider = rangeProvider,
                lineProvider = LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.Line(
                        fill = LineCartesianLayer.LineFill.single(
                            Fill(extendColor.consumed)
                        ),
                        areaFill = LineCartesianLayer.AreaFill.single(
                            fill = Fill(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        extendColor.consumed.copy(alpha = 0.24f),
                                        extendColor.consumed.copy(alpha = 0.08f),
                                        Color.Transparent
                                    )
                                )
                            )
                        ),
                        interpolator = LineCartesianLayer.Interpolator.catmullRom(),
                    ),
                    LineCartesianLayer.Line(
                        fill = LineCartesianLayer.LineFill.single(
                            Fill(extendColor.wasted)
                        ),
                        areaFill = LineCartesianLayer.AreaFill.single(
                            fill = Fill(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        extendColor.wasted.copy(alpha = 0.24f),
                                        extendColor.wasted.copy(alpha = 0.08f),
                                        Color.Transparent
                                    )
                                )
                            )
                        ),
                        interpolator = LineCartesianLayer.Interpolator.catmullRom(),
                    )
                ),
            ),
            marker = marker,
            markerVisibilityListener = markerListener,
            layerPadding = {
                CartesianLayerPadding(
                    /*scalableStart = localSpacing.extraSmall,
                    scalableEnd = localSpacing.extraSmall,
                    unscalableEnd = localSpacing.extraSmall,
                    unscalableStart = localSpacing.extraSmall*/
                )
            },
            startAxis = VerticalAxis.rememberStart(
                line = null,
                guideline = null,
                tick = null,
                label = rememberAxisLabelComponent(
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                ),
                valueFormatter = CartesianValueFormatter.decimal(),
                itemPlacer = VerticalAxis.ItemPlacer.step(
                    step = { chartStep.toDouble() }
                )
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                line = null,
                tick = null,
                guideline = null,
                label = rememberAxisLabelComponent(
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),

                    ),
                valueFormatter = bottomAxisValueFormatter,
                itemPlacer = remember {
                    HorizontalAxis.ItemPlacer.aligned(
                        spacing = { 1 },
                        offset = { 0 },
                        shiftExtremeLines = true,
                        addExtremeLabelPadding = true
                    )
                },
            ),
        ),
        animateIn = false,
        modelProducer = modelProducer,
    )
}

private fun calculateStep(maxValue: Int): Int {
    return when {
        maxValue <= 10 -> 1
        maxValue <= 20 -> 2
        maxValue <= 50 -> 5
        maxValue <= 100 -> 10
        else -> 20
    }
}