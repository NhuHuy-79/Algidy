package com.nhuhuy.algidy.feature.inventory.navigation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.designsystem.motion.AlgidyMotion
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.InventoryOverlayContainer
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.InventoryScreen
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.component.InventoryVerticalToolbar
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryEvent
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryFabAction
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryOverlay
import com.nhuhuy.algidy.feature.inventory.presentation.inventory.viewmodel.InventoryViewModel
import org.koin.androidx.compose.koinViewModel

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
        when (event) {
            InventoryEvent.NavigateToScanner -> onAction(InventoryAction.OnCameraPermissionAccept)
            InventoryEvent.RequestCameraPermission -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    LaunchedEffect(
        key1 = uiState.currentVersionCode,
        key2 = combineState.generalPreferences.appVersionToNotify,
        key3 = combineState.isLoaded
    ) {
        if (combineState.isLoaded && uiState.currentVersionCode > combineState.generalPreferences.appVersionToNotify) {
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

    AnimatedVisibility(
        visible = !uiState.isSelectMode,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(horizontal = 16.dp)
            .padding(bottom = 144.dp),
        enter = AlgidyMotion.verticalToolbar.enter,
        exit = AlgidyMotion.verticalToolbar.exit
    ) {
        InventoryVerticalToolbar(
            state = uiState,
            onAction = onAction,
            onExpandChange = { onAction(InventoryFabAction.ToggleFabMenu(!uiState.expanded)) },
            onBarcodeScanClick = {
                val isGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
                onAction(InventoryFabAction.BarcodeScan(isGranted))
            },
            onSearchClick = { onAction(InventoryAction.OnSearchClick) },
            onAddManuallyClick = { onAction(InventoryFabAction.Manual) }
        )
    }

    InventoryOverlayContainer(
        uiState = uiState,
        onAction = onAction
    )
}
 