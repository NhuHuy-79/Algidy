package com.nhuhuy.algidy.feature.settings.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.navigation.Destination
import com.nhuhuy.algidy.core.presentation.navigation.SettingDestination
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.component.ClickableSettingItem
import com.nhuhuy.algidy.feature.settings.presentation.model.ClickableType
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsUiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainSettingsScreen(
    uiState: SettingsUiState,
    onAction: (SettingsAction) -> Unit,
    onNavigate: (Destination.Setting) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.main_settings_title_page),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                },
                subtitle = {
                    Text(
                        text = stringResource(R.string.main_settings_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            item {
                ClickableSettingItem(
                    position = ItemPosition.TOP,
                    icon = Icons.Rounded.ColorLens,
                    title = stringResource(R.string.appearance_title),
                    description = stringResource(R.string.appearance_subtitle),
                    onClick = { onNavigate(Destination.Setting(SettingDestination.Appearance)) }
                )
            }

            item {
                ClickableSettingItem(
                    position = ItemPosition.MIDDLE,
                    icon = Icons.Rounded.NotificationsActive,
                    title = stringResource(R.string.notification_settings_title),
                    description = stringResource(R.string.notification_settings_subtitle),
                    onClick = { onNavigate(Destination.Setting(SettingDestination.Notification)) }
                )
            }

            item {
                ClickableSettingItem(
                    position = ItemPosition.BOTTOM,
                    icon = Icons.Rounded.Storage,
                    title = stringResource(R.string.your_data_title),
                    description = stringResource(R.string.your_data_subtitle),
                    onClick = { onNavigate(Destination.Setting(SettingDestination.YourData)) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                ClickableSettingItem(
                    position = ItemPosition.TOP,
                    icon = Icons.Rounded.Settings,
                    title = stringResource(R.string.other_settings_title_page),
                    description = stringResource(R.string.other_settings_subtitle),
                    onClick = { onNavigate(Destination.Setting(SettingDestination.OtherSettings)) }
                )
            }

            item {
                ClickableSettingItem(
                    position = ItemPosition.MIDDLE,
                    icon = Icons.Rounded.Widgets,
                    title = stringResource(R.string.setting_widget_debug),
                    description = stringResource(R.string.setting_widget_debug_desc),
                    onClick = {
                        onAction(SettingsAction.ClickableAction(ClickableType.WidgetDebug))
                    }
                )
            }

            item {
                ClickableSettingItem(
                    position = ItemPosition.BOTTOM,
                    icon = Icons.Rounded.Apps,
                    title = stringResource(R.string.setting_about_app),
                    description = stringResource(
                        R.string.setting_about_app_desc,
                        uiState.versionName
                    ),
                    onClick = { onNavigate(Destination.Setting(SettingDestination.AboutApp)) }
                )
            }
        }
    }
}
