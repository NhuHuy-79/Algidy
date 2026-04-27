package com.nhuhuy.algidy.feature.confirm

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.feature.confirm.presentation.ConfirmDatePickerDialog
import com.nhuhuy.algidy.feature.confirm.presentation.ConfirmScreen
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction.OnDismissRequest
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction.OnExitAlertDialog
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction.OnExpiryDateChange
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction.OnLocationChange
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction.OnNameChange
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction.OnNotesChange
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction.OnPurchaseDateChange
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction.OnQuantityChange
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction.OnSaveClick
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction.OnToggleExpiryDatePicker
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction.OnTogglePurchaseDatePicker
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction.OnToggleUnitMenu
import com.nhuhuy.algidy.feature.confirm.viewmodel.ConfirmAction.OnUnitSelected
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
            onAction(OnNameChange(name))
        },
        onQuantityChange = { qty ->
            onAction(OnQuantityChange(qty))
        },
        onUnitSelected = { unit ->
            onAction(OnUnitSelected(unit))
        },
        onToggleUnitMenu = { isOpen ->
            onAction(OnToggleUnitMenu(isOpen))
        },
        onTogglePurchaseDatePicker = { isOpen ->
            onAction(OnTogglePurchaseDatePicker(isOpen))
        },
        onToggleExpiryDatePicker = { isOpen ->
            onAction(OnToggleExpiryDatePicker(isOpen))
        },
        onLocationChange = { loc ->
            onAction(OnLocationChange(loc))
        },
        onNotesChange = { notes ->
            onAction(OnNotesChange(notes))
        },
        onSaveClick = {
            onAction(OnSaveClick)
        },
        onBackClick = {
            onAction(OnExitAlertDialog)
        }
    )


    when (uiState.overlay) {
        ConfirmOverlay.NONE -> Unit
        ConfirmOverlay.EXPIRY_DATE_PICKER -> ConfirmDatePickerDialog(
            initialDate = uiState.foodItem.purchaseDate,
            onDateSelected = { date ->
                onAction(OnExpiryDateChange(date))
            },
            onDismiss = { onAction(OnDismissRequest) }
        )

        ConfirmOverlay.PURCHASE_DATE_PICKER -> ConfirmDatePickerDialog(
            initialDate = uiState.foodItem.purchaseDate,
            onDateSelected = { date ->
                onAction(OnPurchaseDateChange(date))
            },
            onDismiss = { onAction(OnDismissRequest) }
        )

        ConfirmOverlay.ERROR_DIALOG -> Unit
        ConfirmOverlay.EXIT_DIALOG -> AlgidyAlertDialog(
            onDismissRequest = {
                onAction(OnDismissRequest)
            },
            onConfirm = {
                onNavigateBack()
            },
            title = "Discard Changes?",
            text = "Are you sure you want to go back? All food information you've entered will be lost and won't be saved to your pantry.",
            confirmText = "Discard",
            dismissText = "Keep Editing",
            icon = Icons.Rounded.WarningAmber,
            confirmButtonColor = MaterialTheme.colorScheme.error,
            isDestructive = true,
        )
    }
}