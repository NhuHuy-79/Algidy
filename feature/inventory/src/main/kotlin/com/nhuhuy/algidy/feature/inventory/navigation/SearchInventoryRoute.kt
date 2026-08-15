package com.nhuhuy.algidy.feature.inventory.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.detail.DetailBottomSheet
import com.nhuhuy.algidy.feature.inventory.presentation.search.SearchInventoryScreen
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchAction
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchUiSurface
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchInventoryRoute(
    onNavigateBack: () -> Unit,
) = BoxLayout {
    val viewModel: SearchViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    SearchInventoryScreen(
        uiState = uiState,
        onBackClick = onNavigateBack,
        onAction = onAction,
    )

    when (val surface = uiState.surface) {
        is SearchUiSurface.DetailBottomSheet -> {
            DetailBottomSheet(
                foodItem = surface.food,
                onDismiss = {
                    onAction(SearchAction.OnDismiss)
                },
                onWastedClick = {},
                onConsumedClick = {}
            )
        }

        SearchUiSurface.None -> Unit
    }
}