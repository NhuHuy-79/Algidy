package com.nhuhuy.algidy.feature.settings.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.component.ToggleItem
import com.nhuhuy.algidy.feature.settings.presentation.component.appearance.ColorPresetPicker
import com.nhuhuy.algidy.feature.settings.presentation.component.appearance.ThemePicker
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsCombineState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppearanceScreen(
    combineState: SettingsCombineState,
    onAction: (SettingsAction) -> Unit,
) {
    LocalResources.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.appearance_title),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                },
                subtitle = {
                    Text(
                        text = stringResource(R.string.appearance_subtitle),
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
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            item {
                ThemePicker(
                    itemPosition = ItemPosition.TOP,
                    themeMode = combineState.appearancePreferences.themeMode,
                    onThemeModeChange = { mode ->
                        onAction(SettingsAction.SetDarkMode(mode))
                    }
                )
            }

            item {
                ToggleItem(
                    item = combineState.dynamicColorSetting,
                    position = ItemPosition.MIDDLE,
                    onToggle = { enabled, item ->
                        onAction(
                            SettingsAction.ToggleAction(type = item.type, enabled = enabled)
                        )
                    }
                )
            }

            item {
                ColorPresetPicker(
                    itemPosition = ItemPosition.BOTTOM,
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
