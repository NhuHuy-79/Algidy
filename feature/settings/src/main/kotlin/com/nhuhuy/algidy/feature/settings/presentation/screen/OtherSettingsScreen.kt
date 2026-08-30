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
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.component.ClickableItem
import com.nhuhuy.algidy.feature.settings.presentation.component.ToggleItem
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingClickableUiModel
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsCombineState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OtherSettingsScreen(
    combineState: SettingsCombineState,
    onAction: (SettingsAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.other_settings_title_page),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                },
                subtitle = {
                    Text(
                        text = stringResource(R.string.other_settings_subtitle),
                    )
                },
                navigationIcon = {
                    FilledTonalIconButton(
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
                ClickableItem(
                    item = SettingClickableUiModel.LANGUAGE,
                    position = ItemPosition.SINGLE,
                    onClick = { onAction(SettingsAction.ClickableAction(it)) }
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ToggleItem(
                        item = combineState.biometricSetting,
                        position = ItemPosition.TOP,
                        onToggle = { enabled, item ->
                            onAction(
                                SettingsAction.ToggleAction(
                                    type = item.type,
                                    enabled = enabled
                                )
                            )
                        }
                    )
                    ToggleItem(
                        item = combineState.categorySetting,
                        position = ItemPosition.BOTTOM,
                        onToggle = { enabled, item ->
                            onAction(
                                SettingsAction.ToggleAction(
                                    type = item.type,
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
