package com.nhuhuy.algidy.feature.inventory.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.feature.inventory.presentation.search.SearchInventoryScreen
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchInventoryRoute(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (id: String) -> Unit,
) {
    val viewModel: SearchViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction
    SearchInventoryScreen(
        uiState = uiState,
        onBackClick = onNavigateBack,
        onNavigateToDetail = onNavigateToDetail,
        onAction = onAction,
    )
}