package com.nhuhuy.algidy.feature.detail.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.feature.detail.presentation.detail.component.DetailFabMenu
import com.nhuhuy.algidy.feature.detail.presentation.detail.component.DetailHeroCard
import com.nhuhuy.algidy.feature.detail.presentation.detail.component.DetailImage
import com.nhuhuy.algidy.feature.detail.presentation.detail.component.DetailNoteSection
import com.nhuhuy.algidy.feature.detail.presentation.detail.component.DetailStatsRow
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailUiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onBackPress: () -> Unit,
    openEditSheet: () -> Unit,
    openWastedDialog: () -> Unit,
    openConsumedDialog: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(
                        text = "Detail Screen",
                        fontWeight = FontWeight.Bold
                    )
                },
                subtitle = {
                    Text(
                        text = "Don't forget to consume this."
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            DetailFabMenu(
                onEditClick = openEditSheet,
                onWastedClick = openWastedDialog,
                onConsumedClick = openConsumedDialog
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 80.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                DetailImage(imageUri = uiState.foodItem.imageUri)
            }
            item {
                DetailStatsRow(item = uiState.foodItem)
            }

            item {
                DetailHeroCard(item = uiState.foodItem)
            }

            item {
                DetailNoteSection(item = uiState.foodItem)
            }
        }
    }
}