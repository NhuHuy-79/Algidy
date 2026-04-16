package com.nhuhuy.algidy.feature.analytics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Restaurant
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
import com.nhuhuy.algidy.feature.analytics.presentation.component.CategoryWasteCard
import com.nhuhuy.algidy.feature.analytics.presentation.component.ImpactScoreBanner
import com.nhuhuy.algidy.feature.analytics.presentation.component.StatQuickCard
import com.nhuhuy.algidy.feature.analytics.presentation.component.WastedCategory
import com.nhuhuy.algidy.feature.analytics.presentation.component.WaterPieChart

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnalyticsScreen() {
    val sampleData = listOf(
        WastedCategory("Vegetables", "12.4kg", 0.7f),
        WastedCategory("Dairy", "8.2kg", 0.45f),
        WastedCategory("Fruits", "5.1kg", 0.25f)
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumFlexibleTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {}
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //Pie Chart ( Wasted - Consume)
            item {
                WaterPieChart()
            }
            //Wasted by Category
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatQuickCard(
                        modifier = Modifier.weight(1f),
                        count = "142",
                        label = "Consumed",
                        icon = Icons.Outlined.Restaurant,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                    StatQuickCard(
                        modifier = Modifier.weight(1f),
                        count = "26",
                        label = "Wasted",
                        icon = Icons.Outlined.Delete,
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                }
            }

            item { CategoryWasteCard(categories = sampleData) }
            item {
                ImpactScoreBanner(message = "You've prevented 4.2 tons of CO2 emissions this month.")
            }
        }
    }
}