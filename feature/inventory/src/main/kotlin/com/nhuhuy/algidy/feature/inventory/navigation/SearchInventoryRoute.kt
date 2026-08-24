package com.nhuhuy.algidy.feature.inventory.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.feature.food_entry.navigation.FoodEntryRoute
import com.nhuhuy.algidy.feature.inventory.presentation.search.SearchInventoryScreen
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchAction
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchUiSurface
import com.nhuhuy.algidy.feature.inventory.presentation.search.viewmodel.SearchViewModel
import com.nhuhuy.algidy.feature.inventory.presentation.shared.DetailBottomSheetRoute
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
        is SearchUiSurface.DetailBottomSheet -> DetailBottomSheetRoute(
            foodItem = surface.food,
            onDismiss = { onAction(SearchAction.OnDismiss) },
            onNavigateToEdit = { foodUiModel ->
                onAction(SearchAction.OnEditSheetOpen(foodUiModel))
            }
        )

        SearchUiSurface.None -> Unit
        is SearchUiSurface.EditFoodSheet -> FoodEntryRoute(
            foodId = surface.food.id,
            onDismiss = { onAction(SearchAction.OnDismiss) }
        )
    }
}