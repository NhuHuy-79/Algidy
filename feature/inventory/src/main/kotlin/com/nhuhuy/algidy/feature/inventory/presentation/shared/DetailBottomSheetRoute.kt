package com.nhuhuy.algidy.feature.inventory.presentation.shared

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreOwner
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.core.designsystem.component.AppBottomSheet
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel
import com.nhuhuy.algidy.feature.inventory.presentation.shared.viewmodel.DetailAction
import com.nhuhuy.algidy.feature.inventory.presentation.shared.viewmodel.DetailEvent
import com.nhuhuy.algidy.feature.inventory.presentation.shared.viewmodel.DetailOverlay
import com.nhuhuy.algidy.feature.inventory.presentation.shared.viewmodel.DetailUiState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBottomSheetRoute(
    foodItem: FoodUiModel,
    onDismiss: () -> Unit,
    onNavigateToEdit: (FoodUiModel) -> Unit,
) {
    val rememberViewModelStoreOwner = rememberViewModelStoreOwner()
    val viewModel: DetailBottomSheetViewModel = koinViewModel(
        viewModelStoreOwner = rememberViewModelStoreOwner
    ) { parametersOf(foodItem) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            DetailEvent.OnDismiss -> onDismiss()
            is DetailEvent.NavigateToEdit -> onNavigateToEdit(event.foodUiModel)
        }
    }

    AppBottomSheet(
        onDismiss = onDismiss,
    ) {
        DetailBottomSheetContent(
            uiState = uiState,
            onEditClick = { onAction(DetailAction.OnEditClick) },
            onConsumedClick = { onAction(DetailAction.OnConsumeClick) },
            onWastedClick = { onAction(DetailAction.OnWasteClick) }
        )
    }

    DetailBottomSheetOverlays(
        uiState = uiState,
        onDismissOverlay = { onAction(DetailAction.OnDismiss) },
        onConsumeConfirm = { onAction(DetailAction.OnConsumeConfirm) },
        onWasteConfirm = { onAction(DetailAction.OnWasteConfirm) }
    )
}

@Composable
fun DetailBottomSheetOverlays(
    uiState: DetailUiState,
    onDismissOverlay: () -> Unit,
    onConsumeConfirm: () -> Unit,
    onWasteConfirm: () -> Unit
) {
    when (uiState.overlay) {
        DetailOverlay.None -> Unit
        DetailOverlay.ConsumeConfirm -> AlgidyAlertDialog(
            icon = AlgidyIcons.ConsumeFood.toImageVector(),
            onDismissRequest = onDismissOverlay,
            onConfirm = onConsumeConfirm,
            title = stringResource(R.string.detail_dialog_consume_title),
            text = stringResource(R.string.detail_dialog_consume_content),
            confirmText = stringResource(R.string.detail_fab_consume_this)
        )

        DetailOverlay.WasteConfirm -> AlgidyAlertDialog(
            icon = AlgidyIcons.WasteFood.toImageVector(),
            onDismissRequest = onDismissOverlay,
            onConfirm = onWasteConfirm,
            title = stringResource(R.string.detail_dialog_waste_title),
            text = stringResource(R.string.detail_dialog_waste_content),
            confirmText = stringResource(R.string.detail_fab_mark_as_wasted),
            isDestructive = true
        )
    }
}