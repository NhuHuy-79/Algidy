package com.nhuhuy.algidy.feature.scanner.presentation.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.model.food.FoodItem
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.TextFieldDialog
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.component.dialog.ScannerLoadingDialog
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.AddBarcodeDialogAction.OnConfirm
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.AddBarcodeDialogAction.OnValueChange
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerAction.OnDismissRequest
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerEvent
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerOverlay
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerUiState
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.ScannerViewModel
import com.nhuhuy.algidy.feature.scanner.presentation.scanner.viewmodel.WarningDialogAction
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun ScannerRoute(
    onNavigateBack: () -> Unit,
    onNavigateToFoodEntry: (FoodItem) -> Unit,
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val viewModel: ScannerViewModel = koinViewModel()
    val uiState: ScannerUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction
    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
            Timber.d("Camera Granted: $granted")
            if (!granted) onNavigateBack()
        }
    )

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (!hasCameraPermission) {
            Timber.d("Has permission: ${false}")
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            is ScannerEvent.OnSuccess -> {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                onNavigateToFoodEntry(event.foodItem)
            }

            is ScannerEvent.OnFailure -> {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.Reject)
            }
        }
    }

    LaunchedEffect(uiState.foodItemResult) {
        if (uiState.foodItemResult.name.isNotEmpty()) {
            onNavigateToFoodEntry(uiState.foodItemResult)
        }
    }

    BoxLayout {
        ScannerScreen(
            uiState = uiState,
            onClosePress = onNavigateBack,
            onAction = onAction
        )

        when (uiState.overlay) {
            ScannerOverlay.NONE -> Unit
            ScannerOverlay.LOADING_DIALOG -> {
                ScannerLoadingDialog(onDismissRequest = { onAction(OnDismissRequest) })
            }

            ScannerOverlay.BARCODE_DIALOG -> TextFieldDialog(
                value = uiState.barCodeInput,
                title = stringResource(R.string.scanner_barcode_dialog_title),
                confirmText = stringResource(R.string.scanner_barcode_confirm),
                onValueChange = { value -> onAction(OnValueChange(value)) },
                onDismiss = { onAction(OnDismissRequest) },
                onConfirm = { onAction(OnConfirm) }
            )

            ScannerOverlay.WARNING_DIALOG -> AlgidyAlertDialog(
                onConfirm = { onAction(WarningDialogAction.Confirm) },
                onDismissRequest = { onAction(OnDismissRequest) },
                title = stringResource(R.string.scanner_warning_dialog_title),
                text = stringResource(R.string.scanner_warning_dialog_content),
                dismissText = stringResource(R.string.scanner_warning_dialog_dismiss),
                confirmText = stringResource(R.string.scanner_warning_dialog_confirm)
            )
        }
    }
}
