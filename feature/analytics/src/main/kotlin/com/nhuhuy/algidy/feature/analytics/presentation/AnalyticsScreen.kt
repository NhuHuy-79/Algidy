@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhuhuy.algidy.feature.analytics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.analytics.presentation.component.OverallCard
import com.nhuhuy.algidy.feature.analytics.presentation.component.SpoilageHistoryChart
import com.nhuhuy.algidy.feature.analytics.presentation.component.WastedCategoryCard
import com.nhuhuy.algidy.feature.analytics.presentation.component.WeeklyFreshnessChart
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsUiState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnalyticsScreen(
    uiState: AnalyticsUiState,
    onBackPress: () -> Unit,
) {
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
                        text = stringResource(R.string.analytics_title)
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
            val availableHeight = maxHeight

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(minSize = 360.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalItemSpacing = 16.dp
            ) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    OverallCard(
                        modifier = Modifier.height(availableHeight * 0.35f),
                        foodNumberInWeek = 6,
                        consumePercent = 0.8f,
                        wastedPercent = 0.2f
                    )
                }

                item(span = StaggeredGridItemSpan.FullLine) {
                    SpoilageHistoryChart(
                        modifier = Modifier.height(availableHeight * 0.6f)
                    )
                }
                item(span = StaggeredGridItemSpan.FullLine) {
                    WeeklyFreshnessChart(
                        uiModel = uiState.expiryChartUiModel
                    )
                }

                item {
                    WastedCategoryCard(categories = uiState.wastedByCategory)
                }
            }
        }
    }
}
