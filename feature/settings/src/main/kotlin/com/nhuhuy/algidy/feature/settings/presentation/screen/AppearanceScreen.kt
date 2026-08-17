package com.nhuhuy.algidy.feature.settings.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ButtonGroup
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
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.component.ColorPresetPicker
import com.nhuhuy.algidy.feature.settings.presentation.component.ToggleItem
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsCombineState
import com.nhuhuy.algidy.feature.settings.utils.toStringRes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppearanceScreen(
    combineState: SettingsCombineState,
    onAction: (SettingsAction) -> Unit,
) {
    val localResource = LocalResources.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.setting_about_app),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                },
                subtitle = {
                    Text(
                        text = stringResource(R.string.setting_about_app_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.setting_app_theme),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black
                        )
                    )


                    ButtonGroup(
                        modifier = Modifier.fillMaxWidth(),
                        overflowIndicator = {},
                        expandedRatio = 0f,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        DarkMode.entries.forEachIndexed { index, mode ->
                            this.toggleableItem(
                                weight = 1f,
                                checked = combineState.appearancePreferences.darkMode == mode,
                                onCheckedChange = {
                                    onAction(SettingsAction.SetDarkMode(mode))
                                },
                                icon = {},
                                label = localResource.getString(mode.toStringRes())
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.settings_dynamic_color),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                    ToggleItem(
                        item = combineState.dynamicColorSetting,
                        position = ItemPosition.TOP,
                        onToggle = { enabled, _ ->
                            onAction(
                                SettingsAction.ToggleAction(
                                    type = combineState.dynamicColorSetting.type,
                                    enabled = enabled
                                )
                            )
                        }
                    )
                }
            }

            item {
                ColorPresetPicker(
                    enabled = !combineState.appearancePreferences.enableDynamicColor,
                    seedColor = combineState.appearancePreferences.seedColor,
                    onSeedColorSelect = { color ->
                        onAction(SettingsAction.SetSeedColorPreset(color))
                    }
                )
            }
        }
    }
}
