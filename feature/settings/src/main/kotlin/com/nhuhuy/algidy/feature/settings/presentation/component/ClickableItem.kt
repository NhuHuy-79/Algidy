package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.BuildCircle
import androidx.compose.material.icons.rounded.Copyright
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.NewReleases
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.model.ClickableType
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingClickableItem

@Composable
fun ClickableItem(
    modifier: Modifier = Modifier,
    item: SettingClickableItem,
    position: ItemPosition,
    onClick: (SettingClickableItem) -> Unit,
) {
    val icon = when (item.type) {
        ClickableType.AboutApp -> Icons.Rounded.Apps
        ClickableType.CopyRight -> Icons.Rounded.Copyright
        ClickableType.DailyReminder -> Icons.Rounded.NotificationsActive
        ClickableType.DeleteAll -> ImageVector.vectorResource(R.drawable.ic_delete)
        ClickableType.Export -> Icons.Rounded.Upload
        ClickableType.Feedback -> Icons.Rounded.AlternateEmail
        ClickableType.Import -> Icons.Rounded.Download
        ClickableType.NewFeatures -> Icons.Rounded.NewReleases
        ClickableType.OpenSource -> Icons.Rounded.BuildCircle
        ClickableType.PrivacyPolicy -> Icons.Rounded.Policy
    }

    val title = when (item.type) {
        ClickableType.Export -> stringResource(R.string.setting_export)
        is ClickableType.Import -> stringResource(R.string.setting_import)
        ClickableType.DeleteAll -> stringResource(R.string.setting_clear_data)
        ClickableType.AboutApp -> stringResource(R.string.setting_about_app)
        ClickableType.DailyReminder -> stringResource(R.string.setting_daily_reminder)
        ClickableType.NewFeatures -> stringResource(R.string.setting_new_features)
        ClickableType.CopyRight -> stringResource(R.string.setting_copyright)
        ClickableType.Feedback -> stringResource(R.string.setting_feedback)
        ClickableType.OpenSource -> stringResource(R.string.setting_open_source)
        ClickableType.PrivacyPolicy -> stringResource(R.string.setting_privacy_policy)
    }

    val desc = when (item.type) {
        ClickableType.Export -> stringResource(R.string.setting_export_desc)
        is ClickableType.Import -> stringResource(R.string.setting_import_desc)
        ClickableType.DeleteAll -> stringResource(R.string.setting_clear_data_desc)
        ClickableType.AboutApp -> stringResource(R.string.setting_about_app_desc)
        ClickableType.DailyReminder -> stringResource(R.string.setting_daily_reminder_desc)
        ClickableType.NewFeatures -> stringResource(R.string.setting_new_features_desc)
        ClickableType.CopyRight -> stringResource(R.string.setting_copyright_desc)
        ClickableType.Feedback -> stringResource(R.string.setting_feedback_desc)
        ClickableType.OpenSource -> stringResource(R.string.setting_open_source_desc)
        ClickableType.PrivacyPolicy -> stringResource(R.string.setting_privacy_policy_desc)
    }

    ClickableSettingItem(
        modifier = modifier,
        position = position,
        icon = icon,
        description = desc,
        title = title,
        onClick = { onClick(item) },
    )
}