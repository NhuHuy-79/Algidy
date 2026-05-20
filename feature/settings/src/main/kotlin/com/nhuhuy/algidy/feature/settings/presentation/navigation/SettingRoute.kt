package com.nhuhuy.algidy.feature.settings.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalResources
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.settings.presentation.SettingsScreen
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsEvent
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingRoute(
    onNavigateBack: () -> Unit,
) = BoxLayout {
    val viewModel: SettingsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    val snackBarHostState = remember { SnackbarHostState() }
    val resouce = LocalResources.current

    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            SettingsEvent.ExportData.SUCCESS -> {
                snackBarHostState.showSnackbar(
                    message = resouce.getString(R.string.export_success),
                    withDismissAction = true
                )
            }

            SettingsEvent.ExportData.FAILURE -> {
                snackBarHostState.showSnackbar(
                    message = resouce.getString(R.string.export_failed),
                    withDismissAction = true
                )

            }

            SettingsEvent.ImportData.SUCCESS -> {
                snackBarHostState.showSnackbar(
                    message = resouce.getString(R.string.import_success),
                    withDismissAction = true
                )

            }

            SettingsEvent.ImportData.FAILURE -> {
                snackBarHostState.showSnackbar(
                    message = resouce.getString(R.string.export_failed),
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
}