package com.nhuhuy.algidy.feature.analytics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.feature.analytics.presentation.component.ExpiryChart
import com.nhuhuy.algidy.feature.analytics.presentation.component.OverallCard
import com.nhuhuy.algidy.feature.analytics.presentation.component.WastedCategoryCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnalyticsScreen(
    onBackPress: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
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
                        text = "Analytics Screen"
                    )
                },
                subtitle = {
                    Text(
                        text = "Overview of waste and impact"
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    OverallCard(
                        title = "60%",
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(
                            topStart = 32.dp,
                            bottomStart = 32.dp,
                            topEnd = 8.dp,
                            bottomEnd = 8.dp
                        )
                    )
                    OverallCard(
                        title = "40%",
                        modifier = Modifier.weight(1f),
                        icon = Icons.Rounded.Delete,
                        iconColor = MaterialTheme.colorScheme.error,
                        backgroundColor = MaterialTheme.colorScheme.onError,
                        contentColor = MaterialTheme.colorScheme.errorContainer,
                        containerColor = MaterialTheme.colorScheme.onErrorContainer,
                        shape = RoundedCornerShape(
                            bottomEnd = 32.dp,
                            bottomStart = 8.dp,
                            topEnd = 32.dp,
                            topStart = 8.dp
                        )
                    )
                }

            }

            item {
                ExpiryChart()
            }

            item {
                WastedCategoryCard()
            }
        }
    }
}