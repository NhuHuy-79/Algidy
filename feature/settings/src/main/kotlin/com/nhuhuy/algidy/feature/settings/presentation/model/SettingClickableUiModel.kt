package com.nhuhuy.algidy.feature.settings.presentation.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.designsystem.icon.IconProvider
import com.nhuhuy.algidy.core.presentation.R

@Stable
enum class SettingClickableUiModel(
    @param:StringRes val title: Int,
    @param:StringRes val description: Int,
    val icon: IconProvider,
) {
    APPEARANCE(
        title = R.string.appearance_title,
        description = R.string.appearance_subtitle,
        icon = AlgidyIcons.Settings.Appearance
    ),

    NOTIFICATION(
        title = R.string.notification_settings_title,
        description = R.string.notification_settings_subtitle,
        icon = AlgidyIcons.Settings.Notifications
    ),

    YOUR_DATA(
        title = R.string.your_data_title,
        description = R.string.your_data_subtitle,
        icon = AlgidyIcons.Settings.YourData
    ),

    OTHER_SETTING(
        title = R.string.other_settings_title_page,
        description = R.string.other_settings_subtitle,
        icon = AlgidyIcons.Settings.OtherSetting
    ),

    ABOUT(
        title = R.string.setting_about_app,
        description = R.string.setting_about_app_desc,
        icon = AlgidyIcons.Settings.AboutApp
    ),

    DEBUG(
        title = R.string.setting_widget_debug,
        description = R.string.setting_widget_debug_desc,
        icon = AlgidyIcons.Settings.WidgetDebug
    ),

    DAILY_REMINDER(
        title = R.string.setting_daily_reminder,
        description = R.string.setting_daily_reminder_desc,
        icon = AlgidyIcons.Settings.Notifications
    ),

    EXPORT(
        title = R.string.setting_export,
        description = R.string.setting_export_desc,
        icon = AlgidyIcons.Settings.ExportData
    ),

    IMPORT(
        title = R.string.setting_import,
        description = R.string.setting_import_desc,
        icon = AlgidyIcons.Settings.ImportData
    ),

    LANGUAGE(
        title = R.string.setting_language,
        description = 0,
        icon = AlgidyIcons.Settings.Language
    ),

    DELETE_ALL(
        title = R.string.setting_clear_data,
        description = R.string.setting_clear_data_desc,
        icon = AlgidyIcons.Settings.DeleteAll
    ),

    NEW_FEATURES(
        title = R.string.setting_new_features,
        description = R.string.setting_new_features_desc,
        icon = AlgidyIcons.Settings.NewFeature
    ),

    COPYRIGHT(
        title = R.string.setting_copyright,
        description = R.string.setting_copyright_desc,
        icon = AlgidyIcons.Settings.License
    ),

    FEEDBACK(
        title = R.string.setting_feedback,
        description = R.string.setting_feedback_desc,
        icon = AlgidyIcons.Settings.Feedback
    ),

    PRIVACY_POLICY(
        title = R.string.setting_privacy_policy,
        description = R.string.setting_privacy_policy_desc,
        icon = AlgidyIcons.Settings.PrivatePolicy
    ),

    OPEN_SOURCE(
        title = R.string.setting_open_source,
        description = R.string.setting_open_source_desc,
        icon = AlgidyIcons.Settings.OpenSource
    ),

    CHECK_UPDATE(
        title = R.string.setting_check_update_title,
        description = R.string.setting_check_update_desc,
        icon = AlgidyIcons.Settings.CheckUpdate
    )
}
