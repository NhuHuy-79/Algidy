package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition

fun LazyListScope.otherSettingItems(
    isNotificationEnabled: Boolean,
    isBiometricLock: Boolean,
    isDynamicColor: Boolean,
    onToggleNotification: (enabled: Boolean) -> Unit,
    onToggleBiometricLock: (enabled: Boolean) -> Unit,
    onToggleDynamicColor: (enabled: Boolean) -> Unit
) {
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
            enabled = isNotificationEnabled,
            text = stringResource(R.string.settings_notifications_desc),
            title = stringResource(R.string.settings_notifications),
            onToggleClick = onToggleNotification,
        )
    }

    item {
        ToggleableSettingItem(
            position = ItemPosition.MIDDLE,
            enabled = isBiometricLock,
            text = stringResource(R.string.setting_biometric_desc),
            title = stringResource(R.string.setting_biometric),
            onToggleClick = onToggleBiometricLock,
        )
    }

    item {
        ToggleableSettingItem(
            enabled = isDynamicColor,
            position = ItemPosition.BOTTOM,
            text = stringResource(R.string.settings_dynamic_mode_desc),
            title = stringResource(R.string.settings_dynamic_color),
            onToggleClick = onToggleDynamicColor
        )
    }
}


fun LazyListScope.dataSettingItem(
    onDataExport: () -> Unit,
    onDataImport: () -> Unit,
    onAboutAppClick: () -> Unit,
) {
    item {
        Text(
            text = stringResource(R.string.setting_data),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }

    item {
        ClickableSettingItem(
            position = ItemPosition.TOP,
            icon = Icons.Rounded.Upload,
            description = stringResource(R.string.setting_export_desc),
            title = stringResource(R.string.setting_export),
            onClick = onDataExport
        )
    }

    item {
        ClickableSettingItem(
            position = ItemPosition.MIDDLE,
            icon = Icons.Rounded.Download,
            description = stringResource(R.string.setting_import_desc),
            title = stringResource(R.string.setting_import),
            onClick = onDataImport
        )
    }

    item {
        ClickableSettingItem(
            position = ItemPosition.BOTTOM,
            icon = Icons.Rounded.Info,
            description = stringResource(R.string.setting_about_app_desc),
            title = stringResource(R.string.setting_about_app),
            onClick = onAboutAppClick
        )
    }

}