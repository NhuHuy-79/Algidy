package com.nhuhuy.algidy.feature.inventory.presentation.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreOwner
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.core.designsystem.component.AppBottomSheet
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.AppIcon
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.designsystem.tokens.LocalAlgidySpacing
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.detail.DetailMainContent
import com.nhuhuy.algidy.feature.inventory.presentation.model.FoodUiModel
import com.nhuhuy.algidy.toReadableText
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBottomSheetRoute(
    foodItem: FoodUiModel,
    onDismiss: () -> Unit
) {
    val viewModelStoreOwner = rememberViewModelStoreOwner()
    val viewModel: DetailBottomSheetViewModel = koinViewModel(
        viewModelStoreOwner = viewModelStoreOwner
    ) { parametersOf(foodItem) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppBottomSheet(onDismiss = onDismiss) {
        DetailBottomSheetContent(
            uiState = uiState,
            onEditClick = { viewModel.onEditClick(onDismiss) },
            onConsumedClick = viewModel::onConsumedClick,
            onWastedClick = viewModel::onWastedClick
        )
    }

    DetailBottomSheetOverlays(
        uiState = uiState,
        onDismissOverlay = viewModel::onDismissOverlay,
        onConsumeConfirm = { viewModel.onConsumeConfirm(onDismiss) },
        onWasteConfirm = { viewModel.onWasteConfirm(onDismiss) }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun DetailBottomSheetContent(
    uiState: DetailBottomSheetUiState,
    onEditClick: () -> Unit,
    onConsumedClick: () -> Unit,
    onWastedClick: () -> Unit
) {
    MaterialTheme.colorScheme
    val localSpacing = LocalAlgidySpacing.current
    val foodItem = uiState.foodItem

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(vertical = 24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = foodItem.name,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(localSpacing.extraSmall))

        Text(
            text = "${foodItem.purchaseDate.toReadableText()} - ${foodItem.expiryDate.toReadableText()}",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )

        Spacer(modifier = Modifier.height(localSpacing.medium))

        DetailMainContent(
            categoryUiModel = foodItem.categoryUiModel,
            foodItem = foodItem,
            onEditClick = onEditClick
        )

        Spacer(modifier = Modifier.height(localSpacing.large))

        ButtonGroup(
            modifier = Modifier.fillMaxWidth(),
            overflowIndicator = {},
            expandedRatio = 0f
        ) {
            clickableItem(
                weight = 1f,
                onClick = onEditClick,
                label = "",
                icon = {
                    AppIcon(iconProvider = AlgidyIcons.Inventory.EditFood)
                }
            )

            clickableItem(
                weight = 1f,
                onClick = onConsumedClick,
                label = "",
                icon = {
                    AppIcon(iconProvider = AlgidyIcons.ConsumeFood)
                }
            )
        }
    }
}

@Composable
private fun DetailBottomSheetOverlays(
    uiState: DetailBottomSheetUiState,
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
