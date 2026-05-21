package com.nhuhuy.algidy.feature.settings.presentation.navigation

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
import com.nhuhuy.algidy.feature.settings.presentation.SettingsScreen
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
                    withDismissAction = true
                )

            }

            SettingsEvent.ImportData.SUCCESS -> {
                snackBarHostState.showSnackbar(
                    message = resource.getString(R.string.import_success),
                    withDismissAction = true
                )

            }

            SettingsEvent.ImportData.FAILURE -> {
                snackBarHostState.showSnackbar(
                    message = resource.getString(R.string.export_failed),
                    withDismissAction = true
                )

            }

            SettingsEvent.NavigateBack -> {

            }
        }
    }

    SettingsScreen(
        snackBarHostState = snackBarHostState,
        uiState = uiState,
        onAction = onAction,
        onNavigateBack = onNavigateBack
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

    }
}