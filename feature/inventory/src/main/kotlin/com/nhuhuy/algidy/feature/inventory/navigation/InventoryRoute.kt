package com.nhuhuy.algidy.feature.inventory.navigation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scrim
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.AppNewFeatureBottomSheet
import com.nhuhuy.algidy.core.presentation.component.TextFieldDialog
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.InventoryScreen
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.CameraPolicyBottomSheet
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventoryFabMenu
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.detail.DetailBottomSheet
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction.OnEditCategorySheet.OnInputChange
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction.OnEditCategorySheet.Save
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryDetailAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryEvent
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryFabAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryOverlay
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryViewModel
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun InventoryRoute() = BoxLayout {
    val viewModel: InventoryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val combineState by viewModel.combineState.collectAsStateWithLifecycle()
    val inventoryResultState by viewModel.resultState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onAction(InventoryAction.OnCameraPermissionAccept)
            }
        }
    )

    ObserveEffect(flow = viewModel.uiEvent) { event ->
        Timber.d("Received Event: $event")
        when (event) {
            InventoryEvent.NavigateToScanner -> onAction(InventoryAction.OnCameraPermissionAccept)
            InventoryEvent.RequestCameraPermission -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    LaunchedEffect(Unit) {
        if (uiState.currentVersionCode < combineState.generalPreferences.appVersionToNotify) {
            onAction(InventoryAction.ShowAppFeature)
        }
    }

    BackHandler(enabled = uiState.expanded || uiState.overlay != InventoryOverlay.None) {
        if (uiState.expanded) {
            onAction(InventoryFabAction.ToggleFabMenu(false))
        } else {
            onAction(InventoryAction.OnDismiss)
        }
    }

    InventoryScreen(
        uiState = uiState,
        combineState = combineState,
        inventoryResultState = inventoryResultState,
        onAction = onAction
    )

    when (val overlay = uiState.overlay) {
        InventoryOverlay.None -> Unit

        InventoryOverlay.CategoryEdit -> TextFieldDialog(
            title = stringResource(R.string.category_edit_dialog_title),
            value = uiState.categoryInput,
            confirmText = stringResource(R.string.inventory_category_edit_btn),
            onValueChange = { category -> onAction(OnInputChange(category)) },
            onDismiss = { onAction(InventoryAction.OnDismiss) },
            onConfirm = { onAction(Save) }
        )

        InventoryOverlay.CategoryDelete -> AlgidyAlertDialog(
            icon = Icons.Rounded.Delete,
            onDismissRequest = { onAction(InventoryAction.OnDismiss) },
            onConfirm = { onAction(InventoryAction.OnDeleteAlertConfirm) },
            title = stringResource(R.string.delete_category_dialog_title),
            text = stringResource(R.string.delete_category_dialog_content),
            confirmText = stringResource(R.string.delete_category_dialog_confirm)
        )

        InventoryOverlay.ItemDetail -> DetailBottomSheet(
            foodItem = uiState.currentFoodItem,
            categoryUiModel = uiState.currentCategory,
            onDismiss = { onAction(InventoryAction.OnDismiss) },
            onEditClick = { onAction(InventoryDetailAction.OnEditClick) },
            onWastedClick = { onAction(InventoryDetailAction.OnWastedClick) },
            onConsumedClick = { onAction(InventoryDetailAction.OnConsumedClick) }
        )

        InventoryOverlay.CategoryAdd -> TextFieldDialog(
            value = uiState.categoryInput,
            title = stringResource(R.string.inventory_category_add),
            confirmText = stringResource(R.string.inventory_category_add_btn),
            onValueChange = {
                onAction(InventoryAction.OnAddCategory.OnInputChange(it))
            },
            onDismiss = { onAction(InventoryAction.OnDismiss) },
            onConfirm = { onAction(InventoryAction.OnAddCategory.Save) }
        )

        is InventoryOverlay.NewFeatureSheet -> AppNewFeatureBottomSheet(
            versionFeatures = overlay.versionFeature,
            onDismiss = { onAction(InventoryAction.OnDismiss) }
        )

        InventoryOverlay.ConsumeConfirm -> AlgidyAlertDialog(
            icon = Icons.Rounded.Restaurant,
            onDismissRequest = { onAction(InventoryAction.OnDismiss) },
            onConfirm = { onAction(InventoryAction.OnConsumeConfirm) },
            title = stringResource(R.string.detail_dialog_consume_title),
            text = stringResource(R.string.detail_dialog_consume_content),
            confirmText = stringResource(R.string.detail_fab_consume_this)
        )

        InventoryOverlay.WasteConfirm -> AlgidyAlertDialog(
            icon = Icons.Rounded.DeleteForever,
            onDismissRequest = { onAction(InventoryAction.OnDismiss) },
            onConfirm = { onAction(InventoryAction.OnWasteConfirm) },
            title = stringResource(R.string.detail_dialog_waste_title),
            text = stringResource(R.string.detail_dialog_waste_content),
            confirmText = stringResource(R.string.detail_fab_mark_as_wasted),
            isDestructive = true
        )

        InventoryOverlay.CameraPolicySheet -> {
            CameraPolicyBottomSheet(
                onConfirm = {
                    onAction(InventoryAction.OnConfirmCameraPolicy)
                },
                onDismiss = { onAction(InventoryAction.OnDismiss) }
            )
        }
    }

    if (uiState.expanded) {
        Scrim(
            color = MaterialTheme.colorScheme.surface,
            onClick = { onAction(InventoryFabAction.ToggleFabMenu(false)) },
            contentDescription = "scrim",
            modifier = Modifier.fillMaxSize(),
            alpha = { 0.6f }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp, end = 16.dp)
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomEnd
    ) {
        InventoryFabMenu(
            modifier = Modifier,
            expanded = uiState.expanded,
            onExpandClose = { onAction(InventoryFabAction.ToggleFabMenu(it)) },
            onManualClick = { onAction(InventoryFabAction.Manual) },
            onSettingClick = { onAction(InventoryFabAction.Setting) },
            onBarcodeScanClick = {
                val isGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
                Timber.d("BarcodeScan click triggered. isGranted=$isGranted")
                onAction(InventoryFabAction.BarcodeScan(isGranted))
            },
            onAnalyticsClick = { onAction(InventoryFabAction.Analytics) }
        )
    }
}
 