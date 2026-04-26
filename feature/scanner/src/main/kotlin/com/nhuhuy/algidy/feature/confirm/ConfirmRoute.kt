package com.nhuhuy.algidy.feature.confirm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.feature.confirm.presentation.ConfirmDatePickerDialog
import com.nhuhuy.algidy.feature.confirm.presentation.ConfirmScreen
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmOverlay
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmViewModel

@Composable
fun ConfirmRoute(
    onNavigateBack: () -> Unit,
    viewModel: ConfirmViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    ConfirmScreen(
        uiState = uiState,
        onNameChange = { name ->
            onAction(ConfirmAction.OnNameChange(name))
        },
        onQuantityChange = { qty ->
            onAction(ConfirmAction.OnQuantityChange(qty))
        },
        onUnitSelected = { unit ->
            onAction(ConfirmAction.OnUnitSelected(unit))
        },
        onToggleUnitMenu = { isOpen ->
            onAction(ConfirmAction.OnToggleUnitMenu(isOpen))
        },
        onTogglePurchaseDatePicker = { isOpen ->
            onAction(ConfirmAction.OnTogglePurchaseDatePicker(isOpen))
        },
        onToggleExpiryDatePicker = { isOpen ->
            onAction(ConfirmAction.OnToggleExpiryDatePicker(isOpen))
        },
        onLocationChange = { loc ->
            onAction(ConfirmAction.OnLocationChange(loc))
        },
        onNotesChange = { notes ->
            onAction(ConfirmAction.OnNotesChange(notes))
        },
        onSaveClick = {
            onAction(ConfirmAction.OnSaveClick)
        },
        onBackClick = onNavigateBack
    )


    when (uiState.overlay) {
        ConfirmOverlay.NONE -> Unit
        ConfirmOverlay.EXPIRY_DATE_PICKER -> ConfirmDatePickerDialog(
            initialDate = uiState.foodItem.purchaseDate,
            onDateSelected = { date ->
                onAction(ConfirmAction.OnExpiryDateChange(date))
            },
            onDismiss = { onAction(ConfirmAction.OnDismissRequest) }
        )

        ConfirmOverlay.PURCHASE_DATE_PICKER -> ConfirmDatePickerDialog(
            initialDate = uiState.foodItem.purchaseDate,
            onDateSelected = { date ->
                onAction(ConfirmAction.OnPurchaseDateChange(date))
            },
            onDismiss = { onAction(ConfirmAction.OnDismissRequest) }
        )

        ConfirmOverlay.ERROR_DIALOG -> Unit
    }
}