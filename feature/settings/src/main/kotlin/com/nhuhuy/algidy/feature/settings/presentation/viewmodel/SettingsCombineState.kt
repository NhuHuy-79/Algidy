package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.datastore.model.AppearancePreferences
import com.nhuhuy.algidy.core.datastore.model.GeneralPreferences
import com.nhuhuy.algidy.core.datastore.model.NotificationPreferences
import com.nhuhuy.algidy.core.model.VersionFeatures
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState
import com.nhuhuy.algidy.feature.settings.domain.model.SettingDataPreferences
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingToggleItem
import com.nhuhuy.algidy.feature.settings.presentation.model.ToggleType

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
    val generalPreferences: GeneralPreferences = GeneralPreferences()
) {
    val dynamicColorSetting: SettingToggleItem
        get() = SettingToggleItem(
            type = ToggleType.DYNAMIC_COLOR,
            enable = dynamicColorSupported,
            checked = appearancePreferences.enableDynamicColor
        )

    val biometricSetting: SettingToggleItem
        get() = SettingToggleItem(
            type = ToggleType.BIOMETRIC_AUTH,
            enable = biometricSupported,
            checked = biometricEnabled
        )

    val notificationSetting: SettingToggleItem
        get() = SettingToggleItem(
            type = ToggleType.NOTIFICATION,
            enable = true,
            checked = notificationGranted && notificationPreferences.enableNotification
        )

    val categorySetting: SettingToggleItem
        get() = SettingToggleItem(
            type = ToggleType.CATEGORY_GROUP,
            enable = true,
            checked = appearancePreferences.enableCategoryGroup
        )

    val weekendReportSetting: SettingToggleItem
        get() = SettingToggleItem(
            type = ToggleType.WEEKLY_REPORT,
            enable = true,
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
}


