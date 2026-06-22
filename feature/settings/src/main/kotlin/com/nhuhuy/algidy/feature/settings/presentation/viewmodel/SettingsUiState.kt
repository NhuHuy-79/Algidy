package com.nhuhuy.algidy.feature.settings.presentation.viewmodel

import androidx.compose.runtime.Immutable
import com.nhuhuy.algidy.core.model.setting.AppFont
import com.nhuhuy.algidy.core.model.setting.AppLanguage
import com.nhuhuy.algidy.core.model.setting.DarkMode
import com.nhuhuy.algidy.core.presentation.viewmodel.UiState
import com.nhuhuy.algidy.feature.settings.presentation.model.ClickableType
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingClickableItem
import com.nhuhuy.algidy.feature.settings.presentation.model.SettingToggleItem
import com.nhuhuy.algidy.feature.settings.presentation.model.ToggleType

@Immutable
data class SettingsUiState(
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
    val overlay: SettingsOverlay = SettingsOverlay.NONE,
    val hour: Int = 7,
    val minutes: Int = 30
) : UiState {
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

    val dataClickableItems: List<SettingClickableItem>
        get() = listOf(
            SettingClickableItem(type = ClickableType.Export),
            SettingClickableItem(type = ClickableType.Import),
            SettingClickableItem(type = ClickableType.DeleteAll),
        )
}

enum class SettingsOverlay {
    NONE, DELETE_ALERT_DIALOG, TIME_PICKER
}
