package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.nhuhuy.algidy.core.model.VersionFeatures
import com.nhuhuy.algidy.core.model.setting.AppFont
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingToggleItem
import com.nhuhuy.algidy.feature.settings.presentation.model.ToggleType

@Immutable
data class SettingsCombineState(
    val notificationGranted: Boolean = true,
    val biometricSupported: Boolean = true,
    val dynamicColorSupported: Boolean = true,

    val notificationsEnabled: Boolean = true,
    val biometricEnabled: Boolean = false,
    val dynamicColorEnabled: Boolean = false,

    val darkMode: DarkMode = DarkMode.SYSTEM,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val font: AppFont = AppFont.DEFAULT,
    val categoryEnabled: Boolean = false,
    val overlay: SettingsOverlay = SettingsOverlay.None,
    val hour: Int = 7,
    val minutes: Int = 30,
    val weeklyReport: Boolean = false,
    val warningDays: Int = 3,
) {
    val dynamicColorSetting: SettingToggleItem
        get() = SettingToggleItem(
            type = ToggleType.DYNAMIC_COLOR,
            enable = dynamicColorSupported,
            checked = dynamicColorEnabled
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
            checked = notificationGranted && notificationsEnabled
        )

    val categorySetting: SettingToggleItem
        get() = SettingToggleItem(
            type = ToggleType.CATEGORY_GROUP,
            enable = true,
            checked = categoryEnabled
        )

    val weekendReportSetting: SettingToggleItem
        get() = SettingToggleItem(
            type = ToggleType.WEEKLY_REPORT,
            enable = true,
            checked = weeklyReport
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


