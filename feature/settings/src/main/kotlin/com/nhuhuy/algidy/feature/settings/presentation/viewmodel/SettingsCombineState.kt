package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.datastore.model.AppearancePreferences
import com.nhuhuy.algidy.core.datastore.model.GeneralPreferences
import com.nhuhuy.algidy.core.datastore.model.NotificationPreferences
import com.nhuhuy.algidy.core.designsystem.icon.AlgidyIcons
import com.nhuhuy.algidy.core.model.VersionFeatures
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.presentation.R
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState
import com.nhuhuy.algidy.feature.settings.domain.model.SettingDataPreferences
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingToggleType
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingToggleUiModel

@Immutable
data class SettingsCombineState(
    val notificationGranted: Boolean = true,
    val biometricSupported: Boolean = true,
    val dynamicColorSupported: Boolean = true,
    val biometricEnabled: Boolean = false,
    val overlay: SettingsOverlay = SettingsOverlay.None,
    val settingPref: SettingDataPreferences = SettingDataPreferences(),
    val notificationPreferences: NotificationPreferences = NotificationPreferences(),
    val appearancePreferences: AppearancePreferences = AppearancePreferences(),
    val generalPreferences: GeneralPreferences = GeneralPreferences(),
    val exceptionLog: String? = null,
) {
    val dynamicColorSetting: SettingToggleUiModel
        get() = SettingToggleUiModel(
            type = SettingToggleType.DYNAMIC_COLOR,
            title = R.string.settings_dynamic_color,
            description = R.string.settings_dynamic_mode_desc,
            icon = AlgidyIcons.Settings.Appearance,
            enabled = dynamicColorSupported,
            checked = appearancePreferences.enableDynamicColor
        )

    val biometricSetting: SettingToggleUiModel
        get() = SettingToggleUiModel(
            type = SettingToggleType.BIOMETRIC_AUTH,
            title = R.string.setting_biometric,
            description = R.string.setting_biometric_desc,
            icon = AlgidyIcons.Settings.YourData,
            enabled = biometricSupported,
            checked = biometricEnabled
        )

    val notificationSetting: SettingToggleUiModel
        get() = SettingToggleUiModel(
            type = SettingToggleType.NOTIFICATION,
            title = R.string.settings_notifications,
            description = R.string.settings_notifications_desc,
            icon = AlgidyIcons.Settings.Notifications,
            enabled = true,
            checked = notificationGranted && notificationPreferences.enableNotification
        )

    val categorySetting: SettingToggleUiModel
        get() = SettingToggleUiModel(
            type = SettingToggleType.CATEGORY_GROUP,
            title = R.string.setting_use_category,
            description = R.string.setting_use_category_des,
            icon = AlgidyIcons.Settings.OtherSetting,
            enabled = true,
            checked = appearancePreferences.enableCategoryGroup
        )

    val weekendReportSetting: SettingToggleUiModel
        get() = SettingToggleUiModel(
            type = SettingToggleType.WEEKLY_REPORT,
            title = R.string.setting_weekly_report,
            description = R.string.setting_weekly_report_desc,
            icon = AlgidyIcons.Settings.Notifications,
            enabled = true,
            checked = notificationPreferences.enableWeeklyReport
        )
}

@Stable
data class SettingsUiState(
    val versionFeatures: VersionFeatures? = null,
    val versionName: String = "1.0.0",
    val overlay: SettingsOverlay = SettingsOverlay.None
) : UiState

sealed interface SettingsOverlay {
    data object None : SettingsOverlay
    data object DeleteAlertDialog : SettingsOverlay
    data object TimePicker : SettingsOverlay
    data class LanguageSheet(val currentLanguage: AppLanguage) : SettingsOverlay
    data class NewFeatureSheet(val versionFeatures: VersionFeatures) : SettingsOverlay
    data object PolicySheet : SettingsOverlay
    data object CopyrightSheet : SettingsOverlay
    data object OpenSourceSheet : SettingsOverlay
    data object WidgetDebugSheet : SettingsOverlay
}
