package com.nhuhuy.algidy.feature.settings.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.feature.settings.presentation.component.SelectFontRow
import com.nhuhuy.algidy.feature.settings.presentation.component.SelectLanguageRow
import com.nhuhuy.algidy.feature.settings.presentation.component.dataSettingItem
import com.nhuhuy.algidy.feature.settings.presentation.component.otherSettingItems
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsUiState
import com.nhuhuy.algidy.feature.settings.utils.toStringRes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onAction: (SettingsAction) -> Unit,
    onNavigateBack: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    val pickZipLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { sourceUri ->
            onAction(SettingsAction.ImportData(sourceUri))
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.displaySmall
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.settings_dynamic_color),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 8.dp),
                ) {
                    DarkMode.entries.forEachIndexed { index, darkMode ->
                        SegmentedButton(
                            icon = {
                                SegmentedButtonDefaults.Icon(active = uiState.darkMode == darkMode)
                            },
                            label = {
                                Text(
                                    text = stringResource(darkMode.toStringRes()),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            },
                            selected = uiState.darkMode == darkMode,
                            onClick = {
                                onAction(SettingsAction.SetDarkMode(darkMode))
                            },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = DarkMode.entries.size
                            ),
                        )
                    }
                }
            }

            item {
                Text(
                    text = stringResource(R.string.setting_language),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            item {
                SelectLanguageRow(
                    currentLanguage = uiState.language,
                    onLanguageSelected = { language ->
                        onAction(SettingsAction.ChangeLanguage(language))
                    }
                )
            }

            item {
                Text(
                    text = stringResource(R.string.setting_font),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            item {
                SelectFontRow(
                    currentFont = uiState.font,
                    onFontSelected = { font ->
                        onAction(SettingsAction.ChangeFont(font))
                    }
                )
            }

            otherSettingItems(
                isNotificationEnabled = uiState.isNotificationsEnabled,
                isBiometricLock = uiState.isBiometricLock,
                isDynamicColor = uiState.isDynamicColor,
                isCategoryEnabled = uiState.categoryEnabled,
                onToggleNotification = { enabled ->
                    onAction(SettingsAction.ToggleNotifications(enabled))
                },
                onToggleBiometricLock = { enabled ->
                    onAction(SettingsAction.ToggleBiometricLock(enabled))
                },
                onToggleCategoryGroup = { enabled ->
                    onAction(SettingsAction.ToggleCategoryGroup(enabled))
                },
                onToggleDynamicColor = { enabled ->
                    onAction(SettingsAction.ToggleDynamicColor(enabled))
                }
            )

            item {
                Spacer(Modifier.height(8.dp))
            }

            dataSettingItem(
                onDataExport = {
                    onAction(SettingsAction.ExportData)
                },
                onDataImport = {
                    scope.launch {
                        pickZipLauncher.launch("application/zip")
                    }
                },
                onAboutAppClick = {},
                onDeleteDataClick = {
                    onAction(SettingsAction.ClearData)
                }
            )

        }
    }
}

