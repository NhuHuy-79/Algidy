package com.nhuhuy.algidy.feature.food_entry.navigation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.AppDatePickerDialog
import com.nhuhuy.algidy.core.presentation.component.TextFieldDialog
import com.nhuhuy.algidy.feature.food_entry.presentation.FoodEntryScreen
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryAction
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryAction.OnCategoryConfirm
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryAction.OnCategoryQueryChange
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryEvent
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryOverlay
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryUiState
import com.nhuhuy.algidy.feature.food_entry.presentation.viewmodel.FoodEntryViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FoodEntryRoute(
    foodId: String?
) = BoxLayout {
    val viewModel: FoodEntryViewModel = koinViewModel(parameters = { parametersOf(foodId) })
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            onAction(FoodEntryAction.OnNotificationGranted(isGranted))
        }
    )

    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            FoodEntryEvent.AskNotificationPermission -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    FoodEntryScreen(
        uiState = uiState,
        onAction = onAction
    )

    FoodEntryOverlayContainer(
        state = uiState,
        onAction = onAction
    )

}

@Composable
private fun FoodEntryOverlayContainer(
    state: FoodEntryUiState,
    onAction: (FoodEntryAction) -> Unit
) {
    val onDismiss = { onAction(FoodEntryAction.OnDismissOverlay) }
    when (state.overlay) {
        FoodEntryOverlay.NONE -> Unit
        FoodEntryOverlay.PURCHASE_DATE_PICKER -> {
            AppDatePickerDialog(
                initialDateMillis = state.purchaseDate,
                title = stringResource(R.string.confirm_label_purchase_date),
                onDateSelected = {
                    onAction(FoodEntryAction.OnPurchaseDateChange(it))
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }

        FoodEntryOverlay.EXPIRY_DATE_PICKER -> {
            AppDatePickerDialog(
                initialDateMillis = if (state.expiryDate == -1L) null else state.expiryDate,
                title = stringResource(R.string.confirm_label_expiry_date),
                onDateSelected = {
                    onAction(FoodEntryAction.OnExpiryDateChange(it))
                    onDismiss()
                },
                onDismiss = onDismiss
            )
        }

        FoodEntryOverlay.CATEGORY_ADD -> {
            TextFieldDialog(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = state.categoryQuery,
                title = stringResource(R.string.inventory_category_add),
                confirmText = stringResource(R.string.action_add),
                onValueChange = { onAction(OnCategoryQueryChange(it)) },
                onDismiss = onDismiss,
                onConfirm = { onAction(OnCategoryConfirm) },
            )
        }
    }
}
