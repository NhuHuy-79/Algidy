package com.nhuhuy.algidy.feature.settings.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.component.SelectLanguageRow
import com.nhuhuy.algidy.feature.settings.presentation.component.ToggleItem
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsUiState
import com.nhuhuy.algidy.core.presentation.R as CoreR

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OtherSettingsScreen(
    uiState: SettingsUiState,
    onAction: (SettingsAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(CoreR.string.other_settings_title_page),
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.Black
                            )
                        )
                        Text(
                            text = stringResource(CoreR.string.other_settings_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(SettingsAction.OnBackClick) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onAction(SettingsAction.SetNotifyTime.OpenPicker) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.NotificationAdd,
                            contentDescription = "timer"
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(CoreR.string.setting_language),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                    SelectLanguageRow(
                        currentLanguage = uiState.language,
                        onLanguageSelected = { language ->
                            onAction(SettingsAction.ChangeLanguage(language))
                        }
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(CoreR.string.settings_notifications),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                    ToggleItem(
                        item = uiState.notificationSetting,
                        position = ItemPosition.TOP,
                        onToggle = { enabled, _ ->
                            onAction(
                                SettingsAction.ToggleAction(
                                    type = uiState.notificationSetting.type,
                                    enabled = enabled
                                )
                            )
                        }
                    )
                    ToggleItem(
                        item = uiState.biometricSetting,
                        position = ItemPosition.MIDDLE,
                        onToggle = { enabled, _ ->
                            onAction(
                                SettingsAction.ToggleAction(
                                    type = uiState.biometricSetting.type,
                                    enabled = enabled
                                )
                            )
                        }
                    )
                    ToggleItem(
                        item = uiState.categorySetting,
                        position = ItemPosition.BOTTOM,
                        onToggle = { enabled, _ ->
                            onAction(
                                SettingsAction.ToggleAction(
                                    type = uiState.categorySetting.type,
                                    enabled = enabled
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
