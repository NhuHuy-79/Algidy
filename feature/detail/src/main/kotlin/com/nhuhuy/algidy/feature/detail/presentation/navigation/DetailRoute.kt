package com.nhuhuy.algidy.feature.detail.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.component.showShortToast
import com.nhuhuy.algidy.feature.detail.presentation.detail.DetailScreen
import com.nhuhuy.algidy.feature.detail.presentation.detail.component.EditFoodBottomSheet
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailAction
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailEvent
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailOverlay
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailViewModel

@Composable
fun DetailRoute(
    viewModel: DetailViewModel,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val entryError by viewModel.entryError.collectAsStateWithLifecycle()
    val entryState by viewModel.entryState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    val applicationContext = LocalContext.current.applicationContext

    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            DetailEvent.OnImageChangeFailed -> applicationContext.showShortToast("Failed to change image")
        }
    }

    BoxLayout {
        DetailScreen(
            uiState = uiState,
            onBackPress = onNavigateBack,
            onImageChange = { uri -> onAction(DetailAction.EditEntryAction.OnImageChange(uri)) },
            openEditSheet = { onAction(DetailAction.OnEditItem) },
            openWastedDialog = { onAction(DetailAction.OnWastedItem) },
            openConsumedDialog = { onAction(DetailAction.OnConsumeItem) }
        )

        when (uiState.actionState) {
            DetailOverlay.None -> Unit
            DetailOverlay.Wasted -> {
                AlgidyAlertDialog(
                    onDismissRequest = { onAction(DetailAction.OnDismiss) },
                    onConfirm = {
                        onAction(DetailAction.OnWastedItem)
                        onNavigateBack()
                    },
                    title = "Mark as Wasted?",
                    text = "Are you sure this food is no longer usable? It will be moved to your waste history for tracking.",
                    confirmText = "Mark as Wasted",
                    dismissText = "Cancel",
                    icon = Icons.Rounded.Delete,
                    isDestructive = true
                )
            }

            DetailOverlay.Consume -> {
                AlgidyAlertDialog(
                    onDismissRequest = { onAction(DetailAction.OnDismiss) },
                    onConfirm = {
                        onAction(DetailAction.OnConsumeItem)
                        onNavigateBack()
                    },
                    title = "Finished Eating?",
                    text = "Great! We'll update your inventory and move this item to your consumption history.",
                    confirmText = "I Consumed It",
                    dismissText = "Not yet",
                    icon = Icons.Rounded.Restaurant,
                    isDestructive = false
                )
            }

            DetailOverlay.Edit -> {
                EditFoodBottomSheet(
                    editEntry = entryState,
                    errorState = entryError,
                    onAction = onAction
                )
            }
        }
    }
}
