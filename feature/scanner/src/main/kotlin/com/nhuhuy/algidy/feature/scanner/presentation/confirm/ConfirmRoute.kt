package com.nhuhuy.algidy.feature.scanner.presentation.confirm

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.showShortToast
import com.nhuhuy.algidy.feature.scanner.presentation.confirm.viewmodel.ConfirmAction
import com.nhuhuy.algidy.feature.scanner.presentation.confirm.viewmodel.ConfirmEvent
import com.nhuhuy.algidy.feature.scanner.presentation.confirm.viewmodel.ConfirmOverlay
import com.nhuhuy.algidy.feature.scanner.presentation.confirm.viewmodel.ConfirmViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConfirmRoute(
    onNavigateBack: () -> Unit,
    viewModel: ConfirmViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel::onEntryAction
    val onAction = viewModel::onAction
    val applicationContext = LocalContext.current.applicationContext

    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            ConfirmEvent.OnSaveSuccessfully -> onNavigateBack()
            ConfirmEvent.OnImageChangeFailed -> applicationContext.showShortToast("Failed to save image")
        }
    }

    ConfirmScreen(
        uiState = uiState,
        onImageChange = { uri ->
            onAction(ConfirmAction.OnImageChange(uri))
        },
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
        onBackClick = {
            onAction(ConfirmAction.OnExitAlertDialog)
        }
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
        ConfirmOverlay.EXIT_DIALOG -> AlgidyAlertDialog(
            onDismissRequest = {
                onAction(ConfirmAction.OnDismissRequest)
            },
            onConfirm = {
                onNavigateBack()
            },
            title = stringResource(R.string.confirm_dialog_discard_title),
            text = stringResource(R.string.confirm_dialog_discard_content),
            confirmText = stringResource(R.string.confirm_dialog_discard_btn_discard),
            dismissText = stringResource(R.string.confirm_dialog_discard_btn_keep),
            icon = Icons.Rounded.WarningAmber,
            confirmButtonColor = MaterialTheme.colorScheme.error,
            isDestructive = true,
        )
    }
}
