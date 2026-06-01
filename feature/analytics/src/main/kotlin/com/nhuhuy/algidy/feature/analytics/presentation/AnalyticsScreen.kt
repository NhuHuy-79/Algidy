@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhuhuy.algidy.feature.analytics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.analytics.presentation.component.OverallChartLabel
import com.nhuhuy.algidy.feature.analytics.presentation.component.OverallCircularChart
import com.nhuhuy.algidy.feature.analytics.presentation.component.SpoilageHistoryChart
import com.nhuhuy.algidy.feature.analytics.presentation.component.WeeklyFreshnessChart
import com.nhuhuy.algidy.feature.analytics.presentation.component.WeeklyProgressCard
import com.nhuhuy.algidy.feature.analytics.presentation.component.toContainerColor
import com.nhuhuy.algidy.feature.analytics.presentation.component.toContentColor
import com.nhuhuy.algidy.feature.analytics.presentation.component.toImageVector
import com.nhuhuy.algidy.feature.analytics.presentation.component.toStringRes
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsAction
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsUiState
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.CircularChartData

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnalyticsScreen(
    uiState: AnalyticsUiState,
    onBackPress: () -> Unit,
    onAction: (AnalyticsAction) -> Unit,
) {
    var selectedFreshness by remember { mutableStateOf(Freshness.FRESH) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumFlexibleTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onBackPress
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.analytics_title),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                },
                subtitle = {
                    Text(
                        text = stringResource(R.string.analytics_subtitle)
                    )
                }
            )
        }
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            //First Page
            val itemSpacing = 24.dp
            val mainCardWeeklyHeight = 64.dp
            val overallChartHeight = maxHeight * 0.25f
            val newWeeklyFreshnessChart =
                maxHeight - overallChartHeight - mainCardWeeklyHeight - itemSpacing * 3

            //Two Page
            val bottomSpacing = 16.dp
            val spoilageChartHeight = (maxHeight - bottomSpacing) * 0.55f

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalItemSpacing = itemSpacing
            ) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    WeeklyProgressCard(
                        contentColor = uiState.circularChartData.toContentColor(),
                        activeBackgroundColor = uiState.circularChartData.toContainerColor(),
                        disabledBackgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier.height(mainCardWeeklyHeight)
                    )
                }
                item(span = StaggeredGridItemSpan.SingleLane) {
                    OverallCircularChart(
                        modifier = Modifier.height(overallChartHeight),
                        value = uiState.getFloatByCircularChart(),
                        containerColor = uiState.circularChartData.toContainerColor(),
                        contentColor = uiState.circularChartData.toContentColor(),
                    )
                }

                item(span = StaggeredGridItemSpan.SingleLane) {
                    Column(
                        modifier = Modifier.height(overallChartHeight),
                        verticalArrangement = Arrangement.spacedBy(space = 4.dp)
                    ) {
                        CircularChartData.entries.forEach { data ->
                            OverallChartLabel(
                                modifier = Modifier.weight(1f),
                                selected = data == uiState.circularChartData,
                                icon = data.toImageVector(),
                                label = data.toStringRes(),
                                count = uiState.getCountByCircularChart(),
                                backgroundColor = data.toContainerColor(),
                                contentColor = data.toContentColor(),
                                onClick = {
                                    onAction(AnalyticsAction.OnChartDataSelect(data))
                                }
                            )
                        }
                    }
                }

                item(span = StaggeredGridItemSpan.FullLine) {
                    WeeklyFreshnessChart(
                        modifier = Modifier.height(newWeeklyFreshnessChart),
                        selectedFreshness = selectedFreshness,
                        uiModel = uiState.expiryChartUiModel,
                        onSelectFreshness = { freshness -> selectedFreshness = freshness }
                    )
                }

                item(span = StaggeredGridItemSpan.FullLine) {
                    SpoilageHistoryChart(
                        modifier = Modifier.height(spoilageChartHeight),
                        uiModel = uiState.spoilageChartUiModel
                    )
                }
            }
        }
    }
}
