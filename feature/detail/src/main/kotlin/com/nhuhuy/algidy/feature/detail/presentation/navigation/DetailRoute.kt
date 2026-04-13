package com.nhuhuy.algidy.feature.detail.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.feature.detail.presentation.DetailScreen
import com.nhuhuy.algidy.feature.detail.presentation.viewModel.DetailViewModel

@Composable
fun DetailRoute(
    viewModel: DetailViewModel,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BoxLayout {
        DetailScreen(
            uiState = uiState,
            onBackPress = onNavigateBack
        )
    }
}