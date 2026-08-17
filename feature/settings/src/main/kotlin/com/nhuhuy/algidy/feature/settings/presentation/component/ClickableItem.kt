package com.nhuhuy.algidy.feature.settings.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.toImageVector
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.utils.ItemPosition
import com.nhuhuy.algidy.feature.settings.presentation.model.ClickableType
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingClickableItem
import com.nhuhuy.algidy.feature.settings.utils.toStringRes

@Composable
fun ClickableItem(
    modifier: Modifier = Modifier,
    item: SettingClickableItem,
    position: ItemPosition,
    onClick: (SettingClickableItem) -> Unit,
) {
    val algidySettings = AlgidyIcons.Settings
    val icon = when (item.type) {
        ClickableType.AboutApp -> algidySettings.AboutApp
        ClickableType.CopyRight -> algidySettings.License
        ClickableType.DailyReminder -> algidySettings.Notifications
        ClickableType.DeleteAll -> algidySettings.DeleteAll
        ClickableType.Export -> algidySettings.ExportData
        ClickableType.Feedback -> algidySettings.Feedback
        ClickableType.Import -> algidySettings.ImportData
        ClickableType.NewFeatures -> algidySettings.NewFeature
        ClickableType.OpenSource -> algidySettings.OpenSource
        ClickableType.PrivacyPolicy -> algidySettings.PrivatePolicy
        is ClickableType.Language -> algidySettings.Language
        ClickableType.WidgetDebug -> algidySettings.WidgetDebug
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
        is ClickableType.Language -> stringResource(R.string.setting_language)
        ClickableType.WidgetDebug -> stringResource(R.string.setting_widget_debug)
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
        is ClickableType.Language -> stringResource(item.type.currentLanguage.toStringRes())
        ClickableType.WidgetDebug -> stringResource(R.string.setting_widget_debug_desc)
    }

    ClickableSettingItem(
        modifier = modifier,
        position = position,
        icon = icon.toImageVector(),
        description = desc,
        title = title,
        onClick = { onClick(item) },
    )
}