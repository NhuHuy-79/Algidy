package com.nhuhuy.algidy.feature.settings.navigation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nhuhuy.algidy.core.designsystem.component.AlgidyAlertDialog
import com.nhuhuy.algidy.core.designsystem.component.BoxLayout
import com.nhuhuy.algidy.core.presentation.ObserveEffect
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.component.AppNewFeatureBottomSheet
import com.nhuhuy.algidy.core.presentation.component.AppTimePickerDialog
import com.nhuhuy.algidy.core.presentation.navigation.Destination
import com.nhuhuy.algidy.core.presentation.navigation.SettingDestination
import com.nhuhuy.algidy.feature.settings.presentation.component.about_app.CopyrightBottomSheet
import com.nhuhuy.algidy.feature.settings.presentation.component.about_app.PolicyBottomSheet
import com.nhuhuy.algidy.feature.settings.presentation.component.open_source.OpenSourceContent
import com.nhuhuy.algidy.feature.settings.presentation.component.other_setting.SelectLanguageBottomSheet
import com.nhuhuy.algidy.feature.settings.presentation.screen.AboutAppScreen
import com.nhuhuy.algidy.feature.settings.presentation.screen.AppearanceScreen
import com.nhuhuy.algidy.feature.settings.presentation.screen.DataSettingsScreen
import com.nhuhuy.algidy.feature.settings.presentation.screen.MainSettingsScreen
import com.nhuhuy.algidy.feature.settings.presentation.screen.NotificationScreen
import com.nhuhuy.algidy.feature.settings.presentation.screen.OtherSettingsScreen
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.DeleteAll
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.NotifyTimerEvent
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction.SetNotifyTime.SetHourAndMinutes
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsEvent
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsOverlay
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingRoute(
    destination: SettingDestination,
    onNavigateToSettingRoute: (Destination.Setting) -> Unit,
    onNavigateBack: () -> Unit,
) = BoxLayout {
    val viewModel: SettingsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val combineState by viewModel.combineState.collectAsStateWithLifecycle()
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

    val notificationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            onAction(SettingsAction.OnNotificationGranted(granted))
        }
    )

    val context = LocalContext.current

    ObserveEffect(viewModel.uiEvent) { event ->
        when (event) {
            SettingsEvent.ExportData.SUCCESS -> {
                snackBarHostState.showSnackbar(
                    message = resource.getString(R.string.export_success),
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
            }

            SettingsEvent.ExportData.FAILURE -> {
                snackBarHostState.showSnackbar(
                    message = resource.getString(R.string.export_failed),
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )

            }

            SettingsEvent.ImportData.Success -> {
                snackBarHostState.showSnackbar(
                    message = resource.getString(R.string.import_success),
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )

            }

            SettingsEvent.ImportData.Failure -> {
                snackBarHostState.showSnackbar(
                    message = resource.getString(R.string.import_fail),
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )

            }

            SettingsEvent.NavigateBack -> onNavigateBack()

            SettingsEvent.ImportData.PickUri -> {
                pickZipLauncher.launch("application/zip")
            }

            NotifyTimerEvent.Error -> {

            }

            NotifyTimerEvent.Success -> {
                snackBarHostState.showSnackbar(
                    message = resource.getString(R.string.settings_set_time_success),
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
            }

            SettingsEvent.RequestNotificationPermission -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

            is SettingsEvent.ShowSnackBar -> {
                snackBarHostState.showSnackbar(
                    message = event.message,
                    duration = SnackbarDuration.Short
                )
            }

            DeleteAll.Success -> {
                snackBarHostState.showSnackbar(
                    message = resource.getString(R.string.settings_delete_success),
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )
            }

            DeleteAll.Failure -> {
                snackBarHostState.showSnackbar(
                    message = resource.getString(R.string.settings_delete_failure),
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )
            }

            SettingsEvent.SendFeedBackEmail -> {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("nguyennhuhuy42@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "[Algidy Feedback] Bug Report & Suggestions")
                    val emailBody = """
            --- Device Info (Do not delete) ---
            App Version: ${uiState.versionName}
            Android OS: API ${Build.VERSION.SDK_INT} (${Build.VERSION.RELEASE})
            Device Model: ${Build.MANUFACTURER} ${Build.MODEL}
            -----------------------------------
            
            Please write your feedback or bug description below:
            
        """.trimIndent()
                    putExtra(Intent.EXTRA_TEXT, emailBody)
                }

                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            SettingsEvent.OpenSourceClick -> onNavigateToSettingRoute(
                Destination.Setting(SettingDestination.OpenSource)
            )
        }
    }
    when (destination) {
        SettingDestination.Appearance -> AppearanceScreen(
            combineState = combineState,
            onAction = onAction
        )

        SettingDestination.Main -> MainSettingsScreen(
            uiState = uiState,
            onNavigate = onNavigateToSettingRoute,
            onBackClick = onNavigateBack
        )

        SettingDestination.OtherSettings -> {
            OtherSettingsScreen(
                combineState = combineState,
                onAction = onAction
            )
        }

        SettingDestination.YourData -> {
            DataSettingsScreen(
                snackBarHost = snackBarHostState,
                onAction = onAction
            )
        }

        SettingDestination.Notification -> {
            NotificationScreen(
                snackBarHost = snackBarHostState,
                combineState = combineState,
                onAction = onAction
            )
        }

        SettingDestination.AboutApp -> {
            AboutAppScreen(
                uiState = uiState,
                onAction = onAction
            )
        }

        SettingDestination.OpenSource -> {
            OpenSourceContent(
                onBack = onNavigateBack
            )
        }
    }

    when (val overlay = uiState.overlay) {
        SettingsOverlay.None -> Unit
        SettingsOverlay.DeleteAlertDialog -> AlgidyAlertDialog(
            icon = ImageVector.vectorResource(R.drawable.ic_delete),
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
        )

        SettingsOverlay.TimePicker -> AppTimePickerDialog(
            hour = combineState.hour,
            minutes = combineState.minutes,
            title = stringResource(R.string.settings_set_time),
            confirmText = stringResource(R.string.settings_set_time_confirm),
            onDateSelected = { hour, min ->
                onAction(
                    SetHourAndMinutes(
                        hour = hour,
                        minutes = min
                    )
                )
            },
            onDismiss = {
                onAction(SettingsAction.OnDismiss)
            }
        )

        is SettingsOverlay.NewFeatureSheet -> AppNewFeatureBottomSheet(
            versionFeatures = overlay.versionFeatures,
            onDismiss = { onAction(SettingsAction.OnDismiss) }
        )

        SettingsOverlay.CopyrightSheet -> CopyrightBottomSheet(
            onDismiss = { onAction(SettingsAction.OnDismiss) }
        )

        SettingsOverlay.OpenSourceSheet -> OpenSourceContent(
            onBack = { onAction(SettingsAction.OnDismiss) }
        )

        SettingsOverlay.PolicySheet -> PolicyBottomSheet(
            onDismiss = { onAction(SettingsAction.OnDismiss) }
        )

        is SettingsOverlay.LanguageSheet -> SelectLanguageBottomSheet(
            currentLanguage = combineState.language,
            onLanguageSelect = { language -> onAction(SettingsAction.ChangeLanguage(language)) },
            onDismiss = { onAction(SettingsAction.OnDismiss) }
        )
    }
}