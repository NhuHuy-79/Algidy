package com.nhuhuy.algidy.feature.detail.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.showShortToast
import com.nhuhuy.algidy.feature.detail.presentation.detail.DetailScreen
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailAction
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailEvent
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailOverlay
import com.nhuhuy.algidy.feature.detail.presentation.detail.viewModel.DetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailRoute(
    foodItemId: String,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (FoodItem) -> Unit,
) {
    val viewModel: DetailViewModel = koinViewModel(
        parameters = { parametersOf(foodItemId) }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
            openWastedDialog = { onAction(DetailAction.OnWasteFabPress) },
            openConsumedDialog = { onAction(DetailAction.OnConsumeFabPress) }
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
                    title = stringResource(R.string.detail_dialog_waste_title),
                    text = stringResource(R.string.detail_dialog_waste_content),
                    confirmText = stringResource(R.string.action_ok),
                    dismissText = stringResource(R.string.action_cancel),
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
                    title = stringResource(R.string.detail_dialog_consume_title),
                    text = stringResource(R.string.detail_dialog_consume_content),
                    confirmText = stringResource(R.string.action_ok),
                    dismissText = stringResource(R.string.action_cancel),
                    icon = Icons.Rounded.Restaurant,
                    isDestructive = false
                )
            }

            DetailOverlay.Edit -> {
                LaunchedEffect(Unit) {
                    onNavigateToEdit(uiState.detailFoodItem)
                    onAction(DetailAction.OnDismiss)
                }
            }
        }
    }
}
