package com.nhuhuy.algidy.feature.settings.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.ModeEdit
import androidx.compose.material.icons.rounded.NotificationImportant
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.core.presentation.utils.toRoundedCornerShape
import com.nhuhuy.algidy.feature.settings.presentation.component.ToggleItem
import com.nhuhuy.algidy.feature.settings.presentation.model.ToggleType
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsAction
import com.nhuhuy.algidy.feature.settings.presentation.viewmodel.SettingsCombineState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NotificationScreen(
    snackBarHost: SnackbarHostState,
    combineState: SettingsCombineState,
    onAction: (SettingsAction) -> Unit,
) {
    val expiryStepValues = listOf(1, 3, 5, 7)
    val initialIndex = expiryStepValues.indexOf(combineState.warningDays).coerceAtLeast(0).toFloat()
    var selectedIndex by remember(combineState.warningDays) { mutableFloatStateOf(initialIndex) }
    val actualDayValue = expiryStepValues[selectedIndex.toInt()]

    val deleteThresholdStepValue = listOf(0, 7, 14, 21, 28)
    val initialDeleteIndex =
        deleteThresholdStepValue.indexOf(combineState.deleteThresholdDays).coerceAtLeast(0)
            .toFloat()
    var selectedDeleteIndex by remember(combineState.deleteThresholdDays) {
        mutableFloatStateOf(
            initialDeleteIndex
        )
    }
    val actualDeleteValue = deleteThresholdStepValue[selectedDeleteIndex.toInt()]

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHost)
        },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.notification_settings_title),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                },
                subtitle = {
                    Text(
                        text = stringResource(R.string.notification_settings_subtitle),
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
                },
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
                ToggleItem(
                    item = combineState.notificationSetting,
                    position = ItemPosition.TOP,
                    onToggle = { enabled, _ ->
                        onAction(
                            SettingsAction.ToggleAction(
                                type = ToggleType.NOTIFICATION,
                                enabled = enabled
                            )
                        )
                    }

                )
            }

            item {
                ToggleItem(
                    item = combineState.weekendReportSetting,
                    position = ItemPosition.BOTTOM,
                    onToggle = { enabled, item ->
                        onAction(
                            SettingsAction.ToggleAction(type = item.type, enabled = enabled)
                        )
                    }
                )
            }

            item {
                Text(
                    text = "Expiry Reminder",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            item {
                ListItem(
                    modifier = Modifier.clip(
                        ItemPosition.TOP.toRoundedCornerShape()
                    ),
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    headlineContent = {
                        Text(
                            text = stringResource(R.string.setting_daily_reminder)
                        )
                    },
                    supportingContent = {
                        RemindTimeText(combineState.hour, combineState.minutes)
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Rounded.Alarm,
                            contentDescription = null
                        )
                    },
                    trailingContent = {
                        FilledIconButton(
                            onClick = {
                                onAction(SettingsAction.SetNotifyTime.OpenPicker)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ModeEdit,
                                contentDescription = null
                            )
                        }
                    }
                )
            }

            item {
                ListItem(
                    modifier = Modifier.clip(
                        ItemPosition.MIDDLE.toRoundedCornerShape()
                    ),
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    headlineContent = {
                        Text(
                            text = pluralStringResource(
                                id = R.plurals.notification_days_before,
                                count = actualDayValue,
                                actualDayValue
                            )
                        )
                    },
                    supportingContent = {
                        Slider(
                            modifier = Modifier
                                .height(36.dp)
                                .padding(vertical = 8.dp),
                            value = selectedIndex,
                            onValueChange = {
                                selectedIndex = it
                                onAction(SettingsAction.SetWarningDays(expiryStepValues[it.toInt()]))
                            },
                            valueRange = 0f..(expiryStepValues.lastIndex.toFloat()),
                            steps = expiryStepValues.size - 2,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Rounded.NotificationImportant,
                            contentDescription = null
                        )
                    },
                )
            }

            item {
                ListItem(
                    modifier = Modifier.clip(
                        ItemPosition.BOTTOM.toRoundedCornerShape()
                    ),
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    headlineContent = {
                        Text(
                            text = if (actualDeleteValue == 0)
                                stringResource(R.string.settings_never_delete)
                            else
                                pluralStringResource(
                                    id = R.plurals.settings_delete_threshold,
                                    count = actualDeleteValue,
                                    actualDeleteValue
                                )
                        )
                    },
                    supportingContent = {
                        Slider(
                            modifier = Modifier
                                .height(36.dp)
                                .padding(vertical = 8.dp),
                            value = selectedDeleteIndex,
                            onValueChange = {
                                selectedDeleteIndex = it
                                onAction(
                                    SettingsAction.SetDeleteThresholdDays(
                                        deleteThresholdStepValue[it.toInt()]
                                    )
                                )
                            },
                            valueRange = 0f..(deleteThresholdStepValue.lastIndex.toFloat()),
                            steps = deleteThresholdStepValue.size - 2,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                            contentDescription = null
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun RemindTimeText(hour: Int, minute: Int) {
    val appLocale = LocalConfiguration.current.locales[0]

    val localTime = LocalTime.of(hour, minute)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", appLocale)
    val formattedTimeNumbers = localTime.format(timeFormatter)

    val amPmFormatter = DateTimeFormatter.ofPattern("a", appLocale)
    val formattedAmPm = localTime.format(amPmFormatter)

    Text(
        text = stringResource(
            id = R.string.setting_daily_reminder_desc,
            formattedTimeNumbers,
            formattedAmPm
        ),
        style = MaterialTheme.typography.bodyMedium
    )
}