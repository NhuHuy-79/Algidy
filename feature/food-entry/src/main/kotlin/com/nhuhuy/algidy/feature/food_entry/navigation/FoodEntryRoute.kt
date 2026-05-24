package com.nhuhuy.algidy.feature.food_entry.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.AppDatePickerDialog
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.core.presentation.viewmodel.FoodEntryOverlay
import com.nhuhuy.algidy.feature.food_entry.presentation.component.FoodEntryScreen
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryEvent
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FoodEntryRoute(
    title: String,
    initialFoodItem: FoodItem?,
    onNavigateBack: () -> Unit
) {
    val viewModel: FoodEntryViewModel = koinViewModel(
        parameters = { parametersOf(initialFoodItem) }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorState by viewModel.entryError.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            FoodEntryEvent.OnSaveSuccess -> onNavigateBack()
            FoodEntryEvent.NavigateBack -> onNavigateBack()
        }
    }

    BoxLayout {
        FoodEntryScreen(
            title = title,
            uiState = uiState,
            errorState = errorState,
            onAction = onAction
        )

        // Overlay handling
        when (uiState.overlay) {
            FoodEntryOverlay.NONE -> Unit
            FoodEntryOverlay.PURCHASE_DATE_PICKER -> {
                AppDatePickerDialog(
                    initialDateMillis = uiState.purchaseDate,
                    title = stringResource(R.string.confirm_label_purchase_date),
                    onDateSelected = {
                        onAction(FoodEntryAction.OnPurchaseDateChange(it))
                        onAction(FoodEntryAction.OnDismissOverlay)
                    },
                    onDismiss = { onAction(FoodEntryAction.OnDismissOverlay) }
                )
            }

            FoodEntryOverlay.EXPIRY_DATE_PICKER -> {
                AppDatePickerDialog(
                    initialDateMillis = if (uiState.expiryDate == -1L) null else uiState.expiryDate,
                    title = stringResource(R.string.confirm_label_expiry_date),
                    onDateSelected = {
                        onAction(FoodEntryAction.OnExpiryDateChange(it))
                        onAction(FoodEntryAction.OnDismissOverlay)
                    },
                    onDismiss = { onAction(FoodEntryAction.OnDismissOverlay) }
                )
            }

            FoodEntryOverlay.CATEGORY_PICKER -> {
                // Implement Category Picker dialog if needed
            }

            FoodEntryOverlay.UNIT_PICKER -> {
                // Implement Unit Picker dialog if needed
            }

            FoodEntryOverlay.STORAGE_PICKER -> {
                // Implement Storage Picker dialog if needed
            }
        }
    }
}
