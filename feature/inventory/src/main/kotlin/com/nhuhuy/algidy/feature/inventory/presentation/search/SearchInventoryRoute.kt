package com.nhuhuy.algidy.feature.inventory.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchInventoryRoute(
    viewModel: SearchViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction
    SearchInventoryScreen(
        uiState = uiState,
        onBackClick = onNavigateBack,
        onAction = onAction,
    )
}