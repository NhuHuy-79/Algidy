package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingToggleItem
import com.nhuhuy.algidy.feature.settings.presentation.model.ToggleType

@Composable
fun ToggleItem(
    modifier: Modifier = Modifier,
    item: SettingToggleItem,
    position: ItemPosition,
    onToggle: (enable: Boolean, item: SettingToggleItem) -> Unit
) {
    val title = when (item.type) {
        ToggleType.BIOMETRIC_AUTH -> stringResource(R.string.setting_biometric)
        ToggleType.DYNAMIC_COLOR -> stringResource(R.string.settings_dynamic_color)
        ToggleType.NOTIFICATION -> stringResource(R.string.settings_notifications)
        ToggleType.CATEGORY_GROUP -> stringResource(R.string.setting_use_category)
        ToggleType.WEEKLY_REPORT -> stringResource(R.string.setting_weekly_report)
    }

    val desc = when (item.type) {
        ToggleType.BIOMETRIC_AUTH -> stringResource(R.string.setting_biometric_desc)
        ToggleType.DYNAMIC_COLOR -> stringResource(R.string.settings_dynamic_mode_desc)
        ToggleType.NOTIFICATION -> stringResource(R.string.settings_notifications_desc)
        ToggleType.CATEGORY_GROUP -> stringResource(R.string.setting_use_category_des)
        ToggleType.WEEKLY_REPORT -> stringResource(R.string.setting_weekly_report_desc)
    }

    ToggleableSettingItem(
        modifier = modifier,
        position = position,
        checked = item.checked,
        enabled = item.enable,
        text = desc,
        title = title,
        onToggleClick = { enabled ->
            onToggle(enabled, item)
        }
    )
}