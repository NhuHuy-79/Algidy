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
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.DeleteForever
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.model.food.Freshness
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.analytics.presentation.component.FreshnessSegmentedButton
import com.nhuhuy.algidy.feature.analytics.presentation.component.ProductStatsCard
import com.nhuhuy.algidy.feature.analytics.presentation.component.SpoilageHistoryChart
import com.nhuhuy.algidy.feature.analytics.presentation.component.WeeklyFreshnessChart
import com.nhuhuy.algidy.feature.analytics.presentation.component.WeeklyMainCard
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsUiState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnalyticsScreen(
    uiState: AnalyticsUiState,
    onBackPress: () -> Unit,
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
            val itemSpacing = 16.dp
            val weeklyMainCardHeight = maxHeight * 0.1f
            val productStatCardHeight = maxHeight * 0.15f
            val freshnessSegmentButtonHeight = maxHeight * 0.08f

            val newWeeklyFreshnessChart =
                maxHeight - (weeklyMainCardHeight + productStatCardHeight + freshnessSegmentButtonHeight + itemSpacing * 4)

            //Two Page
            val bottomSpacing = 16.dp
            val spoilageChartHeight = (maxHeight - bottomSpacing) * 0.5f


            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalItemSpacing = itemSpacing
            ) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    WeeklyMainCard(
                        modifier = Modifier.height(weeklyMainCardHeight),
                        productCountInWeek = uiState.weeklyFoodItemsCount
                    )
                }

                item {
                    ProductStatsCard(
                        modifier = Modifier.height(productStatCardHeight),
                        contentColor = AlgidyTheme.extendedColors.onConsumed,
                        containerColor = AlgidyTheme.extendedColors.consumed,
                        icon = Icons.Rounded.CheckCircleOutline,
                        title = stringResource(R.string.analytics_card_consumed),
                        description = uiState.consumedCount.toString(),
                    )
                }

                item {
                    ProductStatsCard(
                        modifier = Modifier.height(productStatCardHeight),
                        contentColor = AlgidyTheme.extendedColors.onWasted,
                        containerColor = AlgidyTheme.extendedColors.wasted,
                        icon = Icons.Rounded.DeleteForever,
                        title = stringResource(R.string.analytics_card_wasted),
                        description = uiState.wastedCount.toString(),
                    )
                }

                item {
                    ProductStatsCard(
                        modifier = Modifier.height(productStatCardHeight),
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        icon = ImageVector.vectorResource(com.nhuhuy.algidy.core.designsystem.R.drawable.ic_storage),
                        title = stringResource(R.string.analytics_card_others),
                        description = uiState.othersCount.toString(),
                    )
                }


                item(span = StaggeredGridItemSpan.FullLine) {
                    FreshnessSegmentedButton(
                        modifier = Modifier.height(freshnessSegmentButtonHeight),
                        selectedFreshness = selectedFreshness,
                        onSelectFreshness = { freshness -> selectedFreshness = freshness }
                    )
                }

                item(span = StaggeredGridItemSpan.FullLine) {
                    WeeklyFreshnessChart(
                        modifier = Modifier.height(newWeeklyFreshnessChart),
                        selectedFreshness = selectedFreshness,
                        uiModel = uiState.expiryChartUiModel
                    )
                }

                item(span = StaggeredGridItemSpan.FullLine) {
                    SpoilageHistoryChart(
                        modifier = Modifier.height(spoilageChartHeight),
                        uiModel = uiState.spoilageChartUiModel
                    )
                }

                /*item(span = StaggeredGridItemSpan.FullLine) {
                    WastedCategoryCard(
                        modifier = Modifier.height(locationChartHeight),
                        categories = uiState.wastedByCategory
                    )
                }*/
            }
        }
    }
}
