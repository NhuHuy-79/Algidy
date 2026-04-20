package com.nhuhuy.algidy.feature.detail.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.feature.detail.presentation.detail.DetailScreen
import com.nhuhuy.algidy.feature.detail.presentation.detail.component.EditFoodBottomSheet
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailAction
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailActionState
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailViewModel

@Composable
fun DetailRoute(
    viewModel: DetailViewModel,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorState by viewModel.errorState.collectAsStateWithLifecycle()
    val editEntry by viewModel.editEntry.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction
    BoxLayout {
        DetailScreen(
            uiState = uiState,
            onBackPress = onNavigateBack,
            openEditSheet = { onAction(DetailAction.OnEditItem) },
            openWastedDialog = { onAction(DetailAction.OnWastedItem) },
            openConsumedDialog = { onAction(DetailAction.OnConsumeItem) }
        )

        when (uiState.actionState) {
            DetailActionState.None -> Unit
            DetailActionState.Wasted -> {
                AlgidyAlertDialog(
                    onDismissRequest = { onAction(DetailAction.OnDismiss) },
                    onConfirm = { onAction(DetailAction.OnWastedItem) },
                    title = "Mark as Wasted?",
                    text = "Are you sure this food is no longer usable? It will be moved to your waste history for tracking.",
                    confirmText = "Mark as Wasted",
                    dismissText = "Cancel",
                    icon = Icons.Rounded.Delete,
                    isDestructive = true
                )
            }

            DetailActionState.Consume -> {
                AlgidyAlertDialog(
                    onDismissRequest = { onAction(DetailAction.OnDismiss) },
                    onConfirm = { onAction(DetailAction.OnConsumeItem) },
                    title = "Finished Eating?",
                    text = "Great! We'll update your inventory and move this item to your consumption history.",
                    confirmText = "I Consumed It",
                    dismissText = "Not yet",
                    icon = Icons.Rounded.Restaurant,
                    isDestructive = false
                )
            }

            DetailActionState.Edit -> {
                EditFoodBottomSheet(
                    editEntry = editEntry,
                    errorState = errorState,
                    onNameChange = { name -> onAction(DetailAction.EditEntryAction.OnNameChange(name)) },
                    onQuantityChange = { quantity ->
                        onAction(
                            DetailAction.EditEntryAction.OnQuantityChange(
                                quantity
                            )
                        )
                    },
                    onUnitItemChange = { unit ->
                        onAction(DetailAction.EditEntryAction.OnItemUnitChange(unit))
                    },
                    onNoteChange = { note -> onAction(DetailAction.EditEntryAction.OnNoteChange(note)) },
                    onLocationChange = { location ->
                        onAction(DetailAction.EditEntryAction.OnStorageLocationChange(location))
                    },
                    onExpiryDateChange = { expiryDate ->
                        onAction(
                            DetailAction.EditEntryAction.OnExpiryDateChange(
                                expiryDate
                            )
                        )
                    },
                    onDismiss = { onAction(DetailAction.OnDismiss) },
                    onSave = { onAction(DetailAction.EditEntryAction.OnSave) }
                )
            }
        }
    }
}