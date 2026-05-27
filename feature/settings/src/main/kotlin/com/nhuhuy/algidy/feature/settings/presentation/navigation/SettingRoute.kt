package com.nhuhuy.algidy.feature.settings.presentation.navigation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.AppTimePickerDialog
import com.nhuhuy.algidy.feature.settings.presentation.SettingsScreen
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.NotifyTimerEvent
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsEvent
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsOverlay
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingRoute(
    onNavigateBack: () -> Unit,
) = BoxLayout {
    val viewModel: SettingsViewModel = koinViewModel()
    val overlay by viewModel.overlay.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    val snackBarHostState = remember { SnackbarHostState() }
    val resource = LocalResources.current
    val pickZipLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { sourceUri ->
            onAction(SettingsAction.ImportData(sourceUri))
        }
    }

    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            SettingsEvent.ExportData.SUCCESS -> {
                snackBarHostState.showSnackbar(
                    message = resource.getString(R.string.export_success),
                    withDismissAction = true
                )
            }

            SettingsEvent.ExportData.FAILURE -> {
                snackBarHostState.showSnackbar(
                    message = resource.getString(R.string.export_failed),
                )

            }

            SettingsEvent.ImportData.Success -> {
                snackBarHostState.showSnackbar(
                    message = resource.getString(R.string.import_success),
                )

            }

            SettingsEvent.ImportData.Failure -> {
                snackBarHostState.showSnackbar(
                    message = resource.getString(R.string.export_failed),
                )

            }

            SettingsEvent.NavigateBack -> onNavigateBack()

            SettingsEvent.ImportData.PickUri -> {
                pickZipLauncher.launch("application/zip")
            }

            NotifyTimerEvent.Error -> {

            }

            NotifyTimerEvent.Success -> {

            }
        }
    }

    SettingsScreen(
        snackBarHostState = snackBarHostState,
        uiState = uiState,
        onAction = onAction
    )

    when (overlay) {
        SettingsOverlay.NONE -> Unit
        SettingsOverlay.DELETE_ALERT_DIALOG -> AlgidyAlertDialog(
            confirmText = stringResource(R.string.delete_data_dialog_confirm),
            dismissText = stringResource(R.string.delete_data_dialog_cancel),
            onDismissRequest = {
                onAction(SettingsAction.DeleteAlertDialog.Dismiss)
            },
            onConfirm = {
                onAction(SettingsAction.DeleteAlertDialog.Confirm)
            },
            title = stringResource(R.string.delete_data_dialog_title),
            text = stringResource(R.string.delete_data_dialog_content),
            icon = Icons.Rounded.DeleteForever,
        )

        SettingsOverlay.TIME_PICKER -> AppTimePickerDialog(
            hour = uiState.hour,
            minutes = uiState.minutes,
            title = stringResource(R.string.settings_set_time),
            confirmText = stringResource(R.string.settings_set_time_confirm),
            dismissText = stringResource(R.string.settings_set_time_cancel),
            onDateSelected = { hour, min ->
                onAction(
                    SettingsAction.SetNotifyTime.SetHourAndMinutes(
                        hour = hour,
                        minutes = min
                    )
                )
            },
            onDismiss = {
                onAction(SettingsAction.OnDismiss)
            }
        )
    }
}