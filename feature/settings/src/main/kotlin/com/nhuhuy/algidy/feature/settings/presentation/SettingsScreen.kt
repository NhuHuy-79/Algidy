package com.nhuhuy.algidy.feature.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Upload
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.component.ClickableSettingItem
import com.nhuhuy.algidy.feature.settings.presentation.component.ToggleableSettingItem
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsUiState
import com.nhuhuy.algidy.feature.settings.utils.toStringRes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onAction: (SettingsAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
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
                    modifier = Modifier.padding(vertical = 4.dp)
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
                                        fontWeight = FontWeight.Black
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
                    text = stringResource(R.string.setting_other_settings_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            item {
                ToggleableSettingItem(
                    position = ItemPosition.TOP,
                    enabled = uiState.isNotificationsEnabled,
                    text = stringResource(R.string.settings_notifications_desc),
                    title = stringResource(R.string.settings_notifications),
                    onToggleClick = { enable ->
                        onAction(SettingsAction.ToggleNotifications(enabled = enable))
                    },
                )
            }

            item {
                ToggleableSettingItem(
                    position = ItemPosition.MIDDLE,
                    enabled = uiState.isBiometricLock,
                    text = stringResource(R.string.setting_biometric_desc),
                    title = stringResource(R.string.setting_biometric),
                    onToggleClick = { enable ->
                        onAction(SettingsAction.ToggleBiometricLock(enabled = enable))
                    },
                )
            }

            item {
                ToggleableSettingItem(
                    enabled = uiState.isDynamicColor,
                    position = ItemPosition.BOTTOM,
                    text = stringResource(R.string.settings_dynamic_mode_desc),
                    title = stringResource(R.string.settings_dynamic_color),
                    onToggleClick = { enable ->
                        onAction(SettingsAction.ToggleDynamicColor(enabled = enable))
                    }
                )
            }

            item {
                Spacer(Modifier.height(8.dp))
            }

            item {
                ClickableSettingItem(
                    position = ItemPosition.TOP,
                    icon = Icons.Rounded.Upload,
                    description = stringResource(R.string.setting_export_desc),
                    title = stringResource(R.string.setting_export),
                    onClick = {}
                )
            }

            item {
                ClickableSettingItem(
                    position = ItemPosition.MIDDLE,
                    icon = Icons.Rounded.Download,
                    description = stringResource(R.string.setting_import_desc),
                    title = stringResource(R.string.setting_import),
                    onClick = {}
                )
            }

            item {
                ClickableSettingItem(
                    position = ItemPosition.BOTTOM,
                    icon = Icons.Rounded.Info,
                    description = stringResource(R.string.setting_about_app_desc),
                    title = stringResource(R.string.setting_about_app),
                    onClick = {

                    }
                )
            }


        }
    }
}

