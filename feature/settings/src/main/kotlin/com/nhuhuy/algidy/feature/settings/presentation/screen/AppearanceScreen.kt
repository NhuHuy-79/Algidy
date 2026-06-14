package com.nhuhuy.algidy.feature.settings.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.component.SelectFontRow
import com.nhuhuy.algidy.feature.settings.presentation.component.ToggleItem
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsUiState
import com.nhuhuy.algidy.feature.settings.utils.toStringRes
import com.nhuhuy.algidy.core.presentation.R as CoreR

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppearanceScreen(
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
                            text = stringResource(CoreR.string.appearance_title),
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.Black
                            )
                        )
                        Text(
                            text = stringResource(CoreR.string.appearance_subtitle),
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
                        text = stringResource(CoreR.string.setting_app_theme),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.fillMaxSize(),
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
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(CoreR.string.setting_font),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                    SelectFontRow(
                        currentFont = uiState.font,
                        onFontSelected = { font ->
                            onAction(SettingsAction.ChangeFont(font))
                        }
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(CoreR.string.settings_dynamic_color),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                    ToggleItem(
                        item = uiState.dynamicColorSetting,
                        position = ItemPosition.SINGLE,
                        onToggle = { enabled, _ ->
                            onAction(
                                SettingsAction.ToggleAction(
                                    type = uiState.dynamicColorSetting.type,
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
