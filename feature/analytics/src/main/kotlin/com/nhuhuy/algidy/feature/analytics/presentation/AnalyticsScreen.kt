@file:OptIn(ExperimentalMaterial3Api::class)

package com.nhuhuy.algidy.feature.analytics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.designsystem.theme.AlgidyTheme
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.analytics.domain.model.fakeFreshnessStatistic
import com.nhuhuy.algidy.feature.analytics.presentation.component.AnalyticsState
import com.nhuhuy.algidy.feature.analytics.presentation.component.SpoilageHistory
import com.nhuhuy.algidy.feature.analytics.presentation.component.WeeklyFreshness
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsAction
import com.nhuhuy.algidy.feature.analytics.presentation.viewmodel.AnalyticsUiState

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnalyticsScreen(
    uiState: AnalyticsUiState,
    onBackPress: () -> Unit,
    onAction: (AnalyticsAction) -> Unit,
) {
    val extendColor = AlgidyTheme.extendedColors
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        topBar = {
            MediumFlexibleTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
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
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 96.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item(span = StaggeredGridItemSpan.SingleLane) {
                AnalyticsState(
                    title = "Expired",
                    content = "12",
                    containerColor = extendColor.expired,
                    contentColor = extendColor.onExpired
                )
            }

            item(span = StaggeredGridItemSpan.SingleLane) {
                AnalyticsState(
                    title = "Expiring",
                    content = "12",
                    containerColor = extendColor.notice,
                    contentColor = extendColor.onNotice
                )
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                WeeklyFreshness(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    itemPosition = ItemPosition.TOP,
                    freshnessStatistic = fakeFreshnessStatistic
                )
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                SpoilageHistory(
                    itemPosition = ItemPosition.BOTTOM,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                )
            }
        }
    }
}
